package com.hdzx.tenement.tcp;

/**
 * Created by anchendong on 15/6/30.
 */
public class TcpEntity {
    //回调对象
    private TcpCallbackInterface tcpCallbackInterface;
    //发送信息
    private byte[] sendMsg;

    public TcpCallbackInterface getTcpCallbackInterface() {
        return tcpCallbackInterface;
    }

    public void setTcpCallbackInterface(TcpCallbackInterface tcpCallbackInterface) {
        this.tcpCallbackInterface = tcpCallbackInterface;
    }

    public byte[] getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(byte[] sendMsg) {
        this.sendMsg = sendMsg;
    }
}
