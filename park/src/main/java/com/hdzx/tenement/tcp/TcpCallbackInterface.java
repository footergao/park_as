package com.hdzx.tenement.tcp;

/**
 * Created by anchendong on 15/6/30.
 */
public interface TcpCallbackInterface {

    /**
     * 发送同步消息回调接口
     * @param msgRec
     */
    public void tcpCallback(byte[] msgRec);

}
