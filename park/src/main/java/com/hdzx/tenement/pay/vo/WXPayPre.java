package com.hdzx.tenement.pay.vo;

/**
 * Created by Administrator on 2016/3/8.
 */
public class WXPayPre {

    //
    private String appid = "";
    private String partnerid = "";
    private String nonce_str = "";
    private String prepay_id = "";
    private String packageValue = "";
    private String sign = "";
    private String timestamp = "";
    private String ind_no = "";


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getInd_no() {
        return ind_no;
    }

    public void setInd_no(String ind_no) {
        this.ind_no = ind_no;
    }
}
