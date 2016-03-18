package com.hdzx.tenement.vo;

/**
 * Created by anchendong on 15/7/27.
 */
public class ExpressAddressVo {

    /**
     * 地址别名
     */
    private String addressalias;
    /**
     * 地址具体信息
     */
    private String addressinfo;
    /**
     * 选中标志
     */
    private int selectstatus;//0 未选中 1选中

    public String getAddressalias() {
        return addressalias;
    }

    public void setAddressalias(String addressalias) {
        this.addressalias = addressalias;
    }

    public String getAddressinfo() {
        return addressinfo;
    }

    public void setAddressinfo(String addressinfo) {
        this.addressinfo = addressinfo;
    }

    public int getSelectstatus() {
        return selectstatus;
    }

    public void setSelectstatus(int selectstatus) {
        this.selectstatus = selectstatus;
    }
}
