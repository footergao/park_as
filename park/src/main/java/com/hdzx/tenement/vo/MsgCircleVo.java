package com.hdzx.tenement.vo;

import java.io.Serializable;
import java.util.List;

public class MsgCircleVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MessageBean msgMessage;
	private List<MessageOutlineBean> outlineList;
	public MessageBean getMsgMessage() {
		return msgMessage;
	}
	public void setMsgMessage(MessageBean msgMessage) {
		this.msgMessage = msgMessage;
	}
	public List<MessageOutlineBean> getOutlineList() {
		return outlineList;
	}
	public void setOutlineList(List<MessageOutlineBean> outlineList) {
		this.outlineList = outlineList;
	}
	
}
