package com.hdzx.tenement.tcp.protocol;

/**
 * 
 * @author Jesley
 *
 */
public interface TcpCallback
{
    void processCallbackData(TcpReceiveTamplate template);
}
