package com.hqmy.market.bean;

import java.util.ArrayList;

/**
 * 动态
 */
public class DynamicBean {
    private String    address;// 广东省 深圳市 南山区 高新南一道6号 TCL大厦
    private int       area_id;// 1939
    private int       click;// 0
    private int       comments_count;//         3
    private String    content;// 有美人兮，见之不忘。一日不见兮，思之如狂。凤飞翱翔兮，四海求凰。无奈佳人兮，不在东墙。将琴代语兮，聊写衷肠。何时见许兮，慰我彷徨。愿言配德兮，携手相将。不得於飞兮，使我沦亡。
    private String    cover;// image/201905/29/tHMza0TOgXnf3c4k6PQHunRcC7jSDLprIrDpNYSe.jpeg
    private String    created_at;//     2019-05-29 10:21:43
    private int       favoriters_count;//	        2
    private int       id;//     3
    private ArrayList img;// Array
    private boolean   isFavorited;//         true
    private boolean   isLiked;// true
    private int       likers_count;//         	1
    private int       shares;//	0
    private ArrayList tag_list;//     Array
    private int       type;// 1
    private String    updated_at;//     2019-05-29 10:21:43
    private int       user_id;// 	1

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getFavoriters_count() {
        return favoriters_count;
    }

    public void setFavoriters_count(int favoriters_count) {
        this.favoriters_count = favoriters_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList getImg() {
        return img;
    }

    public void setImg(ArrayList img) {
        this.img = img;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getLikers_count() {
        return likers_count;
    }

    public void setLikers_count(int likers_count) {
        this.likers_count = likers_count;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public ArrayList getTag_list() {
        return tag_list;
    }

    public void setTag_list(ArrayList tag_list) {
        this.tag_list = tag_list;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
