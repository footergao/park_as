package com.hdzx.tenement.vo;

/**
 * Created by anchendong on 15/7/22.
 */
public class MessageCircleInfoVo {
    private String msgtitle;
    private String msginfo;
    private String time;
    private String author;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsgtitle() {
        return msgtitle;
    }

    public void setMsgtitle(String msgtitle) {
        this.msgtitle = msgtitle;
    }

    public String getMsginfo() {
        return msginfo;
    }

    public void setMsginfo(String msginfo) {
        this.msginfo = msginfo;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
