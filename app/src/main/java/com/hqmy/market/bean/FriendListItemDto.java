package com.hqmy.market.bean;

import java.io.Serializable;

public class FriendListItemDto implements Serializable {
    private String id;
    private String user_id;
    private String friend_user_id;
    private String source;
    private String rela;
    private String follow;
    private String intimacy;
    private String remark_name;
    private String top_chat;
    private String created_at;
    private String is_before_three_day;
    private FriendUserDto friend_user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public FriendUserDto getFriend_user() {
        return friend_user;
    }

    public void setFriend_user(FriendUserDto friend_user) {
        this.friend_user = friend_user;
    }

    public String getIs_before_three_day() {
        return is_before_three_day;
    }

    public void setIs_before_three_day(String is_before_three_day) {
        this.is_before_three_day = is_before_three_day;
    }
}
