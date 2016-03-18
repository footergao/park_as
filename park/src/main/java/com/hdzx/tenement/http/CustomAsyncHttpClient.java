package com.hdzx.tenement.http;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.ResponseHandlerInterface;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

public class CustomAsyncHttpClient extends AsyncHttpClient
{
	private static CustomAsyncHttpClient asyncHttpClient = new CustomAsyncHttpClient();
	
	public static CustomAsyncHttpClient getInstance()
	{
		return asyncHttpClient;
	}
	
	protected AsyncHttpRequest newAsyncHttpRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, ResponseHandlerInterface responseHandler,
	        Context context)
	{
		return new CustomAsyncHttpRequest(client, httpContext, uriRequest, responseHandler);
	}
}
