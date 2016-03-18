package com.hdzx.tenement.mine.vo;

import java.io.Serializable;
import java.util.Date;

/*版块列表*/
public class BbsForums implements Serializable {
    private static final long serialVersionUID = 7576734517696549542L;
    /**
     * 版块ID
     */
    private String forumId;

    /**
     * 版块名称
     */
    private String forumName;

    /**
     * 版块简介
     */
    private String forumIntro;

    /**
     * 版块所属机构
     */
    private Integer deptId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private String createDate;

    private String deptName;            // 部门名称
    private int threadsTodayNum;        // 今日主题数
    private int threadsNum;             // 总主题数

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
     * 获取版块名称
     *
     * @return FORUM_NAME - 版块名称
     */
    public String getForumName() {
        return forumName;
    }

    /**
     * 设置版块名称
     *
     * @param forumName 版块名称
     */
    public void setForumName(String forumName) {
        this.forumName = forumName == null ? null : forumName.trim();
    }

    public String getForumIntro() {
        return forumIntro;
    }

    public void setForumIntro(String forumIntro) {
        this.forumIntro = forumIntro;
    }

    /**
     * 获取版块所属机构
     *
     * @return DEPT_ID - 版块所属机构
     */
    public Integer getDeptId() {
        return deptId;
    }

    /**
     * 设置版块所属机构
     *
     * @param deptId 版块所属机构
     */
    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取创建人
     *
     * @return CREATE_BY - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getThreadsTodayNum() {
        return threadsTodayNum;
    }

    public void setThreadsTodayNum(int threadsTodayNum) {
        this.threadsTodayNum = threadsTodayNum;
    }

    public int getThreadsNum() {
        return threadsNum;
    }

    public void setThreadsNum(int threadsNum) {
        this.threadsNum = threadsNum;
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
        BbsForums other = (BbsForums) that;
        return (this.getForumId() == null ? other.getForumId() == null : this.getForumId().equals(other.getForumId()))
            && (this.getForumName() == null ? other.getForumName() == null : this.getForumName().equals(other.getForumName()))
            && (this.getDeptId() == null ? other.getDeptId() == null : this.getDeptId().equals(other.getDeptId()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getForumId() == null) ? 0 : getForumId().hashCode());
        result = prime * result + ((getForumName() == null) ? 0 : getForumName().hashCode());
        result = prime * result + ((getDeptId() == null) ? 0 : getDeptId().hashCode());
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
        sb.append(", forumId=").append(forumId);
        sb.append(", forumName=").append(forumName);
        sb.append(", deptId=").append(deptId);
        sb.append(", createBy=").append(createBy);
        sb.append(", createDate=").append(createDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}