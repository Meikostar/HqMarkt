package com.hqmy.market.bean;

public class MessageCenterBean {
    private String type;
    private String describe;
    private String time;
    private int icon;
    private boolean hasNewMsg;

    public boolean isHasNewMsg() {
        return hasNewMsg;
    }

    public void setHasNewMsg(boolean hasNewMsg) {
        this.hasNewMsg = hasNewMsg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
