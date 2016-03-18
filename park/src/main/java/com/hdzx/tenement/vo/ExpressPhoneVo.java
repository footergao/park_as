package com.hdzx.tenement.vo;

/**
 * Created by anchendong on 15/7/27.
 */
public class ExpressPhoneVo {

    /**
     * 主键
     */
    private Integer enpId;

    /**
     * 手机号
     */
    private String enpPhone;

    /**
     * 备注名
     */
    private String enpName;

    /**
     * 000-我的（不可修改），001-可修改
     */
    private String enpStatus;


    public String getEnpPhone() {
        return enpPhone;
    }

    public void setEnpPhone(String enpPhone) {
        this.enpPhone = enpPhone;
    }

    public String getEnpName() {
        return enpName;
    }

    public void setEnpName(String enpName) {
        this.enpName = enpName;
    }

    public String getEnpStatus() {
        return enpStatus;
    }

    public void setEnpStatus(String enpStatus) {
        this.enpStatus = enpStatus;
    }

    public Integer getEnpId() {
        return enpId;
    }

    public void setEnpId(Integer enpId) {
        this.enpId = enpId;
    }
}
