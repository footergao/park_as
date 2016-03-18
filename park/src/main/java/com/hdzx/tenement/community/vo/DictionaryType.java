package com.hdzx.tenement.community.vo;

import java.io.Serializable;

public class DictionaryType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6074852636898532935L;
	private String label;
	private String value;
	private String type;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}