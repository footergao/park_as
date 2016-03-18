package com.hdzx.tenement.vo;

/**
 * 品牌
 */
public class CommodityVo {
    /**
     * id
     */
    private String id;
    /**
     * 品牌名字
     */
    private String name;
    /**
     * 品牌logo
     */
    private String logo;
    /**
     * 品牌海报url
     */
    private String poster;
    /**
     * 品牌分类
     */
    private String type;
    /**
     * 标签
     */
    private String tag;
    /**
     * 状态（1启用，0停用）
     */
    private String status;
    /**
     * 是否在主页显示（1是0否）
     */
    private String isShowHome;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 生活圈id
     */
    private String lifecircleId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsShowHome() {
        return isShowHome;
    }

    public void setIsShowHome(String isShowHome) {
        this.isShowHome = isShowHome;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLifecircleId() {
        return lifecircleId;
    }

    public void setLifecircleId(String lifecircleId) {
        this.lifecircleId = lifecircleId;
    }
}
