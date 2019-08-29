package com.hqmy.market.bean;

import java.io.Serializable;

public class GroupListDto implements Serializable{
    private String id;
    private String userId;
    private String group_name;
    private String avatar;
    private String notice;
    private String type;
    private String qrcode_img;
    private String status;
    private String show_nickname;
    private String top_chat;
    private String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQrcode_img() {
        return qrcode_img;
    }

    public void setQrcode_img(String qrcode_img) {
        this.qrcode_img = qrcode_img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShow_nickname() {
        return show_nickname;
    }

    public void setShow_nickname(String show_nickname) {
        this.show_nickname = show_nickname;
    }

    public String getTop_chat() {
        return top_chat;
    }

    public void setTop_chat(String top_chat) {
        this.top_chat = top_chat;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
