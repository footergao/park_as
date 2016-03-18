package com.hdzx.tenement.common.deamon;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.http.protocol.*;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.utils.NetUtils;
import com.hdzx.tenement.utils.PreferencesUtils;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class AppDeamon implements IContentReportor
{
    private static final int RETRY_DELAY = 1000;
    
    private static final String REQUEST_CODE_AES = "request_code_aes";
    
    private static final String REQUEST_CODE_TICKET = "request_code_ticket";
    
    private static final int HANDLER_WHAT_ERROR = 1000;
    
    private static final int HANDLER_WHAT_EXE_AES = 1001;
    
    private static final int HANDLER_WHAT_EXE_TICKET = 1002;
    
    private static final int HANDLER_WHAT_REEXECUTE = 1003; 
    
    private BlockingQueue<BlockingEntity> waitForAESBlockingQueue = null;
    
    private BlockingQueue<BlockingEntity> waitForTicketBlockingQueue = null;
    
    private BlockingQueue<ReporterEntity> callbackBlockingQueue = null;
    
    private BlockingQueue<HttpAsyncTask> reexecuteBlockingQueue = null;
    
    private Context context = null;
    
    private HttpAsyncTask aesTask = null;
    
    private HttpAsyncTask ticketTask = null;
    
    private Handler handler = null;
    
    private Timer timer = null;
    
    private boolean isStart = false;
    
    private static AppDeamon instance = new AppDeamon();
    
    private AppDeamon()
    {
        
    }
    
    public static AppDeamon getInstance()
    {
        return instance;
    }
    
    public void start(Context context)
    {
        if (!isStart)
        {
            if (context == null)
            {
                throw new IllegalArgumentException("parameter context is null");
            }
            
            this.context = context;
            waitForAESBlockingQueue = new LinkedBlockingDeque<BlockingEntity>();
            waitForTicketBlockingQueue = new LinkedBlockingDeque<BlockingEntity>();
            callbackBlockingQueue =  new LinkedBlockingDeque<ReporterEntity>();
            reexecuteBlockingQueue =  new LinkedBlockingDeque<HttpAsyncTask>();
            
            handler = new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    switch (msg.what)
                    {
                        case HANDLER_WHAT_ERROR:
                            HttpAsyncTask task = null;
                            ResponseContentTamplate tamplate = null;
                            ReporterEntity entity = callbackBlockingQueue.poll();
                            while (entity != null)
                            {
                                task = entity.getHttpTask();
                                tamplate = entity.getResponseContent();
                                if (task != null && task.getContentReportor() != null && tamplate != null)
                                {
                                    task.getContentReportor().reportBackContent(tamplate);
                                }
                                
                                entity = callbackBlockingQueue.poll();
                            }
                            break;
                            
                        case HANDLER_WHAT_EXE_AES:
                            if (aesTask != null)
                            {
                                aesTask.execute(aesTask.getEntity());
                            }
                            break;
                            
                        case HANDLER_WHAT_EXE_TICKET:
                            if (ticketTask != null)
                            {
                                ticketTask.execute(ticketTask.getEntity());
                            }
                            break;
                            
                        case HANDLER_WHAT_REEXECUTE:
                            task =  reexecuteBlockingQueue.poll();
                            while (task != null)
                            {
                                task.execute(task.getEntity());
                                task =  reexecuteBlockingQueue.poll();
                            }
                            break;
                    }
                }
            };
            
            timer = new Timer();
            
            timer.schedule(new DeamonRunnable(), 0, 1000);
            
            isStart = true;
        }
    }
    
    public void stop()
    {
        if (isStart)
        {
            context = null;
            timer.cancel();
            timer = null;
            handler = null;
            waitForAESBlockingQueue.clear();
            waitForAESBlockingQueue = null;
            waitForTicketBlockingQueue.clear();
            waitForTicketBlockingQueue = null;
            callbackBlockingQueue.clear();
            callbackBlockingQueue = null;
            reexecuteBlockingQueue.clear();
            reexecuteBlockingQueue = null;
            
            isStart = false;
        }
    }
    
    private void offerReporterEntity(ReporterEntity entity)
    {
        callbackBlockingQueue.offer(entity);
    }
    
    private void offerReporterEntity(HttpAsyncTask task, ResponseContentTamplate tamplate)
    {
        callbackBlockingQueue.offer(new ReporterEntity(task, tamplate));
    }
    
    private void offerReexecuteTask(HttpAsyncTask task)
    {
        reexecuteBlockingQueue.offer(task);
    }
    
    private class DeamonRunnable extends TimerTask
    {
        @Override
        public void run()
        {
            processBlockingAESRequest();
            processBlockingTicketRequest();
        }
        
        private void processBlockingAESRequest()
        {
            try
            {
                Iterator<BlockingEntity> iterator = waitForAESBlockingQueue.iterator();
                processBlokingRequest(iterator, UserSession.getInstance().isValidAesKey(), false);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        private void processBlockingTicketRequest()
        {
            try
            {
                Iterator<BlockingEntity> iterator = waitForTicketBlockingQueue.iterator();
                boolean isBackExcute = (UserSession.getInstance().isValidAesKey()) && (UserSession.getInstance().getAccessTicket() != null);
                processBlokingRequest(iterator, isBackExcute, true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        private void processBlokingRequest(Iterator<BlockingEntity> iterator, boolean isBackExcute, boolean isTestUser)
        {
            if (iterator != null)
            {
                BlockingEntity blockingEntity = null;
                HttpAsyncTask task = null;
                while (iterator.hasNext())
                {
                    blockingEntity = iterator.next();
                    task = blockingEntity.getHttpTask();
                    if (isTestUser)
                    {
                        String userName = PreferencesUtils.getInstance().takeString(context, Contants.PREFERENCES_KEY.usn.name());
                        String password = PreferencesUtils.getInstance().takeString(context, Contants.PREFERENCES_KEY.psw.name());
                        if (userName == null || password == null)
                        {
                            processErrorCallback(task, Contants.ResponseInnerCode.CODE_CUST_0X900000, "无法获取用户名和密码刷取票据");
                            return;
                        }
                    }
                    
                    if (!NetUtils.isNetworkAvailable(context))
                    {
                        iterator.remove();
                        processErrorCallback(task, null, "网络不可用，请确认网络。");
                    }
                    else if (task.isDestroy())
                    {
                        iterator.remove();
                    }
                    else if (isBackExcute)
                    {
                        System.out.println("runnable...isBackExcute" + isBackExcute);
                        iterator.remove();
                        offerReexecuteTask(task);
                        handler.sendEmptyMessage(HANDLER_WHAT_REEXECUTE);
                    }
                    else
                    {
                        if (blockingEntity.timeout() <= 0)
                        {
                            System.out.println("runnable...timeout");
                            iterator.remove();
                            processErrorCallback(task, null, "服务器繁忙，请稍后尝试。");
                        }
                    }
                }
            }
        }
        
        private void processErrorCallback(HttpAsyncTask task, String code, String message)
        {
            IContentReportor reportor = task.getContentReportor();
            HttpRequestEntity entity = null;
            if (reportor != null)
            {
                ResponseContentTamplate responseContent = new ResponseContentTamplate();
                entity = task.getEntity();
                if (entity != null)
                {
                    responseContent.setResponseCode(entity.getRequestCode());
                    responseContent.setUserData(entity.getUserData());
                    responseContent.setDecryptoType(entity.getResponseDecryptoType());
                }
                responseContent.setInnerErrorCode(code);
                responseContent.SetErrorMsg(message);
                
                offerReporterEntity(task, responseContent);
                handler.sendEmptyMessage(HANDLER_WHAT_ERROR);
            }
        }
    }
    
    private void makeAESRequest(String userName, String password)
    {
//    	Toast.makeText(context, "正在刷新应用···", Toast.LENGTH_SHORT).show();
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(CryptoTyepEnum.rsa);

        //BODY
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.imei.name(), tm.getDeviceId());
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.imsi.name(), tm.getSubscriberId());
        if (userName != null && !"".equals(userName) && password != null && !"".equals(password))
        {
            reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.loginName.name(), userName);
            reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.password.name(), password);
            reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.type.name(), Contants.LONGIN_TYPE_REFRSH);
        }
        
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.GET_AES.getValue());
        httpRequestEntity.setRequestCode(REQUEST_CODE_AES);
        httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.rsa);
        aesTask = new HttpAsyncTask(context, this);
        aesTask.execute(httpRequestEntity);
    }
    
    private void makeRefreshTicketRequest(String userName, String password)
    {
//    	Toast.makeText(context, "正在刷新应用···", Toast.LENGTH_SHORT).show();
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(CryptoTyepEnum.aes);

        //BODY
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.loginName.name(), userName);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.password.name(), password);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.type.name(), Contants.LONGIN_TYPE_REFRSH);

        //SEND
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.LOGIN.getValue());
        httpRequestEntity.setRequestCode(REQUEST_CODE_TICKET);
        httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
        ticketTask = new HttpAsyncTask(context, this);
        ticketTask.execute(httpRequestEntity);
    }
    
    public void refreshAES()
    {
        if (aesTask == null)
        {
            if (UserSession.getInstance().isLogin())
            {
                String userName = PreferencesUtils.getInstance().takeString(context, Contants.PREFERENCES_KEY.usn.name());
                String password = PreferencesUtils.getInstance().takeString(context, Contants.PREFERENCES_KEY.psw.name());
                if (userName != null && !"".equals(userName) && password != null && !"".equals(password))
                {
                    makeAESRequest(userName, password);
                }
                else
                {
                    makeAESRequest(null, null);
                }
            }
            else
            {
                makeAESRequest(null, null);
            }
        }
    }
    
    public void refreshAES(HttpAsyncTask task)
    {
        refreshAES();
        if (task != null)
        {
            waitForAESBlockingQueue.offer(new BlockingEntity(task));
        }
    }
    
    public void refreshTicket(HttpAsyncTask task)
    {
        String userName = PreferencesUtils.getInstance().takeString(context, Contants.PREFERENCES_KEY.usn.name());
        String password = PreferencesUtils.getInstance().takeString(context, Contants.PREFERENCES_KEY.psw.name());
        if (userName == null || password == null || "".equals(userName.trim()) || "".equals(password.trim()))
        {
            ResponseContentTamplate responseContent = new ResponseContentTamplate();
            HttpRequestEntity entity = task.getEntity();
            if (entity != null)
            {
                responseContent.setResponseCode(entity.getRequestCode());
                responseContent.setUserData(entity.getUserData());
                responseContent.setDecryptoType(entity.getResponseDecryptoType());
            }
            
            responseContent.setInnerErrorCode(Contants.ResponseInnerCode.CODE_CUST_0X900000);
            responseContent.setInnerErrorMsg("无法获取用户名和密码刷取票据");
            return;
        }
        
        if (ticketTask == null)
        {
            makeRefreshTicketRequest(userName, password);
        }
        
        if (ticketTask != null)
        {
            waitForTicketBlockingQueue.offer(new BlockingEntity(task));
        }
    }

    @Override
    public void reportBackContent(ResponseContentTamplate responseContent)
    {
        if (responseContent.getResponseCode().equals(REQUEST_CODE_AES))
        {
            if (responseContent.getErrorMsg() != null
                    || !Contants.ResponseCode.CODE_000000.equals(responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name())))
            {
                handler.sendEmptyMessageDelayed(HANDLER_WHAT_EXE_AES, RETRY_DELAY);
            }
            else
            {
                String aesKey = (String) responseContent.getInMapData(Contants.PROTOCOL_RESP_BODY.aes.name());
                String sessonId = (String) responseContent.getInMapData(Contants.PROTOCOL_RESP_BODY.sessionid.name());
                String ticket = (String) responseContent.getInMapData(Contants.PROTOCOL_RESP_BODY.ticket.name());
                String imagehost = (String) responseContent.getInMapData(Contants.PROTOCOL_RESP_BODY.imghost.name());
                String frontUrl = (String) responseContent.getInMapData(Contants.PROTOCOL_RESP_BODY.frontUrl.name());
                if (aesKey == null || sessonId == null)
                {
                    handler.sendEmptyMessageDelayed(HANDLER_WHAT_EXE_AES, RETRY_DELAY);
                }
                else
                {
                    UserSession.getInstance().setAesKey(aesKey);
                    UserSession.getInstance().setSessionId(sessonId);
                    UserSession.getInstance().setImageHost(imagehost);
                    UserSession.getInstance().setFrontUrl(frontUrl);;
                    if (ticket != null && !"".equals(ticket))
                    {
                        UserSession.getInstance().setAccessTicket(ticket);
                    }
                    aesTask = null;
                }
            }

        }
        else if (responseContent.getResponseCode().equals(REQUEST_CODE_TICKET))
        {
            if (responseContent.getErrorMsg() != null
                    || !Contants.ResponseCode.CODE_000000.equals(responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name())))
            {
                handler.sendEmptyMessageDelayed(HANDLER_WHAT_EXE_TICKET, RETRY_DELAY);
            }
            else
            {
                Object obj = responseContent.getData();
                if ((obj instanceof String) && obj != null && !"".equals(obj))
                {
                    UserSession.getInstance().setAccessTicket((String) obj);
                    ticketTask = null;
                }
                else
                {
                    handler.sendEmptyMessageDelayed(HANDLER_WHAT_EXE_TICKET, RETRY_DELAY);
                }
            }
        }
    }
    
    public boolean requestIntercepter(HttpAsyncTask task)
    {
        boolean isIntercept = false;
        if (NetUtils.isNetworkAvailable(context))
        {
            if (aesTask != task && ticketTask != task)
            {
                if (task.getEntity() != null 
                        && task.getEntity().getPath() != null
                        && task.getEntity().getPath().endsWith(Contants.PROTOCOL_COMMAND.GET_AES.getValue()))
                {
                    isIntercept = true;
                }
                else if (!UserSession.getInstance().isValidAesKey())
                {
                    refreshAES(task);
                    isIntercept = true;
                }
                else
                {
                    if (task.getEntity() != null && task.getEntity().getRequestContentTemplate() != null)
                    {
                        if (task.getEntity().getRequestContentTemplate().isRequestTicket()
                                && UserSession.getInstance().getAccessTicket() == null)
                        {
                            refreshTicket(task);
                            isIntercept = true;
                        }
                    }
                }
            }
        }
        
        return isIntercept;
    }
    
    public boolean responseIntercepter(HttpAsyncTask task, ResponseContentTamplate tamplate)
    {
        boolean isIntercept = false;
        if (NetUtils.isNetworkAvailable(context))
        {
            if (aesTask != task && ticketTask != task)
            {
                if (Contants.ResponseCode.CODE_900003.equals(tamplate.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name())))
                {
                    UserSession.getInstance().setValidAesKey(false);
                    refreshAES(task);
                    isIntercept = true;
                }
                else if (Contants.ResponseCode.CODE_900001.equals(tamplate.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name())))
                {
                    UserSession.getInstance().setAccessTicket(null);
                    refreshTicket(task);
                    isIntercept = true;
                }
            }
        }
        
        return isIntercept;
    }
}
