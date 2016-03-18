package com.hdzx.tenement.community.vo;

public class Repair {

	private String repairId;
	private String userId;
	private String lifecircleId;
	private String repairType;
	private String files;
	private String repairContent;
	private String adviceTime;
	private boolean hasNewMsg;
	public String getRepairId() {
		return repairId;
	}
	public void setRepairId(String repairId) {
		this.repairId = repairId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLifecircleId() {
		return lifecircleId;
	}
	public void setLifecircleId(String lifecircleId) {
		this.lifecircleId = lifecircleId;
	}
	public String getRepairType() {
		return repairType;
	}
	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}
	public String getFiles() {
		return files;
	}
	public void setFiles(String files) {
		this.files = files;
	}
	public String getRepairContent() {
		return repairContent;
	}
	public void setRepairContent(String repairContent) {
		this.repairContent = repairContent;
	}
	public String getAdviceTime() {
		return adviceTime;
	}
	public void setAdviceTime(String adviceTime) {
		this.adviceTime = adviceTime;
	}
	public boolean getHasNewMsg() {
		return hasNewMsg;
	}
	public void setHasNewMsg(boolean hasNewMsg) {
		this.hasNewMsg = hasNewMsg;
	}

	

}
