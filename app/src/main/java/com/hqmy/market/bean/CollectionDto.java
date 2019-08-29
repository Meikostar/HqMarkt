package com.hqmy.market.bean;

import java.util.ArrayList;

public class CollectionDto {
    String  id;
    String  title;
    String  cover_image;
    String  price;
    String  created_at;
    String  updated_at;
    boolean isSel;
    String  type;
    boolean flag2;
    String score;

    private String    address;//     广东省 深圳市 南山区 高新南一道6号 TCL大厦
    private String    area_id;//         1939
    private String    click;//         0
    private String    content;//         有美人兮，见之不忘。一日不见兮，思之如狂。凤飞翱翔兮，四海求凰。无奈佳人兮，不在东墙。将琴代语兮，聊写衷肠。何时见许兮，慰我彷徨。愿言配德兮，携手相将。不得於飞兮，使我沦亡。
    private String    cover;//         image/201905/29/tHMza0TOgXnf3c4k6PQHunRcC7jSDLprIrDpNYSe.jpeg
    private ArrayList<String> imgs;//             Array
    private Boolean    isFavorited;//                         true
    private String    isLiked;//                         false
    private String    shares;//                 0
    private ArrayList<String>    tag_list;//                 Array
    private String    user_id;////
    private String shop_id;
    private ShopInfo shop;
    private TopicUserDto user;
    private String favoriters_count;
    private String comments_count;
    private String likers_count;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCover_image() {
        return cover_image;
    }

    public String getPrice() {
        return price;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public boolean isSel() {
        return isSel;
    }

    public String getType() {
        return type;
    }

    public boolean isFlag2() {
        return flag2;
    }

    public String getScore() {
        return score;
    }

    public String getAddress() {
        return address;
    }

    public String getArea_id() {
        return area_id;
    }

    public String getClick() {
        return click;
    }

    public String getContent() {
        return content;
    }

    public String getCover() {
        return cover;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public Boolean getFavorited() {
        return isFavorited;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public String getShares() {
        return shares;
    }

    public ArrayList<String> getTag_list() {
        return tag_list;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public ShopInfo getShop() {
        return shop;
    }

    public TopicUserDto getUser() {
        return user;
    }

    public String getFavoriters_count() {
        return favoriters_count;
    }

    public String getComments_count() {
        return comments_count;
    }

    public String getLikers_count() {
        return likers_count;
    }
}
