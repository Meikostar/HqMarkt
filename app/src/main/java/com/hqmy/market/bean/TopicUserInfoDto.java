package com.hqmy.market.bean;

import java.io.Serializable;

public class TopicUserInfoDto implements Serializable {

    private String id;
    private String name;
    private String avatar;
    private String phone;
    private String sex;
    private String im_token;
    private boolean isFollowing;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPhone() {
        return phone;
    }

    public String getSex() {
        return sex;
    }

    public String getIm_token() {
        return im_token;
    }

    public boolean isFollowing() {
        return isFollowing;
    }
}
