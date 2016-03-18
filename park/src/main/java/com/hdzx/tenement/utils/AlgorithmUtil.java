package com.hdzx.tenement.utils;

import android.util.Base64;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import org.apache.commons.codec.binary.Base64;

public class AlgorithmUtil
{
	public static String SHA1(String str)
	{
		String outStr = null;

		if (str == null || str.length() == 0)
		{
			return outStr;
		}

		try
		{
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(str.getBytes());
			byte[] sha1Bytes = messageDigest.digest();
			outStr = byte2hex(sha1Bytes);
		}
		catch (NoSuchAlgorithmException nsae)
		{
			nsae.printStackTrace();
		}

		return outStr;
	}
	
	public static String MD5(String str)
	{
		String outStr = null;

		if (str == null || str.length() == 0)
		{
			return outStr;
		}

		try
		{
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(str.getBytes());
			byte[] sha1Bytes = messageDigest.digest();
			outStr = byte2hex(sha1Bytes);
		}
		catch (NoSuchAlgorithmException nsae)
		{
			nsae.printStackTrace();
		}

		return outStr;
	}

	public static String bytetoString(byte[] digest)
	{
		String str = "";
		String tempStr = "";

		for (int i = 1; i < digest.length; i++)
		{
			tempStr = (Integer.toHexString(digest[i] & 0xff));
			if (tempStr.length() == 1)
			{
				str = str + "0" + tempStr;
			}
			else
			{
				str = str + tempStr;
			}
		}
		return str.toLowerCase();
	}

	public static String byte2hex(byte[] b) // 二行制转字符串
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++)
		{
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
			{
				hs = hs + "0" + stmp;
			}
			else
			{
				hs = hs + stmp;
			}
		}
		return hs.toLowerCase();
	}

	public static String signRequest(String appid, Object src, String appkey)
	        throws Exception
	{
		Gson gson = new Gson();
		String srcText = gson.toJson(src);
		
		// 对报文进行BASE64编码，避免中文处理问题
		String base64Text = new String(Base64.encode((appid + srcText).getBytes("utf-8"), Base64.NO_WRAP));
//		String base64Text = new String(Base64.encode((appid + srcText).getBytes("GBK"), Base64.NO_WRAP));
//		String base64Text = new String(Base64.encodeBase64(
//		        (appid + srcText).getBytes("utf-8"), false));
		// MD5摘要，生成固定长度字符串用于加密
		String destText = MD5(base64Text);
		AlgorithmData data = new AlgorithmData();
		data.setDataMing(destText);
		data.setKey(appkey);
		// 3DES加密
		Algorithm3DES.encryptMode(data);
		return data.getDataMi();
	}
	
	public static String getUTF8StringByBase64String(String base64String)
	{
		String retString = null;
		if (base64String != null)
		{
			byte[] contentBytes = Base64.decode(base64String, Base64.NO_WRAP);
			try 
			{
				retString = new String(contentBytes, "utf-8");
			} catch (UnsupportedEncodingException e) 
			{
				e.printStackTrace();
			}
		}
		
		return retString;
		
	}
	
	/**
     * 获取密码强度
     */
    public static int getPwdStrength(String pwd)
    {
//        int res_score = 0;//最后得分
        int pwd_num =0;//数字数
        int pwd_letter = 0;//大写字母
        int pwd_special_char = 0;//小写字母
        int len = pwd.length();
        for (int i=0;i<len;i++){
           int elestr = (int) pwd.charAt(i);
              if (elestr>=48 && elestr <=57){ //数字
                   pwd_num++;
              }else if ((elestr>=65 && elestr <=90)||(elestr>=97 && elestr <=122)){ //大写字母
                     pwd_letter++;
              }else{ //特殊字符
                     pwd_special_char++;
              }
         }
        if((pwd_num>0 && pwd_letter==0 && pwd_special_char==0) || (pwd_letter>0 && pwd_num==0 && pwd_special_char==0) || (pwd_special_char>0 && pwd_num==0 && pwd_letter==0)){
            return 1;
        }else if(pwd_num>0 && pwd_letter>0 && pwd_special_char>0){
            return 3;
        }else if((pwd_num>0 && pwd_letter>0 && pwd_special_char==0) || (pwd_letter>0 && pwd_num==0 && pwd_special_char>0) || (pwd_special_char>0 && pwd_num>0 && pwd_letter==0)){
            return 2;
        }else{
            return 0;
        }   
    }
}
