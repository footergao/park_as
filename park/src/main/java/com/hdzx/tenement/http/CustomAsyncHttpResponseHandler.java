package com.hdzx.tenement.http;

import android.app.ProgressDialog;
import android.util.Log;

import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.common.deamon.AppDeamon;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.utils.Contants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.cordova.LOG;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.util.ByteArrayBuffer;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Jesley
 * 
 */
public class CustomAsyncHttpResponseHandler extends AsyncHttpResponseHandler
{
    private HttpAsyncTask task = null;
    
	private HttpRequestEntity entity = null;
			
	private IContentReportor contentReportor = null;
	
	private ResponseContentTamplate tamplate = new ResponseContentTamplate();
	private ProgressDialog progressDialog = null;

	public CustomAsyncHttpResponseHandler(HttpAsyncTask task, HttpRequestEntity entity, IContentReportor contentReportor, ProgressDialog progressDialog)
    {
	    super();
	    this.task = task;
	    this.entity = entity;
	    this.contentReportor = contentReportor;
	    this.progressDialog = progressDialog;
	    
	    init();
    }
	
	public void setEntity(HttpRequestEntity entity)
    {
        this.entity = entity;
    }

    private void init()
	{
		setCharset(entity.getResponseEncoding());
		tamplate.setResponseCode(entity.getRequestCode());
		tamplate.setUserData(entity.getUserData());
		tamplate.setDecryptoType(entity.getResponseDecryptoType());
		tamplate.setHasData(entity.isHasData());
	}

	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
	{
		 closeProgressDialog();
		if (contentReportor != null)
		{
			if (tamplate.getErrorMsg() == null)
			{
				tamplate.SetErrorMsg("服务器繁忙，请稍后尝试。");
			}
			
			if (contentReportor != null)
			{
				 if (Contants.ResponseCode.CODE_900003.equals(tamplate.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name())))
	             {
	                 UserSession.getInstance().setValidAesKey(false);
	                 AppDeamon.getInstance().requestIntercepter(task);
	             }
	             else if (Contants.ResponseCode.CODE_900001.equals(tamplate.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name())))
	             {
	                 UserSession.getInstance().setAccessTicket(null);
	                 AppDeamon.getInstance().requestIntercepter(task);
	             }else{
	            	 contentReportor.reportBackContent(tamplate);
	             }
			}
		}
	}

	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
	{
		 closeProgressDialog();
		if (contentReportor != null)
		{
			 if (Contants.ResponseCode.CODE_900003.equals(tamplate.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name())))
             {
                 UserSession.getInstance().setValidAesKey(false);
                 AppDeamon.getInstance().requestIntercepter(task);
             }
             else if (Contants.ResponseCode.CODE_900001.equals(tamplate.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name())))
             {
                 UserSession.getInstance().setAccessTicket(null);
                 AppDeamon.getInstance().requestIntercepter(task);
             }else{
            	
            	 contentReportor.reportBackContent(tamplate);
             }
		}
	}

	@Override
	public void sendResponseMessage(HttpResponse response) throws IOException
	{
		// do not process if request has been cancelled
		if (!Thread.currentThread().isInterrupted())
		{
			StatusLine status = response.getStatusLine();
			byte[] responseBody = getResponseData(response.getEntity());
			
			if (responseBody != null && responseBody.length > 0)
			{
				try
				{
					String jsonString = new String(responseBody, getCharset());
					System.out.println("http:response=" + jsonString);
					tamplate.parseContent4Json(jsonString);
					
					if (AppDeamon.getInstance().responseIntercepter(task, tamplate))
					{
					    return;
					}
				}
				catch (Exception e)
				{
					tamplate.SetErrorMsg("数据异常，请稍后尝试。");
					e.printStackTrace();
				}
				
			}
			// additional cancellation check as getResponseData() can take
			// non-zero time to process
			if (!Thread.currentThread().isInterrupted())
			{
				if (tamplate.getErrorMsg() != null)
				{
					sendFailureMessage(status.getStatusCode(), response.getAllHeaders(), responseBody, new HttpResponseException(status.getStatusCode(), tamplate.getErrorMsg()));
				}
				else
				{
					if (status.getStatusCode() >= 300)
					{
						sendFailureMessage(status.getStatusCode(), response.getAllHeaders(), responseBody, new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()));
					}
					else
					{
						sendSuccessMessage(status.getStatusCode(), response.getAllHeaders(), responseBody);
					}
				}
				
			}
		}
	}

	private byte[] getResponseData(HttpEntity entity) throws IOException
	{
		byte[] responseBody = null;
		if (entity != null)
		{
			InputStream instream = entity.getContent();
			if (instream != null)
			{
				long contentLength = entity.getContentLength();
				if (contentLength > Integer.MAX_VALUE)
				{
					throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
				}
				int buffersize = (contentLength <= 0) ? BUFFER_SIZE : (int) contentLength;
				try
				{
					ByteArrayBuffer buffer = new ByteArrayBuffer(buffersize);
					try
					{
						byte[] tmp = new byte[BUFFER_SIZE];
						int l, count = 0;
						// do not send messages if request has been cancelled
						while ((l = instream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted())
						{
							count += l;
							buffer.append(tmp, 0, l);
							sendProgressMessage(count, (int) (contentLength <= 0 ? 1 : contentLength));
						}
					}
					finally
					{
						AsyncHttpClient.silentCloseInputStream(instream);
						AsyncHttpClient.endEntityViaReflection(entity);
					}
					responseBody = buffer.toByteArray();
				}
				catch (OutOfMemoryError e)
				{
					System.gc();
					throw new IOException("File too large to fit into available memory");
				}
			}
		}
		return responseBody;
	}

	public HttpRequestEntity getEntity()
	{
		return entity;
	}
	
	private void closeProgressDialog() {
		if (progressDialog != null&&progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
}
