package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.List;

public class TopicListItemDto implements Serializable {
    private String id;
    private String user_id;
    public String title;
    private String type;
    private String cover;
    private List<String> img;
    private List<String> imgs;
    private String video;
    private String content;
    private String created_at;
    private String area_id;
    private String updated_at;
    private String address;
    private String shares;
    private String click;
    private List<String> tag_list;
    public List<String> labels;
    private String isLiked;
    private String favoriters_count;
    private String comments_count;
    private String likers_count;
    public String market_price;
    public String icon;
    public boolean isChoose;
    private TopicUserDto user;
    public TopicListItemDto brand;
    public TopicListItemDto data;
    public TopicListItemDto category;

    private boolean flag2;
    private ShopInfo shop;
    private String score;
    private String price;
    private Boolean isFavorited;
    String favoritersCount;
    String likersCount;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public List<String> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<String> tag_list) {
        this.tag_list = tag_list;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getFavoriters_count() {
        return favoriters_count;
    }

    public void setFavoriters_count(String favoriters_count) {
        this.favoriters_count = favoriters_count;
    }

    public String getComments_count() {
        return comments_count;
    }

    public void setComments_count(String comments_count) {
        this.comments_count = comments_count;
    }

    public String getLikers_count() {
        return likers_count;
    }

    public void setLikers_count(String likers_count) {
        this.likers_count = likers_count;
    }

    public TopicUserDto getUser() {
        return user;
    }

    public void setUser(TopicUserDto user) {
        this.user = user;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public boolean isFlag2() {
        return flag2;
    }

    public ShopInfo getShop() {
        return shop;
    }

    public String getScore() {
        return score;
    }

    public String getPrice() {
        return price;
    }

    public Boolean getFavorited() {
        return isFavorited;
    }

    public String getFavoritersCount() {
        return favoritersCount;
    }

    public void setFavoritersCount(String favoritersCount) {
        this.favoritersCount = favoritersCount;
    }

    public String getLikersCount() {
        return likersCount;
    }

    public void setLikersCount(String likersCount) {
        this.likersCount = likersCount;
    }
}
