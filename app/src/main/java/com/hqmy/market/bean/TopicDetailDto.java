package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/7/11.
 * "id": 30,
 "user_id": 6,
 "title": null,
 "type": 1,
 "cover": "image\/201907\/11\/SK80vLl58mMUqsxe9xGuSY0Ie41UF3R3fHb9021S.jpeg",
 "img": ["image\/201907\/11\/SK80vLl58mMUqsxe9xGuSY0Ie41UF3R3fHb9021S.jpeg", "image\/201907\/11\/L9nRQuIjF7z0QoNC2BpSfSbpJEBW29trVa62Qead.jpeg"],
 "video": null,
 "content": "\u6211\u6765\u4e86\uff0c\u6211\u6d4b\u8bd5\u4e0b",
 "created_at": "2019-07-11 14:18:18",
 "area_id": 1939,
 "updated_at": "2019-07-11 18:36:34",
 "address": "\u6df1\u5733",
 "shares": 0,
 "click": 2,
 "tag_list": ["\u7231\u5fc3\u4f01\u4e1a"],
 "isFavorited": false,
 "isLiked": false,
 "favoriters_count": 0,
 "comments_count": 0,
 "user": {
 "data": {
 "id": 6,
 "name": "\u6d4b\u8bd53",
 "avatar": "avatar\/201906\/17\/ogkx49OkEZ4R0aNezWlP7cHUL2mRJ2UF7iWYTHKG.jpeg",
 "phone": "135****5678",
 "sex": 0,
 "im_token": "tlw\/hL+sVvQTVldySjQ5L3PEJB7oyCES\/IvwSmfFA7J+xLhWxhjvLcZiAu4h2pAnNbIvMEg\/HIBP6hzfEcd\/2w==",
 "created_at": "2019-06-05 10:35:36",
 "isFollowing": false
 }
 },
 "comments": {
 "data": []
 }
 *
 */
public class TopicDetailDto implements Serializable {
    private String id;
    private String user_id;
    private String title;
    private String type;
    private String cover;
    private List<String> img;
    private String video;
    private String content;
    private String created_at;
    private String area_id;
    private String updated_at;
    private String address;
    private String shares;
    private String click;
    private List<String> tag_list;
    private String isFavorited;
    private String isLiked;
    private String favoriters_count;
    private String comments_count;
    private UserDto user;
    private CommentInfoDto comments;

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

    public String getIsFavorited() {
        return isFavorited;
    }

    public void setIsFavorited(String isFavorited) {
        this.isFavorited = isFavorited;
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

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public CommentInfoDto getComments() {
        return comments;
    }

    public void setComments(CommentInfoDto comments) {
        this.comments = comments;
    }
}
