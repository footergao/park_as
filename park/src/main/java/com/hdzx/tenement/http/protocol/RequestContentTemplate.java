package com.hdzx.tenement.http.protocol;

import com.google.gson.Gson;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.utils.*;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;

import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jesley
 */
public class RequestContentTemplate
{
    private Map<String, Object> dataMap = new LinkedHashMap<String, Object>();

    private Map<String, Object> headMap = new LinkedHashMap<String, Object>();
    
    private String body = null;

    private boolean isRequestTicket = false;

    private boolean isRequestVersion = true;

    private CryptoTyepEnum encryptoType = CryptoTyepEnum.aes;
    
    public String getContent()
    {
        Gson gson = new Gson();
        makeHead();

        Map<String, Object> contentMap = new HashMap<String, Object>();
        contentMap.put(Contants.PROTOCOL_CONTEN_KEY.head.name(), headMap);
        
        if (body == null)
        {
            Map<String, Object> bodyMap = new HashMap<String, Object>();
            if (isRequestTicket && UserSession.getInstance().getAccessTicket() != null)
            {
                bodyMap.put(Contants.PROTOCOL_REQ_BODY.ticket.name(), UserSession.getInstance().getAccessTicket());
            }
            
            if (dataMap != null)
            {
                bodyMap.put(Contants.PROTOCOL_REQ_BODY.data.name(), dataMap);
            }

            String bodyString = gson.toJson(bodyMap);
            System.out.println("RequestContentTemplate:bodyString=" + bodyString);
            String bodyEncodeString = null;
            if (encryptoType == CryptoTyepEnum.aes)
            {
                // AES加密
                bodyEncodeString = AESUtils.encrypt(UserSession.getInstance().getAesKey(), bodyString);
            }
            else if (encryptoType == CryptoTyepEnum.rsa)
            {
                try
                {
                    PublicKey publicKey = RSAUtils.loadPublicKey(Contants.rsaPublicKey);
                    byte[] bodyByte = RSAUtils.encryptDataByPublicKey(bodyString.getBytes("UTF-8"), publicKey);
                    bodyEncodeString = Base64.encodeBytes(bodyByte);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    bodyEncodeString = Base64.encodeBytes(bodyString.getBytes("UTF-8"));
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }

            contentMap.put(Contants.PROTOCOL_CONTEN_KEY.body.name(), bodyEncodeString);
        }
        else
        {
            contentMap.put(Contants.PROTOCOL_CONTEN_KEY.body.name(), body);
        }

        return gson.toJson(contentMap);
    }

    protected void makeHead()
    {
        if (headMap == null)
        {
            headMap = new LinkedHashMap<String, Object>();
        }

        headMap.put(Contants.PROTOCOL_REQ_HEAD.appid.name(), Contants.APPID);
        if (UserSession.getInstance().getAesKey() == null)
        {
            headMap.put(Contants.PROTOCOL_REQ_HEAD.sessionid.name(), "");
        }
        else
        {
            headMap.put(Contants.PROTOCOL_REQ_HEAD.sessionid.name(), UserSession.getInstance().getSessionId());
        }
        
        if (isRequestVersion)
        {
            headMap.put(Contants.PROTOCOL_REQ_HEAD.version.name(), Contants.VERSION);
        }
        
        headMap.put(Contants.PROTOCOL_REQ_HEAD.os.name(), Contants.OS_AND);
        headMap.put(Contants.PROTOCOL_REQ_HEAD.appver.name(), CommonUtil.appVersion);
        headMap.put(Contants.PROTOCOL_REQ_HEAD.uuid.name(), CommonUtil.uuid);
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

    public boolean isRequestVersion()
    {
        return isRequestVersion;
    }

    public void setRequestVersion(boolean isRequestVersion)
    {
        this.isRequestVersion = isRequestVersion;
    }

    public CryptoTyepEnum getEncryptoType()
    {
        return encryptoType;
    }

    public void setEncryptoType(CryptoTyepEnum encryptoType)
    {
        this.encryptoType = encryptoType;
    }

    public String getBodyString()
    {
        return body;
    }

    public void setBodyString(String bodyString)
    {
        this.body = bodyString;
    }
}
