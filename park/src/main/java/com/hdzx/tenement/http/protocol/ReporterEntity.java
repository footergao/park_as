package com.hdzx.tenement.http.protocol;

public class ReporterEntity
{
    private HttpAsyncTask httpTask = null;
    
    private ResponseContentTamplate responseContent = null;
    
    public ReporterEntity(HttpAsyncTask httpTask, ResponseContentTamplate responseContent)
    {
        super();
        this.httpTask = httpTask;
        this.responseContent = responseContent;
    }

    public HttpAsyncTask getHttpTask()
    {
        return httpTask;
    }

    public void setHttpTask(HttpAsyncTask httpTask)
    {
        this.httpTask = httpTask;
    }

    public ResponseContentTamplate getResponseContent()
    {
        return responseContent;
    }

    public void setResponseContent(ResponseContentTamplate responseContent)
    {
        this.responseContent = responseContent;
    }
}
