package com.hdzx.tenement.mine.vo;

import java.io.Serializable;

public class BbsPostsRead implements Serializable {

    private static final long serialVersionUID = -4253467255317115990L;
    /**
     * 帖子ID
     */
    private String postId;

    /**
     * 回复的帖子
     */
    private String referPost;

    /**
     * 已读到
     */
    private String isRead;

    public BbsPostsRead() {
    }

    public BbsPostsRead(String postId, String referPost, String isRead) {
        this.postId = postId;
        this.referPost = referPost;
        this.isRead = isRead;
    }

    /**
     * 获取帖子ID
     *
     * @return POST_ID - 帖子ID
     */
    public String getPostId() {
        return postId;
    }

    /**
     * 设置帖子ID
     *
     * @param postId 帖子ID
     */
    public void setPostId(String postId) {
        this.postId = postId == null ? null : postId.trim();
    }

    public String getReferPost() {
        return referPost;
    }

    public void setReferPost(String referPost) {
        this.referPost = referPost;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}