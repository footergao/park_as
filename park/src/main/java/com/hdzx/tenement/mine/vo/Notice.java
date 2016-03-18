package com.hdzx.tenement.mine.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * User: hope chen
 * Date: 2015/11/30
 * Description: 消息
 */
public class Notice implements Serializable {

    private static final long serialVersionUID = -311072934540513671L;

    private String threadId;
    private String postId;
    private String subject;
    private String imageUrl;

    private String referUserName;
    private String replyPostId;
    private String replyUserId;
    private String replyUserNick;
    private String replyUserHeadPhoto;
    private String replyContent;
    private String replyTime;
    private String customerServiceFlag;             // 是否客服回复：0-否，1-是

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReferUserName() {
        return referUserName;
    }

    public void setReferUserName(String referUserName) {
        this.referUserName = referUserName;
    }

    public String getReplyPostId() {
        return replyPostId;
    }

    public void setReplyPostId(String replyPostId) {
        this.replyPostId = replyPostId;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyUserNick() {
        return replyUserNick;
    }

    public void setReplyUserNick(String replyUserNick) {
        this.replyUserNick = replyUserNick;
    }

    public String getReplyUserHeadPhoto() {
        return replyUserHeadPhoto;
    }

    public void setReplyUserHeadPhoto(String replyUserHeadPhoto) {
        this.replyUserHeadPhoto = replyUserHeadPhoto;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getCustomerServiceFlag() {
        return customerServiceFlag;
    }

    public void setCustomerServiceFlag(String customerServiceFlag) {
        this.customerServiceFlag = customerServiceFlag;
    }
}
