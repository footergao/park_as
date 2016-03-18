package com.hdzx.tenement.utils;

import com.hdzx.tenement.http.EasySSLSocketFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * HTTP实用类
 * 
 * @author Jesley
 * 
 */
public class HttpUtil
{
	public static final String CHARACTER_ENCODING = "UTF-8";
	public static final String PATH_SIGN = "/";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	public static final String CONTENT_TYPE = "Content-Type";

	public static final int CONNECTION_TIMEOUT = 10000;
	public static final int SO_TIMEOUT = 30000;
	public static final int SO_TIMEOUT_60S = 60000;

	public static final int BUFFER = 1024;

	/**
	 * HttpResponse包装类
	 * 
	 * @author Jesley
	 * 
	 */
	public static class HttpResponseWrapper
	{
		private HttpResponse httpResponse;
		private HttpClient httpClient;

		public HttpResponseWrapper(HttpClient httpClient,
		        HttpResponse httpResponse)
		{
			this.httpClient = httpClient;
			this.httpResponse = httpResponse;
		}

		public HttpResponseWrapper(HttpClient httpClient)
		{
			this.httpClient = httpClient;
		}

		public HttpResponse getHttpResponse()
		{
			return httpResponse;
		}

		public void setHttpResponse(HttpResponse httpResponse)
		{
			this.httpResponse = httpResponse;
		}

		/**
		 * 获得流类型的响应
		 */
		public InputStream getResponseStream() throws IllegalStateException,
		        IOException
		{
			return httpResponse.getEntity().getContent();
		}

		/**
		 * 获得字符串类型的响应
		 */
		public String getResponseString(String responseCharacter)
		        throws ParseException, IOException
		{
			HttpEntity entity = getEntity();
			String responseStr = EntityUtils
			        .toString(entity, responseCharacter);
			if (entity.getContentType() == null)
			{
				responseStr = new String(responseStr.getBytes("iso-8859-1"),
				        responseCharacter);
			}
			// EntityUtils.consume(entity);
			return responseStr;
		}

		public String getResponseString() throws ParseException, IOException
		{
			return getResponseString(CHARACTER_ENCODING);
		}

		/**
		 * 获得响应状态码
		 */
		public int getStatusCode()
		{
			return httpResponse.getStatusLine().getStatusCode();
		}

		/**
		 * 获得响应状态码并释放资源
		 */
		public int getStatusCodeAndClose()
		{
			close();
			return getStatusCode();
		}

		public HttpEntity getEntity()
		{
			return httpResponse.getEntity();
		}

		/**
		 * 释放资源
		 */
		public void close()
		{
			httpClient.getConnectionManager().shutdown();
		}
	}

	/**
	 * POST方式提交上传文件请求
	 */
	public static HttpResponseWrapper requestPostUpload(String url,
	        Map<String, File> requestFiles, Map<String, String> requestParas,
	        String requestCharacter) throws ClientProtocolException,
	        IOException
	{
		HttpClient client = null;
		if (url.startsWith("https"))
		{
			client = createHttpsClient();
		}
		else
		{
			client = createHttpClient();
		}
		HttpPost httpPost = new HttpPost(url);
		MultipartEntity multipartEntity = new MultipartEntity(
		        HttpMultipartMode.BROWSER_COMPATIBLE, null,
		        Charset.forName(requestCharacter));
		if (requestFiles == null || requestFiles.size() == 0)
		{
			return null;
		}
		for (Entry<String, File> entry : requestFiles.entrySet())
		{
			multipartEntity.addPart(entry.getKey(),
			        new FileBody(entry.getValue(), "application/octet-stream",
			                requestCharacter));
		}
		if (requestParas != null && requestParas.size() > 0)
		{
			// 对key进行排序
			List<String> keys = new ArrayList<String>(requestParas.keySet());
			Collections.sort(keys);
			for (String key : keys)
			{
				multipartEntity.addPart(
				        key,
				        new StringBody(requestParas.get(key), Charset
				                .forName(requestCharacter)));
			}
		}
		httpPost.setEntity(multipartEntity);
		HttpResponse httpResponse = client.execute(httpPost);
		return new HttpResponseWrapper(client, httpResponse);
	}

	/**
	 * POST方式提交上传文件请求
	 */
	public static HttpResponseWrapper requestPostUpload(String url,
	        Map<String, File> requestFiles, Map<String, String> requestParas)
	        throws ClientProtocolException, IOException
	{
		return requestPostUpload(url, requestFiles, requestParas,
		        CHARACTER_ENCODING);
	}

	/**
	 * POST方式提交表单数据，返回响应对象
	 */
	public static HttpResponseWrapper requestPostFormResponse(String url,
	        Map<String, String> requestParas, String requestCharacter)
	        throws ClientProtocolException, IOException
	{
		HttpClient client = null;
		if (url.startsWith("https"))
		{
			client = createHttpsClient();
		}
		else
		{
			client = createHttpClient();
		}
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> formParams = initNameValuePair(requestParas);
		httpPost.setEntity(new UrlEncodedFormEntity(formParams,
		        requestCharacter));
		HttpResponse httpResponse = client.execute(httpPost); // 执行POST请求
		return new HttpResponseWrapper(client, httpResponse);
	}

	/**
	 * 自定义操作表单提交的数据
	 * 
	 * @return
	 * @throws java.io.IOException
	 * @throws org.apache.http.client.ClientProtocolException
	 */
	public static HttpResponseWrapper requestPostFormResponse2(String url,
	        Map<String, String> requestParas, int timeOut)
	        throws ClientProtocolException, IOException
	{
		HttpClient client = null;
		if (url.startsWith("https"))
		{
			client = createHttpsClient(CONNECTION_TIMEOUT, timeOut * 1000);
		}
		else
		{
			client = createHttpClient(CONNECTION_TIMEOUT, timeOut * 1000);
		}
		// client.getParams().setParameter("Accept-Encoding", "gzip, deflate");
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> formParams = initNameValuePair(requestParas);
		httpPost.setEntity(new HttpParamEncodeEntity(formParams,
		        CHARACTER_ENCODING));
		HttpResponse httpResponse = client.execute(httpPost); // 执行POST请求
		return new HttpResponseWrapper(client, httpResponse);
	}

	/**
	 * POST方式提交表单数据，返回响应对象，utf8编码
	 */
	public static HttpResponseWrapper requestPostFormResponse(String url,
	        Map<String, String> requestParas) throws ClientProtocolException,
	        IOException
	{
		return requestPostFormResponse(url, requestParas, CHARACTER_ENCODING);
	}

	/**
	 * POST方式提交表单数据，不会自动重定向
	 */
	public static String requestPostForm(String url,
	        Map<String, String> requestParas, String requestCharacter,
	        String responseCharacter)
	{
		HttpResponseWrapper httpResponseWrapper = null;
		try
		{
			httpResponseWrapper = requestPostFormResponse(url, requestParas,
			        requestCharacter);
			return httpResponseWrapper.getResponseString(responseCharacter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			httpResponseWrapper.close();
		}
		return null;
		// HttpClient client = null;
		// String responseStr = null;
		// try{
		// if (url.startsWith("https")) {
		// client =createHttpsClient();
		// }else{
		// client =createHttpClient();
		// }
		// HttpPost httpPost = new HttpPost(url);
		// List<NameValuePair> formParams = initNameValuePair(requestParas);
		// httpPost.setEntity(new UrlEncodedFormEntity(formParams,
		// requestCharacter));
		// HttpResponse httpResponse = client.execute(httpPost); //执行POST请求
		// int statusCode = httpResponse.getStatusLine().getStatusCode();
		// if (isRedirectStatus(statusCode)) {
		// httpPost.abort();
		// Header locationHeader = httpResponse.getFirstHeader("location");
		// if (locationHeader != null) {
		// String location = locationHeader.getValue();
		// LOG.info("redirect url=" + location);
		// HttpGet httpGet = new HttpGet(location);
		// HttpResponse response = client.execute(httpGet);
		// HttpEntity entity = response.getEntity();
		// if (entity!=null) {
		// responseStr = EntityUtils.toString(entity, responseCharacter);
		// EntityUtils.consume(entity);
		// }
		// }
		// } else {
		// HttpEntity entity = httpResponse.getEntity(); //获取响应实体
		// if (entity!=null) {
		// responseStr = EntityUtils.toString(entity, responseCharacter);
		// EntityUtils.consume(entity);
		// }
		// }
		// }catch(IOException e) {
		// LOG.error(e.getMessage());
		// e.printStackTrace();
		// }finally {
		// client.getConnectionManager().shutdown();
		// }
		// return responseStr;
	}

	/**
	 * POST方式提交表单数据，不会自动重定向
	 */
	public static String requestPostForm(String url,
	        Map<String, String> requestParas)
	{
		return requestPostForm(url, requestParas, CHARACTER_ENCODING,
		        CHARACTER_ENCODING);
	}

	// private static boolean isRedirectStatus(int statuscode) {
	// return (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) ||
	// (statuscode == HttpStatus.SC_MOVED_TEMPORARILY) ||
	// (statuscode == HttpStatus.SC_SEE_OTHER) ||
	// (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT);
	// }

	public static HttpResponseWrapper requestGetResponse(String url)
	        throws ClientProtocolException, IOException
	{
		HttpClient client = null;
		if (url.startsWith("https"))
		{
			client = createHttpsClient();
		}
		else
		{
			client = createHttpClient();
		}
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = client.execute(httpGet);
		return new HttpResponseWrapper(client, httpResponse);
	}

	public static HttpResponseWrapper requestGetResponse(String url,
	        String xForWardedFor) throws ClientProtocolException, IOException
	{
		HttpClient client = null;
		if (url.startsWith("https"))
		{
			client = createHttpsClient();
		}
		else
		{
			client = createHttpClient();
		}
		HttpGet httpGet = new HttpGet(url);
		if (xForWardedFor != null && xForWardedFor.length() > 0)
		{
			httpGet.addHeader("X-Forwarded-For", xForWardedFor);
		}
		HttpResponse httpResponse = client.execute(httpGet);
		return new HttpResponseWrapper(client, httpResponse);
	}

	/**
	 * GET方式提交URL请求，会自动重定向
	 */
	public static String requestGet(String url, String responseCharacter,
	        String xForWardedFor)
	{
		HttpResponseWrapper httpResponseWrapper = null;
		try
		{
			httpResponseWrapper = requestGetResponse(url, xForWardedFor);
			return httpResponseWrapper.getResponseString(responseCharacter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			httpResponseWrapper.close();
		}
		return null;
	}

	/**
	 * GET方式提交URL请求，会自动重定向
	 */
	public static String requestGet(String url)
	{
		return requestGet(url, CHARACTER_ENCODING, null);
	}

	/**
	 * get请求
	 * 
	 * @param url
	 *            请求url(eg:https://api.lvmama.com/user/getUserInfo)
	 * @param params
	 *            参数
	 * @return 响应信息
	 */
	public static String requestGet(String url, Map<String, Object> params)
	{
		StringBuilder urlBuilder = new StringBuilder(url);
		urlBuilder.append("?").append(mapToUrl(params));
		return requestGet(urlBuilder.toString(), CHARACTER_ENCODING, null);
	}

	/**
	 * 将Map中的数据组装成url
	 * 
	 * @param params
	 * @return
	 */
	public static String mapToUrl(Map<String, Object> params)
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			boolean isFirst = true;
			for (String key : params.keySet())
			{
				String value = (String) params.get(key);
				if (isFirst)
				{
					sb.append(key + "=" + URLEncoder.encode(value, "utf-8"));
					isFirst = false;
				}
				else
				{
					if (value != null)
					{
						sb.append("&" + key + "="
						        + URLEncoder.encode(value, "utf-8"));
					}
					else
					{
						sb.append("&" + key + "=");
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * GET方式提交URL请求，会自动重定向
	 */
	public static String proxyRequestGet(String url, String xForWardedFor)
	{
		return requestGet(url, CHARACTER_ENCODING, xForWardedFor);
	}

	/**
	 * POST方式提交非表单数据，返回响应对象
	 */
	public static HttpResponseWrapper requestPostData(String url, String data,
	        String contentType, String requestCharacter, int connectionTimeout,
	        int soTimeout, Map<String, String> httpHeader) throws ClientProtocolException, IOException
	{
		HttpClient client = null;
		if (url.startsWith("https"))
		{
			client = createHttpsClient(connectionTimeout, soTimeout);
		}
		else
		{
			client = createHttpClient(connectionTimeout, soTimeout);
		}
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader(CONTENT_TYPE, contentType);
		if (httpHeader != null && httpHeader.entrySet() != null)
		{
			for (Entry<String, String> entry : httpHeader.entrySet())
			{
				if (entry != null)
				{
					httpPost.addHeader(entry.getKey(), entry.getValue());
					
//					Header[] heanders = httpPost.getHeaders(entry.getKey());
//					if (heanders == null || heanders.length == 0)
//					{
//						httpPost.setHeader(entry.getKey(), entry.getValue());
//					}
//					else
//					{
//						httpPost.addHeader(entry.getKey(), entry.getValue());
//					}
				}
			}
		}
		httpPost.setEntity(new StringEntity(data, requestCharacter));
		HttpResponse httpResponse = client.execute(httpPost);
		return new HttpResponseWrapper(client, httpResponse);
	}

	/**
	 * POST方式提交非表单数据，返回响应对象
	 */
	public static HttpResponseWrapper requestPostData(String url, String data,
	        String contentType, String requestCharacter, Map<String, String> httpHeader)
	        throws ClientProtocolException, IOException
	{
		return requestPostData(url, data, contentType, requestCharacter,
		        CONNECTION_TIMEOUT, SO_TIMEOUT, httpHeader);
	}

	/**
	 * POST非表单方式提交XML数据
	 */
	public static String requestPostXml(String url, String xmlData,
	        String requestCharacter, String responseCharacter)
	{
		HttpResponseWrapper httpResponseWrapper = null;
		try
		{
			String contentType = "text/xml; charset=" + requestCharacter;
			httpResponseWrapper = requestPostData(url, xmlData, contentType,
			        requestCharacter, null);
			return httpResponseWrapper.getResponseString(responseCharacter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			httpResponseWrapper.close();
		}
		return null;
	}
	
	/**
	 * Post json date to http server
	 * 
	 * @param url
	 * @param jsonData
	 * @param requestCharacter
	 * @param responseCharacter
	 * @return
	 */
	public static String requestPostJson(String url, String jsonData,
	        String requestCharacter, String responseCharacter, Map<String, String> httpHeader)
	{
		System.out.println("request,json=" + jsonData + " , url=" + url);
		HttpResponseWrapper httpResponseWrapper = null;
		try
		{
			String contentType = "text/json; charset=" + requestCharacter;
			httpResponseWrapper = requestPostData(url, jsonData, contentType,
			        requestCharacter, httpHeader);
			return httpResponseWrapper.getResponseString(responseCharacter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (httpResponseWrapper != null)
			{
				httpResponseWrapper.close();
			}
		}
		
		return null;
	}

	/**
	 * POST非表单方式提交XML数据
	 */
	public static String requestPostXml(String url, String xmlData)
	{
		return requestPostXml(url, xmlData, CHARACTER_ENCODING,
		        CHARACTER_ENCODING);
	}

	public static HttpClient createHttpClient(int connectionTimeout,
	        int soTimeout)
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
		HttpConnectionParams.setSoTimeout(params, soTimeout);
		return httpClient;
	}

	public static HttpClient createHttpClient()
	{
		return createHttpClient(CONNECTION_TIMEOUT, SO_TIMEOUT);
	}

	public static HttpClient createHttpsClient(int connectionTimeout,
	        int soTimeout)
	{
		try
		{
			HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
			HttpParams params = httpClient.getParams();
			HttpConnectionParams
			        .setConnectionTimeout(params, connectionTimeout);
			HttpConnectionParams.setSoTimeout(params, soTimeout);

			SchemeRegistry schemeRegistry = httpClient.getConnectionManager()
			        .getSchemeRegistry();
			schemeRegistry.register(new Scheme("https",
			        new EasySSLSocketFactory(), 443));
			schemeRegistry.register(new Scheme("https",
			        new EasySSLSocketFactory(), 8443));
			return httpClient;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static HttpClient createHttpsClient()
	{
		return createHttpsClient(CONNECTION_TIMEOUT, SO_TIMEOUT);
	}

	public static List<NameValuePair> initNameValuePair(
	        Map<String, String> params)
	{
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		if (params != null && params.size() > 0)
		{
			// 对key进行排序
			List<String> keys = new ArrayList<String>(params.keySet());
			Collections.sort(keys);
			for (String key : keys)
			{
				// LOG.info(key+" = " +params.get(key));
				formParams.add(new BasicNameValuePair(key, params.get(key)));
			}
		}
		return formParams;
	}

	/**
	 * 通过httpclient下载的文件直接转为字节数组
	 * 
	 * @param path
	 * @author yanzhirong
	 * @return
	 */
	public static byte[] getHttpClientResponseByteArray(String path)
	{
		HttpClient client = new DefaultHttpClient();
		HttpUriRequest httpGet = new HttpGet(path);

		byte[] byteArrays = new byte[0];
		try
		{
			HttpResponse httpResponse = client.execute(httpGet);
			InputStream in = httpResponse.getEntity().getContent();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			byte[] b = new byte[BUFFER];
			int len = 0;
			while ((len = in.read(b)) != -1)
			{
				bos.write(b, 0, len);
			}
			byteArrays = bos.toByteArray();
			in.close();
			bos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{

		}
		return byteArrays;
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier
	{
		public boolean verify(String hostname, SSLSession session)
		{
			return true;
		}
	}

	private static class TrustAnyTrustManager implements X509TrustManager
	{

		public void checkClientTrusted(X509Certificate[] chain, String authType)
		        throws CertificateException
		{
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
		        throws CertificateException
		{
		}

		public X509Certificate[] getAcceptedIssuers()
		{
			return new X509Certificate[] {};
		}
	}
}
