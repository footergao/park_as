package com.hdzx.tenement.mine.vo;

import java.io.Serializable;
import java.util.List;

import com.hdzx.tenement.vo.CellVo;

public class LifeCircleAddressVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer lifecircleId = null;
	private Integer regionalId = null;
	private Integer aluId = null;
	private String lifecircleName = null;
	
	
	private String keywordType=null;
	
	//true-在当前生活圈下，false-不在
	private Boolean isInside = null;
	
	//true-已绑定，false-未绑定（008有）
	private Boolean isBindLifecircle=null;
	
	//true-已绑定户号，false-未绑定（008有）
	private Boolean isBindAr=null;
	
	private List<CellVo> regionalInfos= null;
	
	private String lifecircleCity=null;
	

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

	/**
	 * true-在当前生活圈下，false-不在
	 * @return
	 */
	public Boolean getIsInside() {
		return isInside;
	}

	public void setIsInside(Boolean isInside) {
		this.isInside = isInside;
	}

	/**
	 * true-已绑定，false-未绑定（008有）
	 * @return
	 */
	public Boolean getIsBindLifecircle() {
		return isBindLifecircle;
	}

	public void setIsBindLifecircle(Boolean isBindLifecircle) {
		this.isBindLifecircle = isBindLifecircle;
	}

	/**
	 * true-已绑定户号，false-未绑定（008有）
	 * @return
	 */
	public Boolean getIsBindAr() {
		return isBindAr;
	}

	public void setIsBindAr(Boolean isBindAr) {
		this.isBindAr = isBindAr;
	}

	public List<CellVo> getRegionalInfos() {
		return regionalInfos;
	}

	public void setRegionalInfos(List<CellVo> regionalInfos) {
		this.regionalInfos = regionalInfos;
	}

	public String getKeywordType() {
		return keywordType;
	}

	public void setKeywordType(String keywordType) {
		this.keywordType = keywordType;
	}

	public String getLifecircleCity() {
		return lifecircleCity;
	}

	public void setLifecircleCity(String lifecircleCity) {
		this.lifecircleCity = lifecircleCity;
	}

	public Integer getRegionalId() {
		return regionalId;
	}

	public void setRegionalId(Integer regionalId) {
		this.regionalId = regionalId;
	}

	public Integer getAluId() {
		return aluId;
	}

	public void setAluId(Integer aluId) {
		this.aluId = aluId;
	}
	
	
}
