package com.hdzx.tenement.community.vo;

import java.io.Serializable;
import java.util.List;

/**
 * User: hope chen
 * Date: 2015/12/23
 * Description: 便民电话分类集合
 */
public class ConvenPhoneSet implements Serializable {

    private static final long serialVersionUID = -1388883449529110451L;
    private String phoneType;           // 便民电话类型
    private String phoneTypeName;       // 便民电话类型名称
    private List<ConvenPhone> conveniencePhones;

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhoneTypeName() {
        return phoneTypeName;
    }

    public void setPhoneTypeName(String phoneTypeName) {
        this.phoneTypeName = phoneTypeName;
    }

    public List<ConvenPhone> getConveniencePhones() {
        return conveniencePhones;
    }

    public void setConveniencePhones(List<ConvenPhone> conveniencePhones) {
        this.conveniencePhones = conveniencePhones;
    }
}
