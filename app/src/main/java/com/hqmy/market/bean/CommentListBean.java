package com.hqmy.market.bean;

import java.util.List;

public class CommentListBean {
    private String       comment;//        abc deft
    private int          commented_id;//            3
    private String       commented_type;//        	Modules\Project\Entities\Post
    private String       created_at;//            2019-05-30 15:26:42
    private String       flag;//        1559201202
    private int          id;//    9
    private int          is_liked;//        0
    private int          likers_count;//            0
    private CommentUser  user;//        Object
    private int          user_id;//        3
    private String       score;
    private List<String> images;
    private String       comments_count;
    private String       share_count;


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCommented_id() {
        return commented_id;
    }

    public void setCommented_id(int commented_id) {
        this.commented_id = commented_id;
    }

    public String getCommented_type() {
        return commented_type;
    }

    public void setCommented_type(String commented_type) {
        this.commented_type = commented_type;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(int is_liked) {
        this.is_liked = is_liked;
    }

    public int getLikers_count() {
        return likers_count;
    }

    public void setLikers_count(int likers_count) {
        this.likers_count = likers_count;
    }

    public CommentUser getUser() {
        return user;
    }

    public void setUser(CommentUser user) {
        this.user = user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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
}
