package com.hdzx.tenement.mine.vo;

import java.io.Serializable;

/**
 * 地址簿对象
 */
public class MineAddressVo implements Serializable {

    private static final long serialVersionUID = 7725639996866403449L;
    private String abId;
    private String abLastuseName;
    private String abLastuseContact;
    private String abProvince;
    private String abCity;
    private String abArea;
    private String abStreet = "";
    private String abOther;
    private String abLastuseTime;
    private String abLastuseScene;

    public String getAbId() {
        return abId;
    }

    public void setAbId(String abId) {
        this.abId = abId;
    }

    public String getAbLastuseName() {
        return abLastuseName;
    }

    public void setAbLastuseName(String abLastuseName) {
        this.abLastuseName = abLastuseName;
    }

    public String getAbLastuseContact() {
        return abLastuseContact;
    }

    public void setAbLastuseContact(String abLastuseContact) {
        this.abLastuseContact = abLastuseContact;
    }

    public String getAbProvince() {
        return abProvince;
    }

    public void setAbProvince(String abProvince) {
        this.abProvince = abProvince;
    }

    public String getAbCity() {
        return abCity;
    }

    public void setAbCity(String abCity) {
        this.abCity = abCity;
    }

    public String getAbArea() {
        return abArea;
    }

    public void setAbArea(String abArea) {
        this.abArea = abArea;
    }

    public String getAbStreet() {
        return abStreet;
    }

    public void setAbStreet(String abStreet) {
        this.abStreet = abStreet;
    }

    public String getAbOther() {
        return abOther;
    }

    public void setAbOther(String abOther) {
        this.abOther = abOther;
    }

    public String getAbLastuseTime() {
        return abLastuseTime;
    }

    public void setAbLastuseTime(String abLastuseTime) {
        this.abLastuseTime = abLastuseTime;
    }

    public String getAbLastuseScene() {
        return abLastuseScene;
    }

    public void setAbLastuseScene(String abLastuseScene) {
        this.abLastuseScene = abLastuseScene;
    }
}
