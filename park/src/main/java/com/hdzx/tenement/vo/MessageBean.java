package com.hdzx.tenement.vo;

import java.io.Serializable;
import java.util.List;

public class MessageBean implements Serializable {

	/**
	 * 社区交流新回复通知
	 */
	public static final String BBS_NEW_MESSAGE="BBS_NEW_MESSAGE";
	
	/**
	 * 订单发货提醒
	 */
	public static final String BRAND_ORDER_DELIVER="BRAND_ORDER_DELIVER";
	/**
	 * 享帮帮取消订单
	 */
	public static final String CANCEL_ORDER="CANCEL_ORDER";
	/**
	 * 享帮帮选择咚咚侠
	 */
	public static final String CDDX_ORDER="CDDX_ORDER";
	/**
	 * 指定咚咚侠接单
	 */
	public static final String CDDX_ORDER_ACCEPT="CDDX_ORDER_ACCEPT";
	/**
	 * 指定咚咚侠拒绝接单
	 */
	public static final String CDDX_ORDER_REFUSE="CDDX_ORDER_REFUSE";
	/**
	 * 咚咚侠服务资格申请被审核
	 */
	public static final String DDX_SERVICE_CHECK="DDX_SERVICE_CHECK";

	/**
	 * 物业通知消息
	 */
	public static final String ESTATE_NOTICE="ESTATE_NOTICE";
	

	/**
	 * 品牌街提醒
	 */
	public static final String BRAND_NOTICE="BRAND_NOTICE";
	/**
	 *软件更新
	 */
	public static final String APP_UPDATE="APP_UPDATE";
	/**
	 * 成为咚咚侠
	 */
	public static final String APPLY_DDX_SUCCESS="APPLY_DDX_SUCCESS";
	/**
	 * 活动汇消息提醒
	 */
	public static final String ACTIVE_NOTICE="ACTIVE_NOTICE";
	
	/**
	 * 投诉建议有新回复(XBB)
	 */
	public static final String ESTATE_ADVICE_TOXBB="ESTATE_ADVICE_TOXBB";
	/**
	 * 投诉建议有新回复(XBB)
	 */
	public static final String ESTATE_REPAIR_TOXBB="ESTATE_REPAIR_TOXBB";
	/**
	 * 到件通知
	 */
	public static final String TAKE_EXPRESS_NOTICE="TAKE_EXPRESS_NOTICE";
	/**
	 * 评价
	 */
	public static final String EVALU_ORDER="EVALU_ORDER";
	/**
	 * 咚咚侠抢单,发送给用户（抢单）
	 */
	public static final String GRAB_ORDER="GRAB_ORDER";
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 消息摘要
	private String title;
	// 消息内容
	private String content;
	// 发送者用户ID
	private String senderUserId;
	// 消息图标URL
	private String icon;
	// 消息类别代码：
	// CATEGORY_SYSTEM：系统消息
	// CATEGORY_TENEMENT：物业消息
	// CATEGORY_COMMERSE：商业消息
	// CATEGORY_COMMUNITY：社区通消息
	// ATEGORY_COMMUNITY_EXPRESS：社区通快递消息
	// CATEGORY_COMMUNITY_TENEMENT：社区通物业消息
	private String category;
	// 消息类别名称
	private String categoryName;
	// 消息是否已读
	private boolean read;
	// 消息创建日期
	private String createDate;

	// 消息ID
	private int messageId;
	// 消息模板代码：
	// PLAIN：纯文字模板
	// RFT：富文本
	// EXLINK：外链接
	private String tamplateCode;
	// 消息属性，目前只有一个属性“EXTENT_URL”，代表外部链接。
	private Object props;
	
	private List<Components> components;
	/**
	 * 请求服务器编号
	 */
	private String interaction;
	
	private String interactionType;
	

	public String getInteraction() {
		return interaction;
	}

	public void setInteraction(String interaction) {
		this.interaction = interaction;
	}

	public String getInteractionType() {
		return interactionType;
	}

	public void setInteractionType(String interactionType) {
		this.interactionType = interactionType;
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

	/**
	 * 消息类别代码：
	 * CATEGORY_SYSTEM：系统消息 
	 * CATEGORY_TENEMENT：物业消息
	 * CATEGORY_COMMERSE：商业消息
	 * CATEGORY_COMMUNITY：社区通消息
	 * ATEGORY_COMMUNITY_EXPRESS：社区通快递消息 
	 * CATEGORY_COMMUNITY_TENEMENT：社区通物业消息
	 */

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

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	 /**
	  * 消息模板代码：
	  * PLAIN：纯文字模板
	  * RFT：富文本
	  * EXLINK：外链接
	  */
	public String getTamplateCode() {
		return tamplateCode;
	}

	public void setTamplateCode(String tamplateCode) {
		this.tamplateCode = tamplateCode;
	}

	public Object getProps() {
		return props;
	}

	public void setProps(Object props) {
		this.props = props;
	}

	public List<Components> getComponents() {
		return components;
	}

	public void setComponents(List<Components> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "MessageBean [title=" + title + ", content=" + content
				+ ", senderUserId=" + senderUserId + ", icon=" + icon
				+ ", category=" + category + ", categoryName=" + categoryName
				+ ", read=" + read + ", createDate=" + createDate
				+ ", messageId=" + messageId + ", tamplateCode=" + tamplateCode
				+ ", props=" + props + ", components=" + components + "]";
	}
}
