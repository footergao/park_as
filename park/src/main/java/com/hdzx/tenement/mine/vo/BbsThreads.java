package com.hdzx.tenement.mine.vo;

import java.io.Serializable;
import java.util.Date;

public class BbsThreads implements Serializable {
    private static final long serialVersionUID = 5225375957386092527L;
    /**
     * 主题ID
     */
    private String threadId;

    /**
     * 版块ID
     */
    private String forumId;

    /**
     * 标题
     */
    private String subject;

    /**
     * 浏览次数
     */
    private int views;

    /**
     * 回复数量
     */
    private int replies;

    /**
     * 显示顺序：0-普通主题，1-置顶
     */
    private String displayOrder;

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
    private Date createDate;

    private String forumName;       // 版块名称
    /**
     * 获取主题ID
     *
     * @return THREAD_ID - 主题ID
     */
    public String getThreadId() {
        return threadId;
    }

    /**
     * 设置主题ID
     *
     * @param threadId 主题ID
     */
    public void setThreadId(String threadId) {
        this.threadId = threadId == null ? null : threadId.trim();
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
     * 获取浏览次数
     *
     * @return VIEWS - 浏览次数
     */
    public int getViews() {
        return views;
    }

    /**
     * 设置浏览次数
     *
     * @param views 浏览次数
     */
    public void setViews(int views) {
        this.views = views;
    }

    /**
     * 获取回复数量
     *
     * @return REPLIES - 回复数量
     */
    public int getReplies() {
        return replies;
    }

    /**
     * 设置回复数量
     *
     * @param replies 回复数量
     */
    public void setReplies(int replies) {
        this.replies = replies;
    }

    /**
     * 获取显示顺序：0-普通主题，1-置顶
     *
     * @return DISPLAY_ORDER - 显示顺序：0-普通主题，1-置顶
     */
    public String getDisplayOrder() {
        return displayOrder;
    }

    /**
     * 设置显示顺序：0-普通主题，1-置顶
     *
     * @param displayOrder 显示顺序：0-普通主题，1-置顶
     */
    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder == null ? null : displayOrder.trim();
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
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        BbsThreads other = (BbsThreads) that;
        return (this.getThreadId() == null ? other.getThreadId() == null : this.getThreadId().equals(other.getThreadId()))
            && (this.getForumId() == null ? other.getForumId() == null : this.getForumId().equals(other.getForumId()))
            && (this.getSubject() == null ? other.getSubject() == null : this.getSubject().equals(other.getSubject()))
            && (this.getViews() == other.getViews())
            && (this.getReplies() == other.getReplies())
            && (this.getDisplayOrder() == null ? other.getDisplayOrder() == null : this.getDisplayOrder().equals(other.getDisplayOrder()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getThreadId() == null) ? 0 : getThreadId().hashCode());
        result = prime * result + ((getForumId() == null) ? 0 : getForumId().hashCode());
        result = prime * result + ((getSubject() == null) ? 0 : getSubject().hashCode());
        result = prime * result + getViews();
        result = prime * result + getReplies();
        result = prime * result + ((getDisplayOrder() == null) ? 0 : getDisplayOrder().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", threadId=").append(threadId);
        sb.append(", forumId=").append(forumId);
        sb.append(", subject=").append(subject);
        sb.append(", views=").append(views);
        sb.append(", replies=").append(replies);
        sb.append(", displayOrder=").append(displayOrder);
        sb.append(", status=").append(status);
        sb.append(", userName=").append(userName);
        sb.append(", createBy=").append(createBy);
        sb.append(", createDate=").append(createDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}