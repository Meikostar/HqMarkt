package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/7/11.
 */
public class CommentDto implements Serializable {
    private String id;
    private String user_id;
    private String commented_type;
    private String commented_id;
    private String comment;
//  private RateDto rate;
    private String score;
    private List<String> images;
    private String created_at;
    private String flag;
    private ExtDto ext;
    private String likers_count;
    private String is_liked;
    private String comments_count;
    private String share_count;
    private UserDto user;

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

    public String getCommented_type() {
        return commented_type;
    }

    public void setCommented_type(String commented_type) {
        this.commented_type = commented_type;
    }

    public String getCommented_id() {
        return commented_id;
    }

    public void setCommented_id(String commented_id) {
        this.commented_id = commented_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
//
//    public String getRate() {
//        return rate;
//    }
//
//    public void setRate(String rate) {
//        this.rate = rate;
//    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<String> getImages() {
        return images;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public ExtDto getExt() {
        return ext;
    }

    public void setExt(ExtDto ext) {
        this.ext = ext;
    }

    public String getLikers_count() {
        return likers_count;
    }

    public void setLikers_count(String likers_count) {
        this.likers_count = likers_count;
    }

    public String getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(String is_liked) {
        this.is_liked = is_liked;
    }

    public String getComments_count() {
        return comments_count;
    }

    public void setComments_count(String comments_count) {
        this.comments_count = comments_count;
    }

    public String getShare_count() {
        return share_count;
    }

    public void setShare_count(String share_count) {
        this.share_count = share_count;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
