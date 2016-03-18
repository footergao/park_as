package com.hdzx.tenement.vo;

import java.io.Serializable;

public class MessageOutlineBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 系统消息
	 */
	public static final String CATEGORY_SYSTEM="CATEGORY_SYSTEM";
	/**
	 * 物业消息
	 */
	public static final String CATEGORY_TENEMENT="CATEGORY_TENEMENT";
	/**
	 * 商业消息
	 */
	public static final String CATEGORY_COMMERSE="CATEGORY_COMMERSE";
	/**
	 * 社区消息
	 */
	public static final String CATEGORY_COMMUNITY="CATEGORY_COMMUNITY";
	/**
	 * 社区通快递消息
	 */
	public static final String ATEGORY_COMMUNITY_EXPRESS="ATEGORY_COMMUNITY_EXPRESS";
	/**
	 * 社区通物业消息
	 */
	public static final String CATEGORY_COMMUNITY_TENEMENT="CATEGORY_COMMUNITY_TENEMENT";
	//消息类别ID
	private int msgConfigId;
	//消息类别名称
	private String name;
	private String category;
	private String icon;
	private int unreadCount;
	private MessageBean msgMessage;
	public int getMsgConfigId() {
		return msgConfigId;
	}
	public void setMsgConfigId(int msgConfigId) {
		this.msgConfigId = msgConfigId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getUnreadCount() {
		return unreadCount;
	}
	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}
	public MessageBean getMsgMessage() {
		return msgMessage;
	}
	public void setMsgMessage(MessageBean msgMessage) {
		this.msgMessage = msgMessage;
	}
	@Override
	public String toString() {
		return "MessageOutlineBean [msgConfigId=" + msgConfigId + ", name="
				+ name + ", category=" + category + ", icon=" + icon
				+ ", unreadCount=" + unreadCount + ", msgMessage=" + msgMessage
				+ "]";
	}
}
