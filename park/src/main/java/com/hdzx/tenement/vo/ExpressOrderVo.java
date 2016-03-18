package com.hdzx.tenement.vo;

public class ExpressOrderVo {

    /**
     * 订单编号
     */
    private String ecId;

    /**
     * 寄件：
     * 000-等待付款，
     * 001-付款失败，
     * 002-付款成功等待产生取件记录与嗨单，
     * 003-等待抢单，
     * 004-等待提交运单号，
     * 005-等待寄件，
     * 006-等待寄件评价，
     * 007-寄件完成；
     * <p/>
     * 送件：
     * 000-待提交配送信息，
     * 001-自提，
     * 002-等待付款，
     * 003-付款失败，
     * 004-付款成功生成送单记录并发送嗨单，
     * 005-等待抢单，
     * 006-等待送件，
     * 007-等待评价，
     * 008-送件完成
     */
    private String ecStatus;

    /**
     * 订单费用
     */
    private String ecCost;

    /**
     * 帮帮俠姓名
     */
    private String ecHelper;

    public String getEcId() {
        return ecId;
    }

    public void setEcId(String ecId) {
        this.ecId = ecId;
    }

    public String getEcStatus() {
        return ecStatus;
    }

    public void setEcStatus(String ecStatus) {
        this.ecStatus = ecStatus;
    }

    public String getEcCost() {
        return ecCost;
    }

    public void setEcCost(String ecCost) {
        this.ecCost = ecCost;
    }

    public String getEcHelper() {
        return ecHelper;
    }

    public void setEcHelper(String ecHelper) {
        this.ecHelper = ecHelper;
    }
}
