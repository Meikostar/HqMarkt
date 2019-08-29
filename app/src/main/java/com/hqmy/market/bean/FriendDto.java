package com.hqmy.market.bean;

import java.io.Serializable;

public class FriendDto implements Serializable {
    private String id;
    private String friend_group_id;
    private String user_id;
    private String friend_user_id;
    private String source;
    private String rela;
    private String follow;
    private String intimacy;
    private String is_new;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private String remark_name;
    private String top_chat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriend_group_id() {
        return friend_group_id;
    }

    public void setFriend_group_id(String friend_group_id) {
        this.friend_group_id = friend_group_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFriend_user_id() {
        return friend_user_id;
    }

    public void setFriend_user_id(String friend_user_id) {
        this.friend_user_id = friend_user_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRela() {
        return rela;
    }

    public void setRela(String rela) {
        this.rela = rela;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getIntimacy() {
        return intimacy;
    }

    public void setIntimacy(String intimacy) {
        this.intimacy = intimacy;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getRemark_name() {
        return remark_name;
    }

    public void setRemark_name(String remark_name) {
        this.remark_name = remark_name;
    }

    public String getTop_chat() {
        return top_chat;
    }

    public void setTop_chat(String top_chat) {
        this.top_chat = top_chat;
    }
}
