package com.gzhy.aichat.model;

import android.graphics.Bitmap;

/**
 * Created by hy on 2018-05-29
 */

public class MsgMode {
    private int type;
    private String content;
    private Bitmap icon;

    public MsgMode() {
    }

    public MsgMode(int type, String content, Bitmap icon) {
        this.type = type;
        this.content = content;
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
