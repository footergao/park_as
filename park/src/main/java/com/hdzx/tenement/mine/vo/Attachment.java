package com.hdzx.tenement.mine.vo;

/**
 * User: hope chen
 * Date: 2015/11/24
 * Description: 附件信息
 */
public class Attachment {

    private String fileType;
    private String fileUrl;
    private String attrExtend;
    

    public String getAttrExtend() {
		return attrExtend;
	}

	public void setAttrExtend(String attrExtend) {
		this.attrExtend = attrExtend;
	}

	public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
