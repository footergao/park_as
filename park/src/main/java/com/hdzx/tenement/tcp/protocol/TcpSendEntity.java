package com.hdzx.tenement.tcp.protocol;

import com.hdzx.tenement.tcp.TcpContants;

import java.io.Serializable;

/**
 * 
 * @author Jesley
 *
 */
public class TcpSendEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    private TcpSendTemplate sendTemplate = null;
    
    private TcpCallback tcpCallback = null;
    
    private int timeout = 15;
    
    private int retry = 2;
    
    private String encoding = TcpContants.CHARACTER_ENCODING;
    
    public TcpSendEntity()
    {
        super();
    }

    public TcpSendEntity(TcpSendTemplate sendTemplate)
    {
        super();
        this.sendTemplate = sendTemplate;
    }

    public TcpSendEntity(TcpSendTemplate sendTemplate, TcpCallback tcpCallback)
    {
        super();
        this.sendTemplate = sendTemplate;
        this.tcpCallback = tcpCallback;
    }

    public TcpSendTemplate getSendTemplate()
    {
        return sendTemplate;
    }

    public void setSendTemplate(TcpSendTemplate sendTemplate)
    {
        this.sendTemplate = sendTemplate;
    }

    public TcpCallback getTcpCallback()
    {
        return tcpCallback;
    }

    public void setTcpCallback(TcpCallback tcpCallback)
    {
        this.tcpCallback = tcpCallback;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public int getRetry()
    {
        return retry;
    }

    public void setRetry(int retry)
    {
        this.retry = retry;
    }
    
    public int timeout()
    {
        --timeout;
        return timeout;
    }
    
    public int countRetry()
    {
        --retry;
        return retry;
    }
    
    public void callback(String message)
    {
        System.out.println("TcpRequestEntity: message=" + message);
    }

    public String getEncoding()
    {
        return encoding;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }
}
