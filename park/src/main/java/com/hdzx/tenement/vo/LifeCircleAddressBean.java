package com.hdzx.tenement.vo;

import java.io.Serializable;

/**
 * 生活圈bean
 * @author Administrator
 *
 */
public class LifeCircleAddressBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer lifecircleId=null;
	private String lifecircleName=null;
	//省
	private String province;
	//市
	private String city;
	private String area;//区
	private String lng;//纬度
	private String lat;//经度
	public Integer getLifecircleId() {
		return lifecircleId;
	}
	public void setLifecircleId(Integer lifecircleId) {
		this.lifecircleId = lifecircleId;
	}
	public String getLifecircleName() {
		return lifecircleName;
	}
	public void setLifecircleName(String lifecircleName) {
		this.lifecircleName = lifecircleName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}

}
