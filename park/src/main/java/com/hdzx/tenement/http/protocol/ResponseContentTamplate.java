package com.hdzx.tenement.http.protocol;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.utils.AESUtils;
import com.hdzx.tenement.utils.Base64;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.utils.RSAUtils;

import java.security.PublicKey;
import java.util.Map;

public class ResponseContentTamplate
{
    private final String TAG = ResponseContentTamplate.class.getSimpleName();

    private Object head = null;

    private Object data = null;
    
    private String dataJson = null;

    private String errorMsg = null;

    private String innerErrorMsg = null;

    private String innerErrorCode = null;

    private String responseCode = null;

    private Object userData = null;
    
    private boolean hasData = true;
    
    private CryptoTyepEnum decryptoType = CryptoTyepEnum.aes;

    public void parseContent4Json(String jsonText)
    {
        try
        {
            Gson gson = new Gson();
            Map JsonObject = gson.fromJson(jsonText, Map.class);

            if (JsonObject != null)
            {
                head = JsonObject.get(Contants.PROTOCOL_CONTEN_KEY.head.name());
                if (head == null)
                {
                    Log.i(TAG, jsonText);
                    errorMsg = "数据异常，请稍后尝试。";
                    innerErrorMsg = "Json语法解析后head值为空。";
                    return;
                }

                // 取得body加密字符串
                String bodyEncodeString = (String) JsonObject.get(Contants.PROTOCOL_CONTEN_KEY.body.name());
                if ("".equals(bodyEncodeString) || bodyEncodeString == null)
                {
                    Log.i(TAG, jsonText);
                    errorMsg = "数据异常，请稍后尝试。";
                    innerErrorMsg = "Json语法解析后body值为空。";
                    return;
                }
                // body解密字符串
                String bodyDecodeString = null;
                if (decryptoType == CryptoTyepEnum.rsa)
                {
                    PublicKey publicKey = RSAUtils.loadPublicKey(Contants.rsaPublicKey);
                    bodyDecodeString = new String(RSAUtils.decryptDataByPublicKey(Base64.decode(bodyEncodeString), publicKey), "UTF-8");
                }
                else if (decryptoType == CryptoTyepEnum.aes)
                {
                    // decodeFlag=1 -> AES解密
                    bodyDecodeString = AESUtils.decrypt(UserSession.getInstance().getAesKey(), bodyEncodeString);
                }
                else
                {
                    bodyDecodeString = new String(Base64.decode(bodyEncodeString.getBytes("UTF-8")), "UTF-8");
                }
                
                if (hasData)
                {
                    data = gson.fromJson(bodyDecodeString, Map.class).get("data");
                }
                else
                {
                    data = gson.fromJson(bodyDecodeString, Object.class);
                }
                
                int startIndex = bodyDecodeString.indexOf(":");
                int endIndex = bodyDecodeString.indexOf("}");
                if (startIndex < endIndex)
                {
                    dataJson = bodyDecodeString.substring(startIndex - 1, endIndex).trim();
                }
                
                Log.v("test-bodyDecodeString", bodyDecodeString);
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public Object getHead()
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

    public String getResponseCode()
    {
        return responseCode;
    }

    public void setResponseCode(String responseCode)
    {
        this.responseCode = responseCode;
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

    public Object getUserData()
    {
        return userData;
    }

    public void setUserData(Object userData)
    {
        this.userData = userData;
    }

    public String getInnerErrorMsg()
    {
        return innerErrorMsg;
    }

    public void setInnerErrorMsg(String innerErrorMsg)
    {
        this.innerErrorMsg = innerErrorMsg;
    }

    public CryptoTyepEnum getDecryptoType()
    {
        return decryptoType;
    }

    public void setDecryptoType(CryptoTyepEnum decryptoType)
    {
        this.decryptoType = decryptoType;
    }

    public String getInnerErrorCode()
    {
        return innerErrorCode;
    }

    public void setInnerErrorCode(String innerErrorCode)
    {
        this.innerErrorCode = innerErrorCode;
    }

    public String getDataJson()
    {
        String dataJson = null;
        if (data != null)
        {
            Gson gson = new Gson();
            dataJson = gson.toJson(data);
        }
        
        return dataJson;
    }

    public boolean isHasData()
    {
        return hasData;
    }

    public void setHasData(boolean hasData)
    {
        this.hasData = hasData;
    }
}
