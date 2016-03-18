/**
 * 
 */
package com.hdzx.tenement.http;

import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 
 * @author Jesley
 * 
 */
public class HttpParamEncodeEntity extends StringEntity
{

	public HttpParamEncodeEntity(List<NameValuePair> formParams, String charset)
	        throws UnsupportedEncodingException
	{
		super(ParamEncodeUtil.format(formParams, charset));
	}

	private static class ParamEncodeUtil
	{

		static String format(List<NameValuePair> formParams,
		        String requestCharacter)
		{
			StringBuffer sb = new StringBuffer();
			for (NameValuePair nv : formParams)
			{
				if (sb.length() > 0)
				{
					sb.append("&");
				}
				sb.append(encodeString(nv.getName(), requestCharacter));
				sb.append("=");
				sb.append(encodeString(nv.getValue(), requestCharacter));
			}

			return sb.toString();
		}

		private static String encodeString(String str, String requestCharacter)
		{
			try
			{
				return URLEncoder.encode(str, requestCharacter);
			}
			catch (UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}

	}

}
