package com.hdzx.tenement.vo;

import java.io.Serializable;

/**
 * 小区
 * @author Administrator
 *
 */
public class CellVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer regionalId=null;
	private String regionalName=null;
	//纬度
	private Double latitude=null;
	//经度
	private Double longitude=null;
	
	private Integer radius=null;

	public Integer getRegionalId() {
		return regionalId;
	}

	public void setRegionalId(Integer regionalId) {
		this.regionalId = regionalId;
	}

	public String getRegionalName() {
		return regionalName;
	}

	public void setRegionalName(String regionalName) {
		this.regionalName = regionalName;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

}
