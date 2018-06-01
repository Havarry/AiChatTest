package com.gzhy.aichat.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hy on 2018-05-29
 */

public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String msg;
    private String senderType;
    private String SenderName;
    private String SenderFace;

    public ChatMessage() {

    }

    public ChatMessage(String msg, String senderType) {
        this.msg = msg;
        this.senderType = senderType;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public String getSenderFace() {
        return SenderFace;
    }

    public void setSenderFace(String senderFace) {
        SenderFace = senderFace;
    }
}
