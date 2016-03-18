package com.hdzx.tenement.pay.vo;

/**
 * Created by Administrator on 2016/3/8.
 */
public class PayType {

    private String bank_no;
    private String pay_channel;
    private String remark;

    public String getBank_no() {
        return bank_no;
    }

    public void setBank_no(String bank_no) {
        this.bank_no = bank_no;
    }

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
