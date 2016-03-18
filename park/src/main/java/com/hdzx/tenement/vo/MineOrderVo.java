package com.hdzx.tenement.vo;

/**
 * Created by anchendong on 15/8/6.
 */
public class MineOrderVo {

    private String status;
    private String statusInfo;
    private String ordertype;
    private String ordertypeStatus;
    private String cost;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getOrdertypeStatus() {
        return ordertypeStatus;
    }

    public void setOrdertypeStatus(String ordertypeStatus) {
        this.ordertypeStatus = ordertypeStatus;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
