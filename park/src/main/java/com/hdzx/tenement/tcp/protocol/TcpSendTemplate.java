package com.hdzx.tenement.tcp.protocol;

import com.google.gson.Gson;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.tcp.TcpContants;
import com.hdzx.tenement.utils.AESUtils;
import com.hdzx.tenement.utils.CommonUtil;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jesley
 */
public class TcpSendTemplate implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Map<String, Object> headMap = new LinkedHashMap<String, Object>();
    
    private Map<String, Object> dataMap = new LinkedHashMap<String, Object>();

    private boolean isRequestTicket = false;

    private boolean isRequest = true;
    
    private boolean isServerCallback = false;
    
    private String requestCode = null;
    
    private Object userData = null;
    
    private String operation = null;
    
    private String message = null;
    
    private String seq = null;
    
    private CryptoTyepEnum cryptoType = CryptoTyepEnum.aes;

    public String getContent()
    {
        Gson gson = new Gson();
        makeHead();

        Map<String, Object> contentMap = new HashMap<String, Object>();
        contentMap.put(TcpContants.PROTOCOL_CONTEN_KEY.head.name(), headMap);

        Map<String, Object> bodyMap = new LinkedHashMap<String, Object>();
        if (isRequestTicket() && UserSession.getInstance().getAccessTicket()!= null)
        {
            bodyMap.put(TcpContants.REQUEST_TERMINAL_BODY.ticket.name(), UserSession.getInstance().getAccessTicket());
        }
        bodyMap.put(TcpContants.REQUEST_TERMINAL_BODY.data.name(), dataMap);
        
        String bodyString = gson.toJson(bodyMap);
        System.out.println("TcpSendTemplate, body=" + bodyString);
        String bodyEncodeString = null;
        if (cryptoType == CryptoTyepEnum.none)
        {
            bodyEncodeString = bodyString;
        }
        else if (cryptoType == CryptoTyepEnum.aes)
        {
            bodyEncodeString = AESUtils.encrypt(UserSession.getInstance().getAesKey(), bodyString);
        }
        else
        {
            bodyEncodeString = bodyString;
        }
        
        contentMap.put(Contants.PROTOCOL_CONTEN_KEY.body.name(), bodyEncodeString);

        return gson.toJson(contentMap);
    }

    protected void makeHead()
    {
        if (headMap == null)
        {
            headMap = new LinkedHashMap<String, Object>();
        }

        headMap.put(TcpContants.REQUEST_TERMINAL_HEAD.appid.name(), TcpContants.APPID);
        headMap.put(TcpContants.REQUEST_TERMINAL_HEAD.sessionid.name(), UserSession.getInstance().getSessionId());
        headMap.put(TcpContants.REQUEST_TERMINAL_HEAD.version.name(), TcpContants.VERSION);
        headMap.put(TcpContants.REQUEST_TERMINAL_HEAD.os.name(), TcpContants.OS);
        if (isRequest)
        {
            headMap.put(TcpContants.REQUEST_TERMINAL_HEAD.type.name(), "0");
            if (seq == null)
            {
                seq = System.currentTimeMillis() + "";
            }
        }
        else
        {
            headMap.put(TcpContants.REQUEST_TERMINAL_HEAD.type.name(), "1");
        }
        
        headMap.put(TcpContants.REQUEST_TERMINAL_HEAD.seq.name(), seq);
        
        headMap.put(TcpContants.REQUEST_TERMINAL_HEAD.operation.name(), operation);
        if (message != null)
        {
            headMap.put(TcpContants.REQUEST_TERMINAL_HEAD.msg.name(), message);
        }
        
        headMap.put(TcpContants.REQUEST_TERMINAL_HEAD.appver.name(), CommonUtil.appVersion);
        headMap.put(TcpContants.REQUEST_TERMINAL_HEAD.uuid.name(), CommonUtil.uuid);
    }

    public void appendData(String key, Object object)
    {
        if (key == null || object == null)
        {
            return;
        }

        if (dataMap == null)
        {
            dataMap = new LinkedHashMap<String, Object>();
        }

        dataMap.put(key, object);
    }

    public void appendData(Map<String, Object> map)
    {
        if (map == null)
        {
            return;
        }

        if (dataMap == null)
        {
            dataMap = new LinkedHashMap<String, Object>();
        }

        dataMap.putAll(map);
    }

    public void appendHead(String key, Object object)
    {
        if (key == null || object == null)
        {
            return;
        }

        if (headMap == null)
        {
            headMap = new LinkedHashMap<String, Object>();
        }

        headMap.put(key, object);
    }

    public void appendHead(Map<String, Object> map)
    {
        if (map == null)
        {
            return;
        }

        if (headMap == null)
        {
            headMap = new LinkedHashMap<String, Object>();
        }

        headMap.putAll(map);
    }

    public Map<String, Object> getDataMap()
    {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap)
    {
        this.dataMap = dataMap;
    }

    public Map<String, Object> getHeadMap()
    {
        return headMap;
    }

    public void setHeadMap(Map<String, Object> headMap)
    {
        this.headMap = headMap;
    }

    public boolean isRequestTicket()
    {
        return isRequestTicket;
    }

    public void setRequestTicket(boolean isRequestTicket)
    {
        this.isRequestTicket = isRequestTicket;
    }

    public boolean isRequest()
    {
        return isRequest;
    }
    
    public boolean isAnswer()
    {
        return !isRequest;
    }

    public void setRequest()
    {
        this.isRequest = true;
    }
    
    public void setAnswer()
    {
        this.isRequest = false;
    }

    public String getOperation()
    {
        return operation;
    }

    public void setOperation(String operation)
    {
        this.operation = operation;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public CryptoTyepEnum getCryptoType()
    {
        return cryptoType;
    }

    public void setCryptoType(CryptoTyepEnum cryptoType)
    {
        this.cryptoType = cryptoType;
    }

    public String getSeq()
    {
        return seq;
    }

    public void setSeq(String seq)
    {
        this.seq = seq;
    }

    public boolean isServerCallback()
    {
        return isServerCallback;
    }

    public void setServerCallback(boolean isServerCallback)
    {
        this.isServerCallback = isServerCallback;
    }

    public String getRequestCode()
    {
        return requestCode;
    }

    public void setRequestCode(String requestCode)
    {
        this.requestCode = requestCode;
    }

    public Object getUserData()
    {
        return userData;
    }

    public void setUserData(Object userData)
    {
        this.userData = userData;
    }
}
