package com.hdzx.tenement.http.protocol;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.hdzx.tenement.common.deamon.AppDeamon;
import com.hdzx.tenement.http.CustomAsyncHttpClient;
import com.hdzx.tenement.http.CustomAsyncHttpResponseHandler;
import com.hdzx.tenement.utils.NetUtils;
import com.loopj.android.http.RequestHandle;

/**
 * 
 * @author Jesley
 * 
 */
//public class HttpAsyncTask
//        extends
//        AsyncTask<HttpRequestEntity, Map<String, Object>, ResponseContentTamplate>
//{
//	private Context context = null;
//
//	private IContentReportor contentReportor = null;
//
//	public HttpAsyncTask(Context context, IContentReportor contentReportor)
//	{
//		this.context = context;
//		this.contentReportor = contentReportor;
//	}
//
//	@Override
//	protected ResponseContentTamplate doInBackground(HttpRequestEntity... args)
//	{
//		ResponseContentTamplate responseContentTamplate = null;
//
//		if (args != null && args.length > 0)
//		{
//			HttpRequestEntity httpRequestEntity = args[0];
//			if (httpRequestEntity != null)
//			{
//				String responseContent = HttpUtil.requestPostJson(
//				        httpRequestEntity.getRequestUrl(),
//				        httpRequestEntity.getContent(),
//				        httpRequestEntity.getRequestEncoding(),
//				        httpRequestEntity.getResponseEncoding(),
//				        httpRequestEntity.getHttpHeader());
//				
//				System.out.println("http=response message=" + responseContent);
//
//				responseContentTamplate = new ResponseContentTamplate();
//				responseContentTamplate.setResponseCode(httpRequestEntity
//				        .getRequestCode());
//				responseContentTamplate.setData(httpRequestEntity.getData());
//
//				if (responseContent != null)
//				{
//					responseContentTamplate.parseContent4Json(responseContent);
//				}
//				else
//				{
//					responseContentTamplate.SetErrorMsg("服务器繁忙，请稍后尝试。");
//				}
//			}
//		}
//
//		return responseContentTamplate;
//	}
//
//	public boolean distroy(boolean mayInterruptIfRunning)
//	{
//		context = null;
//		contentReportor = null;
//
//		return super.cancel(mayInterruptIfRunning);
//	}
//
//	@Override
//	protected void onPostExecute(ResponseContentTamplate result)
//	{
//		if (contentReportor != null)
//		{
//			contentReportor.reportBackContent(result);
//		}
//	}


//------------------------------------------------------------------------------------------------------------

/**
 * That is said that AsyncHttpClient is a good framework in internet.
 * But it is realized by apach's HttpClient.
 * I use it because of it provide a thread pool for each http request, and allow you customize your provided thread pool.
 * I found its encapsulation better for apach HttpClient after used it, but has no customiztion for me.
 * So I realized some classes for my custom requirement.
 * 
 */

/**
 * 
 * @author Jesley
 *
 */
public class HttpAsyncTask
{
	private Context context = null;

	private IContentReportor contentReportor = null;
	
	private RequestHandle requestHandle = null;
	
	private HttpRequestEntity entity = null;
	
	private boolean isDestroy = false;
	
	private ProgressDialog progressDialog = null;

	public HttpAsyncTask(Context context, IContentReportor contentReportor)
	{
		this.context = context;
		this.contentReportor = contentReportor;
	}

	public boolean distroy(boolean mayInterruptIfRunning)
	{
		boolean isResult = false;
		if (requestHandle != null)
		{
			isResult = requestHandle.cancel(true);
		}
		
		context = null;
		contentReportor = null;
		requestHandle = null;
		isDestroy = true;
		return isResult;
	}
	
	public boolean isDestroy()
    {
        return isDestroy;
    }

    public HttpRequestEntity getEntity()
    {
        return entity;
    }

    public IContentReportor getContentReportor()
    {
        return contentReportor;
    }

    public void execute(HttpRequestEntity entity)
	{
        if (!NetUtils.isNetworkAvailable(context))
        {
            if (contentReportor != null)
            {
                ResponseContentTamplate responseContent = new ResponseContentTamplate();
                if (entity != null)
                {
                    responseContent.setResponseCode(entity.getRequestCode());
                    responseContent.setUserData(entity.getUserData());
                    responseContent.setDecryptoType(entity.getResponseDecryptoType());
                }
                responseContent.SetErrorMsg("网络不可用，请确认网络。");
                contentReportor.reportBackContent(responseContent);
            }
        }
        else if (entity != null)
		{
		    this.entity = entity;
		    if (!AppDeamon.getInstance().requestIntercepter(this))
		    {
		    	showProgressDialog("正在加载···");
		    	
		        requestHandle = CustomAsyncHttpClient.getInstance().post(entity.getRequestUrl(), entity.getRequestParams(), new CustomAsyncHttpResponseHandler(this, entity, contentReportor,progressDialog));
		        //requestHandle = CustomAsyncHttpClient.getInstance().post(entity.getRequestUrl(), new CustomAsyncHttpResponseHandler(this, entity, contentReportor));
		    }
		}
	}
    
    
    private void showProgressDialog(String message) {
		// 创建ProgressDialog对象
		progressDialog = new ProgressDialog(context);
		// 设置进度条风格，风格为圆形，旋转的
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog提示信息
		progressDialog.setMessage(message);

		// 设置ProgressDialog标题图标
		// pDialog.setIcon(R.drawable.img1);

		// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
		progressDialog.setIndeterminate(false);

		// 设置ProgressDialog 是否可以按退回键取消
		progressDialog.setCancelable(false);

		// 让ProgressDialog显示
		progressDialog.show();
	}
}
