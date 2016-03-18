package com.hdzx.tenement.mine.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BbsPosts implements Serializable {

    private static final long serialVersionUID = -1215913552774618523L;
    private String postId;

    /**
     * 版块ID
     */
    private String forumId;

    /**
     * 主题id
     */
    private String threadId;

    /**
     * 标题
     */
    private String subject;

    /**
     * 类型（0-主题；1-回复）
     */
    private String type;

    /**
     * 是否有附件（0-否，1-是）
     */
    private String attachment;

    /**
     * 状态：0-未审核，1-审核通过，2-审核不通过，3-屏蔽
     */
    private String status;

    /**
     * 创建人名称
     */
    private String userName;

    /**
     * 创建人ID
     */
    private String createBy;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 内容
     */
    private String content;

    /**
     * 回复的帖子
     */
    private String referPost;

    /**
     * 该帖提及的用户
     */
    private String referUser;

    /**
     * 该帖提及的用户的名称
     */
    private String referUserName;
    private String replies;                            // 回复数
    private String userNickName;                    // 用户昵称
    private String userHeadPhoto;                   // 用户头像
    private List<BbsAttachments> attachmentses;     // 附件
    private String customerServiceFlag;             // 是否客服回复：0-否，1-是

    /**
     * @return POST_ID
     */
    public String getPostId() {
        return postId;
    }

    /**
     * @param postId
     */
    public void setPostId(String postId) {
        this.postId = postId == null ? null : postId.trim();
    }

    /**
     * 获取版块ID
     *
     * @return FORUM_ID - 版块ID
     */
    public String getForumId() {
        return forumId;
    }

    /**
     * 设置版块ID
     *
     * @param forumId 版块ID
     */
    public void setForumId(String forumId) {
        this.forumId = forumId == null ? null : forumId.trim();
    }

    /**
     * 获取主题id
     *
     * @return THREAD_ID - 主题id
     */
    public String getThreadId() {
        return threadId;
    }

    /**
     * 设置主题id
     *
     * @param threadId 主题id
     */
    public void setThreadId(String threadId) {
        this.threadId = threadId == null ? null : threadId.trim();
    }

    /**
     * 获取标题
     *
     * @return SUBJECT - 标题
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 设置标题
     *
     * @param subject 标题
     */
    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    /**
     * 获取类型（0-主题；1-回复）
     *
     * @return TYPE - 类型（0-主题；1-回复）
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型（0-主题；1-回复）
     *
     * @param type 类型（0-主题；1-回复）
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取是否有附件（0-否，1-是）
     *
     * @return ATTACHMENT - 是否有附件（0-否，1-是）
     */
    public String getAttachment() {
        return attachment;
    }

    /**
     * 设置是否有附件（0-否，1-是）
     *
     * @param attachment 是否有附件（0-否，1-是）
     */
    public void setAttachment(String attachment) {
        this.attachment = attachment == null ? null : attachment.trim();
    }

    /**
     * 获取状态：0-未审核，1-审核通过，2-审核不通过，3-屏蔽
     *
     * @return STATUS - 状态：0-未审核，1-审核通过，2-审核不通过，3-屏蔽
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态：0-未审核，1-审核通过，2-审核不通过，3-屏蔽
     *
     * @param status 状态：0-未审核，1-审核通过，2-审核不通过，3-屏蔽
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getReferPost() {
        return referPost;
    }

    public void setReferPost(String referPost) {
        this.referPost = referPost;
    }

    public String getReferUser() {
        return referUser;
    }

    public void setReferUser(String referUser) {
        this.referUser = referUser;
    }

    /**
     * 获取创建人名称
     *
     * @return USER_NAME - 创建人名称
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置创建人名称
     *
     * @param userName 创建人名称
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取创建人ID
     *
     * @return CREATE_BY - 创建人ID
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人ID
     *
     * @param createBy 创建人ID
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * 获取创建时间
     *
     * @return CREATE_DATE - 创建时间
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取内容
     *
     * @return CONTENT - 内容
     */
    public String getContent() {
        return content;
    }

    public String getReferUserName() {
        return referUserName;
    }

    public void setReferUserName(String referUserName) {
        this.referUserName = referUserName;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getReplies() {
        return replies;
    }

    public void setReplies(String replies) {
        this.replies = replies;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserHeadPhoto() {
        return userHeadPhoto;
    }

    public void setUserHeadPhoto(String userHeadPhoto) {
        this.userHeadPhoto = userHeadPhoto;
    }

    public List<BbsAttachments> getAttachmentses() {
        return attachmentses;
    }

    public void setAttachmentses(List<BbsAttachments> attachmentses) {
        this.attachmentses = attachmentses;
    }

    public String getCustomerServiceFlag() {
        return customerServiceFlag;
    }

    public void setCustomerServiceFlag(String customerServiceFlag) {
        this.customerServiceFlag = customerServiceFlag;
    }
}