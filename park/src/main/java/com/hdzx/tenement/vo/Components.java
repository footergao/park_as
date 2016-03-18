package com.hdzx.tenement.vo;

import java.io.Serializable;

public class Components implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 001，图片；002，按钮
	 */
	private String componentType;
	private String componentContent;
	/**
	 * 001，图片；002，按钮
	 */
	public String getComponentType() {
		return componentType;
	}
	/**
	 * 001，图片；002，按钮
	 */
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}
	/**
	 * 显示名称
	 * @return
	 */
	public String getComponentContent() {
		return componentContent;
	}
	/**
	 * 显示名称
	 * @param componentContent
	 */
	public void setComponentContent(String componentContent) {
		this.componentContent = componentContent;
	}
	
}
