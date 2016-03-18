package com.hdzx.tenement.community.vo;

import java.io.Serializable;
import java.util.Date;

public class Arcticle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6881801658359712375L;

	private String acId;

	private String acName;

	private String acPicture;

	private String staticUrl;

	private String dynameicUrl;

	private String realUrl;

	public String getAcId() {
		return acId;
	}

	public void setAcId(String acId) {
		this.acId = acId;
	}

	public String getAcName() {
		return acName;
	}

	public void setAcName(String acName) {
		this.acName = acName;
	}

	public String getAcPicture() {
		return acPicture;
	}

	public void setAcPicture(String acPicture) {
		this.acPicture = acPicture;
	}

	public String getStaticUrl() {
		return staticUrl;
	}

	public void setStaticUrl(String staticUrl) {
		this.staticUrl = staticUrl;
	}

	public String getDynameicUrl() {
		return dynameicUrl;
	}

	public void setDynameicUrl(String dynameicUrl) {
		this.dynameicUrl = dynameicUrl;
	}

	public String getRealUrl() {
		return realUrl;
	}

	public void setRealUrl(String realUrl) {
		this.realUrl = realUrl;
	}
}