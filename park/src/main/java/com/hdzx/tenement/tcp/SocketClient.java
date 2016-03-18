package com.hdzx.tenement.tcp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.tcp.protocol.TcpReceiveTamplate;
import com.hdzx.tenement.tcp.protocol.TcpSendEntity;
import com.hdzx.tenement.tcp.protocol.TcpSendTemplate;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * @author Jesley
 *
 */
public class SocketClient
{
    private static final int MSG_WHAT_CALLBACK_ON_MAIN = 100;
    
    private static final int HEART_BEAT_INTERVAL = 180;
    
    private SocketChannel channel = null;
    
    private volatile boolean isConnected = false;
    
    private boolean isEnableService = false;
    
    private Thread receiveThread = null;
    
    private Thread sendThread = null;
    
    private BlockingQueue<TcpSendEntity> sendBlockingQueue = null;
    
    private ConcurrentHashMap<String, TcpSendEntity> waitAnswerMap = null;
    
    private Timer timeoutTimer = null;
    
    private TimerTask task = null;
    
    private Context context = null;
    
    private Handler handler = null;
    
    private int secondCounter = 0;

    private static SocketClient instance = new SocketClient();
    
    private SocketClient()
    {
        sendBlockingQueue = new LinkedBlockingQueue<TcpSendEntity>();
        waitAnswerMap = new ConcurrentHashMap<String, TcpSendEntity>();
        timeoutTimer = new Timer();
    }
    
    public static SocketClient getInstance()
    {
        return instance;
    }
    
    public void init(Context context)
    {
        this.context = context;
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                TcpSendEntity entity = null;
                TcpReceiveTamplate tamplate = null;
                
                try
                {
                    switch (msg.what)
                    {
                        case MSG_WHAT_CALLBACK_ON_MAIN:
                            Bundle bundle = msg.getData();
                            if (bundle != null)
                            {
                                entity = (TcpSendEntity) bundle.getSerializable(TcpContants.DATA_KEY_ENTITY);
                                tamplate = (TcpReceiveTamplate) bundle.getSerializable(TcpContants.DATA_KEY_RESPONSE_TEMPLATE);
                                entity.getTcpCallback().processCallbackData(tamplate);
                            }
                            break;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
    }
    
    public void sendRunMessageOnMain(TcpSendEntity entity, TcpReceiveTamplate tamplate)
    {
        if (entity != null && entity.getTcpCallback() != null && tamplate != null && handler != null)
        {
            Message msg = handler.obtainMessage();
            msg.what = MSG_WHAT_CALLBACK_ON_MAIN;
            Bundle bundle = new Bundle();
            bundle.putSerializable(TcpContants.DATA_KEY_ENTITY, entity);
            bundle.putSerializable(TcpContants.DATA_KEY_RESPONSE_TEMPLATE, tamplate);
            msg.setData(bundle);
            
            handler.sendMessage(msg);
        }
    }
    
    public void sendBroadcastMessage(TcpReceiveTamplate tamplate)
    {
        if (context != null)
        {
            Intent intent = new Intent(TcpContants.TCP_RESPONSE_BROADCAST);
            intent.putExtra(TcpContants.DATA_KEY_RESPONSE_TEMPLATE, tamplate);
            context.sendBroadcast(intent);
        }
    }
    
    public void sendBroadcastDirtyMessage(TcpReceiveTamplate tamplate)
    {
        if (context != null)
        {
            Intent intent = new Intent(TcpContants.TCP_RESPONSE_BROADCAST_DIRTY);
            intent.putExtra(TcpContants.DATA_KEY_RESPONSE_TEMPLATE, tamplate);
            context.sendBroadcast(intent);
        }
    }
    
    public boolean offerSendTcpEntity(TcpSendEntity entity)
    {
        return sendBlockingQueue.offer(entity);
    }
    
    public boolean offerTcpSendTemplate(TcpSendTemplate template)
    {
        TcpSendEntity entity = new TcpSendEntity();
        entity.setSendTemplate(template);
        return sendBlockingQueue.offer(entity);
    }
    
    public TcpSendEntity pollSendTcpEntity()
    {
        return sendBlockingQueue.poll();
    }
    
    public TcpSendEntity takeSendTcpEntity() throws InterruptedException
    {
        return sendBlockingQueue.take();
    }
    
    private void processSendBlockingQueueTimeout()
    {
        Iterator<TcpSendEntity>  iterator = sendBlockingQueue.iterator();
        if (iterator != null)
        {
            TcpSendEntity entity = null;
            while (iterator.hasNext())
            {
                entity = iterator.next();
                if (entity.timeout() <= 0)
                {
                    sendBlockingQueue.remove(entity);
                    entity.callback("调用超时");
                }
            }
        }
    }
    
    private void processWaitAnswerTimeout()
    {
        Set<Map.Entry<String, TcpSendEntity>> entrySet = waitAnswerMap.entrySet();
        Iterator<Map.Entry<String, TcpSendEntity>>  iterator = entrySet.iterator();
        if (iterator != null)
        {
            TcpSendEntity entity = null;
            Map.Entry<String, TcpSendEntity> entry = null;
            while (iterator.hasNext())
            {
                entry = iterator.next();
                entity = entry.getValue();
                if (entity.timeout() <= 0)
                {
                    waitAnswerMap.remove(entry.getKey());
                    entity.callback("调用超时");
                }
            }
        }
    }
    
    private void sendHeartBeatPacket()
    {
        if (UserSession.getInstance().isValidAesKey())
        {
            ++secondCounter;
            if (secondCounter >= HEART_BEAT_INTERVAL)
            {
                TcpSendTemplate template = new TcpSendTemplate();
                template.setRequest();
                template.setOperation(TcpContants.OperationCode.CODE_0x00HB);
                offerTcpSendTemplate(template);
                
                secondCounter = 0;
            }
        }
    }
    
    private void timeout()
    {
        processSendBlockingQueueTimeout();
        processWaitAnswerTimeout();
    }
    
    void putWaitAnswer(TcpSendEntity entity)
    {
        waitAnswerMap.put(entity.getSendTemplate().getSeq(), entity);
    }
    
    TcpSendEntity getEntityFromWaitAnswer(String key)
    {
        return waitAnswerMap.get(key);
    }
    
    TcpSendEntity removeEntityFromWaitAnswer(String key)
    {
        return waitAnswerMap.remove(key);
    }
    
    SocketChannel getChannel()
    {
        return channel;
    }
    
    boolean isConnected()
    {
        return isConnected;
    }
    
    void setConnected(boolean isConnected)
    {
        this.isConnected = isConnected;
    }
    
    
    public synchronized void startService()
    {
        if (!isEnableService)
        {
            if (handler == null)
            {
                throw new IllegalStateException("first call init()method to initlize client.");
            }
            
            receiveThread = new Thread(new ReceiveRunnable());
            receiveThread.start();
            sendThread = new Thread(new SendRunnable());
            sendThread.start();
            
            timeoutTimer = new Timer();
            task = new TimerTask()
            {

                @Override
                public void run()
                {
                    sendHeartBeatPacket();
                    timeout();
                }
                
            };
            timeoutTimer.schedule(task, 0, 1000);
            
            isEnableService = true;
        }
    }
    
    public synchronized void stopService()
    {
        System.out.println("stopService...");
        if (isEnableService)
        {
            isEnableService = false;
            close();
            
            if (receiveThread != null)
            {
                receiveThread.interrupt();
            }
            
            if (receiveThread != null)
            {
                sendThread.interrupt();
            }
            
            if (timeoutTimer != null)
            {
                timeoutTimer.cancel();
                timeoutTimer = null;
            }
        }
    }
    
    synchronized void connect()
    {
        if (isConnected == false)
        {
            try
            {
                if (channel == null)
                {
                    channel = SocketChannel.open();
                    Socket socket = channel.socket();
                    socket.setSoTimeout(TcpContants.SOCKET_TIMEOUT_SEC);
                    socket.setKeepAlive(true);
                    System.out.println("建立连接……Blocking=" + channel.isBlocking());
                    SocketAddress address = new InetSocketAddress(TcpContants.SERVER_DOMAIN, TcpContants.SERVER_PORT);
                    socket.connect(address, TcpContants.SOCKET_CONNECTION_TIMEOUT_SEC);
                    isConnected = true;
                    System.out.println("连接已建立……");
                }
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    synchronized void close()
    {
        System.out.println("close...");
        isConnected = false;
        try
        {
            if (channel != null)
            {
                channel.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        channel = null;
    }
    
    private class TimeoutRunnable implements Runnable
    {

        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            
        }
        
    }

    public static void main(String[] args) throws UnknownHostException, IOException
    {
        SocketClient.getInstance().startService();
        
        try
        {
            Thread.sleep(5*1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        //SocketClient.getInstance().stopService();
    }
}