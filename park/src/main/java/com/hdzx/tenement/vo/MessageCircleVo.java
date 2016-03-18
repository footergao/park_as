package com.hdzx.tenement.vo;

import java.util.List;

/**
 * Created by anchendong on 15/7/22.
 */
public class MessageCircleVo {
    private int msgtype;
    /**
     * 1 系统消息
     * 2 物业消息
     * 3 商业消息
     * 4 快递消息
     * 5 物业咨询
     * 6 员工消息
     */
    private List<String> msginfo;
    private String time;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public List<String> getMsginfo() {
        return msginfo;
    }

    public void setMsginfo(List<String> msginfo) {
        this.msginfo = msginfo;
    }
}
