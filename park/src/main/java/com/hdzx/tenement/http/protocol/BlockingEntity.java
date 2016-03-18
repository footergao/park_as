package com.hdzx.tenement.http.protocol;

public class BlockingEntity
{
    //private int timeout = 15;
    
    private int timeout = 60;
    
    private HttpAsyncTask httpTask = null;

    public BlockingEntity(int timeout, HttpAsyncTask httpTask)
    {
        super();
        this.timeout = timeout;
        this.httpTask = httpTask;
    }

    public BlockingEntity(HttpAsyncTask httpTask)
    {
        super();
        this.httpTask = httpTask;
    }

    public HttpAsyncTask getHttpTask()
    {
        return httpTask;
    }

    public void setHttpTask(HttpAsyncTask httpTask)
    {
        this.httpTask = httpTask;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public int timeout()
    {
        --timeout;
        System.out.println("timeout=" + timeout);
        return timeout;
    }
}
