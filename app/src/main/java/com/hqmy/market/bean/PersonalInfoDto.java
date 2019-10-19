package com.hqmy.market.bean;

import java.io.Serializable;

public class PersonalInfoDto implements Serializable {
    private String id;
    private String name;
    private String email;
    private String avatar;
    private String phone;
    private String created_at;
    private String updated_at;
    private String sex;
    private String im_token;
    public String status;
    private String isFollowing;
    public BaseDto1 real_name;//实名认证
    public BaseDto1 seller;
    public BalanceDto wallet;
//    "is_realname": false,
//            "is_shop": false,
//            "is_live": false,
//            "apply_check_status": 0,
//            "is_liveing": false
    public boolean is_realname;
    public boolean is_shop;
    public boolean is_liveing;
    public int apply_check_status;
    public String live_reward;
    public String withdraw;
    public boolean is_live;

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

    public String getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(String isFollowing) {
        this.isFollowing = isFollowing;
    }
}
