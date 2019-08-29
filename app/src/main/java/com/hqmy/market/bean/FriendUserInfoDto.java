package com.hqmy.market.bean;

import java.io.Serializable;

/*
 * Created by rzb on 2019/6/22.
 */
public class FriendUserInfoDto implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String created_at;
    private String updated_at;
    private String avatar;
    private String sex;
    private String ext;
    private String im_token;
    private FriendDto friend;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIm_token() {
        return im_token;
    }

    public void setIm_token(String im_token) {
        this.im_token = im_token;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public FriendDto getFriend() {
        return friend;
    }

    public void setFriend(FriendDto friend) {
        this.friend = friend;
    }
}
