package com.hdzx.tenement.mine.vo;

import java.io.Serializable;
import java.util.Date;

public class BbsAttachments implements Serializable {
	/**
	 * 附件ID
	 */
	private String attId;

	/**
	 * 附件所在主题ID
	 */
	private String threadId;

	/**
	 * 附件所在帖子ID
	 */
	private String postId;

	/**
	 * 文件名称
	 */
	private String fileName;

	/**
	 * 附件文件类型，1-图片，2-语音，3-视频
	 */
	private String fileType;

	/**
	 * 附件URL
	 */
	private String fileUrl;

	private String createBy;

	private String createDate;

	private static final long serialVersionUID = 1L;

	private String attrExtend;

	public String getAttrExtend() {
		return attrExtend;
	}

	public void setAttrExtend(String attrExtend) {
		this.attrExtend = attrExtend;
	}

	/**
	 * 获取附件ID
	 *
	 * @return ATT_ID - 附件ID
	 */
	public String getAttId() {
		return attId;
	}

	/**
	 * 设置附件ID
	 *
	 * @param attId
	 *            附件ID
	 */
	public void setAttId(String attId) {
		this.attId = attId == null ? null : attId.trim();
	}

	/**
	 * 获取附件所在主题ID
	 *
	 * @return THREAD_ID - 附件所在主题ID
	 */
	public String getThreadId() {
		return threadId;
	}

	/**
	 * 设置附件所在主题ID
	 *
	 * @param threadId
	 *            附件所在主题ID
	 */
	public void setThreadId(String threadId) {
		this.threadId = threadId == null ? null : threadId.trim();
	}

	/**
	 * 获取附件所在帖子ID
	 *
	 * @return POST_ID - 附件所在帖子ID
	 */
	public String getPostId() {
		return postId;
	}

	/**
	 * 设置附件所在帖子ID
	 *
	 * @param postId
	 *            附件所在帖子ID
	 */
	public void setPostId(String postId) {
		this.postId = postId == null ? null : postId.trim();
	}

	/**
	 * 获取文件名称
	 *
	 * @return FILE_NAME - 文件名称
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 设置文件名称
	 *
	 * @param fileName
	 *            文件名称
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName == null ? null : fileName.trim();
	}

	/**
	 * 获取附件文件类型（MIME Content-Type）
	 *
	 * @return FILE_TYPE - 附件文件类型（MIME Content-Type）
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * 设置附件文件类型（MIME Content-Type）
	 *
	 * @param fileType
	 *            附件文件类型（MIME Content-Type）
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType == null ? null : fileType.trim();
	}

	/**
	 * 获取附件URL
	 *
	 * @return FILE_URL - 附件URL
	 */
	public String getFileUrl() {
		return fileUrl;
	}

	/**
	 * 设置附件URL
	 *
	 * @param fileUrl
	 *            附件URL
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl == null ? null : fileUrl.trim();
	}

	/**
	 * @return CREATE_BY
	 */
	public String getCreateBy() {
		return createBy;
	}

	/**
	 * @param createBy
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy == null ? null : createBy.trim();
	}

	/**
	 * @return CREATE_DATE
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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
		BbsAttachments other = (BbsAttachments) that;
		return (this.getAttId() == null ? other.getAttId() == null : this
				.getAttId().equals(other.getAttId()))
				&& (this.getThreadId() == null ? other.getThreadId() == null
						: this.getThreadId().equals(other.getThreadId()))
				&& (this.getPostId() == null ? other.getPostId() == null : this
						.getPostId().equals(other.getPostId()))
				&& (this.getFileName() == null ? other.getFileName() == null
						: this.getFileName().equals(other.getFileName()))
				&& (this.getFileType() == null ? other.getFileType() == null
						: this.getFileType().equals(other.getFileType()))
				&& (this.getFileUrl() == null ? other.getFileUrl() == null
						: this.getFileUrl().equals(other.getFileUrl()))
				&& (this.getCreateBy() == null ? other.getCreateBy() == null
						: this.getCreateBy().equals(other.getCreateBy()))
				&& (this.getCreateDate() == null ? other.getCreateDate() == null
						: this.getCreateDate().equals(other.getCreateDate()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getAttId() == null) ? 0 : getAttId().hashCode());
		result = prime * result
				+ ((getThreadId() == null) ? 0 : getThreadId().hashCode());
		result = prime * result
				+ ((getPostId() == null) ? 0 : getPostId().hashCode());
		result = prime * result
				+ ((getFileName() == null) ? 0 : getFileName().hashCode());
		result = prime * result
				+ ((getFileType() == null) ? 0 : getFileType().hashCode());
		result = prime * result
				+ ((getFileUrl() == null) ? 0 : getFileUrl().hashCode());
		result = prime * result
				+ ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
		result = prime * result
				+ ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", attId=").append(attId);
		sb.append(", threadId=").append(threadId);
		sb.append(", postId=").append(postId);
		sb.append(", fileName=").append(fileName);
		sb.append(", fileType=").append(fileType);
		sb.append(", fileUrl=").append(fileUrl);
		sb.append(", createBy=").append(createBy);
		sb.append(", createDate=").append(createDate);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}