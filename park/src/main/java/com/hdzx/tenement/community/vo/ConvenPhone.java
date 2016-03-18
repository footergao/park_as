package com.hdzx.tenement.community.vo;

import java.io.Serializable;

/**
 * User: hope chen
 * Date: 2015/12/23
 * Description: 便民电话
 */
public class ConvenPhone implements Serializable {

    private static final long serialVersionUID = 7286263390834730114L;
    private String conveniencePhoneId;      // 便民电话ID
    private String lifecircleId;            // 生活圈ID
    private String phoneType;               // 便民电话类型，同上
    private String phoneName;               // 电话名称
    private String phoneNum;                // 电话号码，多个使用 | 分隔
    private String phoneDetail;             // 描述
    private String serverDate;              // 服务日期
    private String serverTime;              // 服务时间

    public String getConveniencePhoneId() {
        return conveniencePhoneId;
    }

    public void setConveniencePhoneId(String conveniencePhoneId) {
        this.conveniencePhoneId = conveniencePhoneId;
    }

    public String getLifecircleId() {
        return lifecircleId;
    }

    public void setLifecircleId(String lifecircleId) {
        this.lifecircleId = lifecircleId;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhoneDetail() {
        return phoneDetail;
    }

    public void setPhoneDetail(String phoneDetail) {
        this.phoneDetail = phoneDetail;
    }

    public String getServerDate() {
        return serverDate;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
}
