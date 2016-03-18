package com.hdzx.tenement.community.vo;

import java.io.Serializable;

public class ExtraInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String soaKey;

    private String soaValue;
    
    private String soaType;

	public ExtraInfo() {
    }

    public ExtraInfo(String soaKey, String soaValue,String soaType) {
        this.soaKey = soaKey;
        this.soaValue = soaValue;
        this.soaType = soaType;
    }

    public String getSoaKey() {
        return soaKey;
    }

    public void setSoaKey(String soaKey) {
        this.soaKey = soaKey;
    }

    public String getSoaValue() {
        return soaValue;
    }

    public void setSoaValue(String soaValue) {
        this.soaValue = soaValue;
    }
    
    public String getSoaType() {
		return soaType;
	}

	public void setSoaType(String soaType) {
		this.soaType = soaType;
	}

}
