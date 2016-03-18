package com.hdzx.tenement.tcp.protocol;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.tcp.TcpContants;
import com.hdzx.tenement.utils.AESUtils;
import com.hdzx.tenement.utils.Base64;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.utils.RSAUtils;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.Map;

/**
 * 
 * @author Jesley
 *
 */
public class TcpReceiveTamplate implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final String TAG = TcpReceiveTamplate.class.getSimpleName();

    private Map<String, String> head = null;

    private Object data = null;

    private String errorMsg = null;

    private String innerErrorMsg = null;
    
    private String requestCode = null;
    
    private Object userData = null;
    
    //测试专用，正式版删除
    private String text = null;
    
    private CryptoTyepEnum cryptoType = CryptoTyepEnum.aes;

    public void parseContent4Json(String jsonText)
    {
        text = jsonText;
        try
        {
            Gson gson = new Gson();
            Map JsonObject = gson.fromJson(jsonText, Map.class);

            if (JsonObject != null)
            {
                head = (Map<String, String>) JsonObject.get(TcpContants.PROTOCOL_CONTEN_KEY.head.name());
                if (head == null)
                {
                    Log.i(TAG, jsonText);
                    errorMsg = "数据异常，请稍后尝试。";
                    innerErrorMsg = "Json语法解析后head值为空。";
                    return;
                }

                // 取得body加密字符串
                String bodyEncodeString = (String) JsonObject.get(TcpContants.PROTOCOL_CONTEN_KEY.body.name());
                if ("".equals(bodyEncodeString) || bodyEncodeString == null)
                {
                    Log.i(TAG, jsonText);
                    errorMsg = "数据异常，请稍后尝试。";
                    innerErrorMsg = "Json语法解析后body值为空。";
                    return;
                }
                // body解密字符串
                String bodyDecodeString = null;
                if (cryptoType == CryptoTyepEnum.rsa)
                {
                    PublicKey publicKey = RSAUtils.loadPublicKey(Contants.rsaPublicKey);
                    bodyDecodeString = new String(RSAUtils.decryptDataByPublicKey(Base64.decode(bodyEncodeString), publicKey), "UTF-8");
                }
                else if (cryptoType == CryptoTyepEnum.aes)
                {
                    bodyDecodeString = AESUtils.decrypt(UserSession.getInstance().getAesKey(), bodyEncodeString);
                }
                else
                {
                    bodyDecodeString = bodyEncodeString;
                }
                data = gson.fromJson(bodyDecodeString, Map.class).get("data");
                System.out.println("TcpReceiveTamplate:body=" + bodyDecodeString);
            }
            else
            {
                Log.i(TAG, jsonText);
                errorMsg = "数据异常，请稍后尝试。";
                innerErrorMsg = "Json语法解析后值为空。";
            }
        }
        catch (JsonSyntaxException e)
        {
            Log.i(TAG, jsonText);
            errorMsg = "数据异常，请稍后尝试。";
            innerErrorMsg = "Json语法解析错误。";
            e.printStackTrace();
        }
        catch (Exception e)
        {
            Log.i(TAG, jsonText);
            errorMsg = "未知异常发生，请稍后尝试。";
            innerErrorMsg = "未知异常发生";
            e.printStackTrace();
        }
    }

    public String getText()
    {
        return text;
    }

    public Map<String, String> getHead()
    {
        return head;
    }

    public Object getData()
    {
        return data;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void SetErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }


    public String getCode()
    {
        return head.get(TcpContants.RESPONSE_TERMINAL_HEAD.code.name());
    }

    public String getMessage()
    {
        return head.get(TcpContants.RESPONSE_TERMINAL_HEAD.msg.name());
    }
    
    public String getType()
    {
        String type = null;
        if (head != null)
        {
            type = head.get(TcpContants.RESPONSE_TERMINAL_HEAD.type.name());
        }
        return type;
    }
    
    public String getSeq()
    {
        String seq = null;
        if (head != null)
        {
            seq = head.get(TcpContants.RESPONSE_TERMINAL_HEAD.seq.name());
        }
        return seq;
    }
    
    public boolean isSend()
    {
        String type = getType();
        if ("0".equals(type))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean isAnsewer()
    {
        return !isSend();
    }

    public Object getInMapHead(String key)
    {
        Object object = null;
        if (head != null && (head instanceof Map))
        {
            object = ((Map) head).get(key);
        }

        return object;
    }

    public Object getInMapData(String key)
    {
        Object object = null;
        if (data != null && (data instanceof Map))
        {
            object = ((Map) data).get(key);
        }

        return object;
    }

    public String getInnerErrorMsg()
    {
        return innerErrorMsg;
    }

    public void setInnerErrorMsg(String innerErrorMsg)
    {
        this.innerErrorMsg = innerErrorMsg;
    }

    public CryptoTyepEnum getCryptoType()
    {
        return cryptoType;
    }

    public void setCryptoType(CryptoTyepEnum cryptoType)
    {
        this.cryptoType = cryptoType;
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
