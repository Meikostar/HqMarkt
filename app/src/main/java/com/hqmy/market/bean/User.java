package com.hqmy.market.bean;

public class User {

    private String  avatar;//   avatar/201905/29/nzaDWRGHtLrNsT6BnASD29gXHdPy3iEqWaXEzmB7.jpeg
    private int     id;//   3
    private boolean isFollowing;//       true
    private String  name;//   春风十里不如你
    private String  phone;//   188****1225
    private int     sex;//   2

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
