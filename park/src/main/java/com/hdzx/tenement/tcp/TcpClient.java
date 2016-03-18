package com.hdzx.tenement.tcp;

/**
 * Created by anchendong on 15/6/30.
 */
public class TcpClient {

	// 回调接口
	public TcpCallbackInterface tcpCallbackInterface;

	/**
	 * 设置TCP回调对象
	 *
	 * @param tcpCallbackInterface
	 */
	public void setCallback(TcpCallbackInterface tcpCallbackInterface) {
		this.tcpCallbackInterface = tcpCallbackInterface;
	}

	/**
	 * 发送TCP信息
	 *
	 * @param msg
	 */
	public void sendMsg(byte[] msg) {
		TcpEntity tcpEntity = new TcpEntity();
		tcpEntity.setSendMsg(msg);
		tcpEntity.setTcpCallbackInterface(tcpCallbackInterface);
		TcpBase.msgSetQueue.offer(tcpEntity);
	}
}
