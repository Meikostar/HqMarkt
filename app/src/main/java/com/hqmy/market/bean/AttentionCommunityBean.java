package com.hqmy.market.bean;

public class AttentionCommunityBean {

    private String avatar;//   avatar/201905/29/nzaDWRGHtLrNsT6BnASD29gXHdPy3iEqWaXEzmB7.jpeg
    private int id;//   3
    private boolean isFollowing;//           true
    private String name;//   春风十里不如你
    private String phone;//   188****1225
    private int sex;//   2
    private String logo;
    private String shop_name;
    private String       lat;
    private String       lng;
    public String type;
    public String followers_count;
    public String new_goods_count;

    public String getAvatar() {
        return avatar;
    }

    public int getId() {
        return id;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getSex() {
        return sex;
    }

    public String getLogo() {
        return logo;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getType() {
        return type;
    }
}
