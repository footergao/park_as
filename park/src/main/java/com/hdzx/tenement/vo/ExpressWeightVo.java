package com.hdzx.tenement.vo;

/**
 * Created by anchendong on 15/8/4.
 */
public class ExpressWeightVo {

    /**
     * 快递公司名称
     */
    private String edCompany;

    /**
     * 目标省
     */
    private String edProvince;

    /**
     * 目标市
     */
    private String edCity;

    /**
     * 重量大于等于X
     */
    private int edStandardAbove;

    /**
     * 重量小于X
     */
    private int edStandardBelow;

    /**
     * 运费
     */
    private double edCost;

    /**
     * 类型：000-普通标准，001-特殊标准，002-不送
     */
    private String edType;

    public String getEdCompany() {
        return edCompany;
    }

    public void setEdCompany(String edCompany) {
        this.edCompany = edCompany;
    }

    public String getEdProvince() {
        return edProvince;
    }

    public void setEdProvince(String edProvince) {
        this.edProvince = edProvince;
    }

    public String getEdCity() {
        return edCity;
    }

    public void setEdCity(String edCity) {
        this.edCity = edCity;
    }

    public int getEdStandardAbove() {
        return edStandardAbove;
    }

    public void setEdStandardAbove(int edStandardAbove) {
        this.edStandardAbove = edStandardAbove;
    }

    public int getEdStandardBelow() {
        return edStandardBelow;
    }

    public void setEdStandardBelow(int edStandardBelow) {
        this.edStandardBelow = edStandardBelow;
    }

    public double getEdCost() {
        return edCost;
    }

    public void setEdCost(double edCost) {
        this.edCost = edCost;
    }

    public String getEdType() {
        return edType;
    }

    public void setEdType(String edType) {
        this.edType = edType;
    }
}
