package com.hdzx.tenement.vo;

import java.io.Serializable;

/**
 * 收到的订单推送消息
 * @author Administrator
 *
 */
public class OrderPushVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* 消费类型开始*/
	 
	/**
	 * 到件通知
	 */
	public static final String TAKE_EXPRESS_NOTICE="TAKE_EXPRESS_NOTICE";
	/**
	 * 新订单
	 */
	public static final String NEW_ORDER="NEW_ORDER";
	/**
	 * 选择咚咚侠
	 */
	public static final String CDDX_ORDER="CDDX_ORDER";
	/**
	 * 通知用户订单被抢
	 */
	public static final String GRAB_ORDER_USER="GRAB_ORDER";
	/**
	 * 通知咚咚侠订单被抢
	 */
	public static final String GRAB_ORDER_DDX="GRAB_ORDER_DDX";
	/**
	 *  用户取消订单
	 */
	public static final String CANCEL_ORDER="CANCEL_ORDER";
	/**
	 * 咚咚侠接受订单
	 */
	public static final String CDDX_ORDER_ACCEPT="CDDX_ORDER_ACCEPT";
	/**
	 * 咚咚侠拒接订单
	 */
	public static final String CDDX_ORDER_REFUSE="CDDX_ORDER_REFUSE";
	/**
	 * 用户线下支付
	 */
	public static final String PAY_OFFLINE_TODDX="PAY_OFFLINE_TODDX";
	/**
	 * 咚咚侠确认收款
	 */
	public static final String CONFIRM_ORDER_PAY="CONFIRM_ORDER_PAY";
	
	/**
	 * 用户已评价
	 */
	public static final String EVALU_ORDER="EVALU_ORDER";
	
	private int messageId;
	private String title= null;
	private String content=null;
	private String senderUserId= null;
	private String icon=null;
	private String category=null;
	private String categoryName=null;
	private String tamplateCode=null;
	private String createDate=null;
	private String lifecircleName=null;
	private Object props=null;
	/**
	 * 消息类型
	 */
	private String interactionType=null;
	/**
	 * 请求服务器编号
	 */
	private String interaction;

	private boolean read;
	
	private String summary;

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSenderUserId() {
		return senderUserId;
	}

	public void setSenderUserId(String senderUserId) {
		this.senderUserId = senderUserId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTamplateCode() {
		return tamplateCode;
	}

	public void setTamplateCode(String tamplateCode) {
		this.tamplateCode = tamplateCode;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLifecircleName() {
		return lifecircleName;
	}

	public void setLifecircleName(String lifecircleName) {
		this.lifecircleName = lifecircleName;
	}

	public Object getProps() {
		return props;
	}

	public void setProps(Object props) {
		this.props = props;
	}

	public String getInteractionType() {
		return interactionType;
	}

	public void setInteractionType(String interactionType) {
		this.interactionType = interactionType;
	}

	/**
	 * 请求服务器编号
	 * @return
	 */
	public String getInteraction() {
		return interaction;
	}

	public void setInteraction(String interaction) {
		this.interaction = interaction;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
}
