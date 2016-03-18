package com.hdzx.tenement.http;

import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.ResponseHandlerInterface;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

public class CustomAsyncHttpRequest extends AsyncHttpRequest
{
    private final HttpUriRequest request;

    private HttpRequestEntity entity = null;

    public CustomAsyncHttpRequest(AbstractHttpClient client, HttpContext context, HttpUriRequest request, ResponseHandlerInterface responseHandler)
    {
        super(client, context, request, responseHandler);
        this.request = request;

        if (responseHandler != null && responseHandler instanceof CustomAsyncHttpResponseHandler)
        {
            entity = ((CustomAsyncHttpResponseHandler) responseHandler).getEntity();
        }
    }

    public void onPreProcessRequest(AsyncHttpRequest httpRequest)
    {
        if (entity != null && request instanceof HttpPost)
        {
            HttpPost httpPost = (HttpPost) request;
            HttpEntity httpEntity = httpPost.getEntity();
            StringEntity stringEntity = null;
            Map<String, String> httpHeader = entity.getHttpHeader();
            if (httpHeader != null && httpHeader.entrySet() != null)
            {
                for (Entry<String, String> entry : httpHeader.entrySet())
                {
                    if (entry != null)
                    {
                        if (httpEntity != null && "Content-Type".equals(entry.getKey()))
                        {
                            continue;
                        }
                        httpPost.addHeader(entry.getKey(), entry.getValue());
                    }
                }
            }

            if (httpEntity == null)
            {
                String jsonText = entity.getContent();
                System.out.println("htpp:url=" + entity.getRequestUrl());
                System.out.println("http:request=" + jsonText);
                try
                {
                    stringEntity = new StringEntity(jsonText, entity.getRequestEncoding());
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
                httpPost.setEntity(stringEntity);
            }
            else
            {
                //TODO
            }
        }

    }

    public HttpRequestEntity getEntity()
    {
        return entity;
    }

    public void setEntity(HttpRequestEntity entity)
    {
        this.entity = entity;
    }
}
