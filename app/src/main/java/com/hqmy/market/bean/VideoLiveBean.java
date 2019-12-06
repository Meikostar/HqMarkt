package com.hqmy.market.bean;

import java.util.List;

public class VideoLiveBean {
    private String               id;
    private String               title;

    private String               is_recommended;
    private String               created_at;
    private String               live_title;
    private String               stream_id;
    private String               rtmp_publish_url;
    private String               rtmp_play_url;
    private String               hls_play_url;
    private String               hdl_play_url;
    private String               snap_shot_img;
    private String               likers_count;
    public String               play_url;
    public String               products_count;
    public String               chatter_total;
    public String               images;
    public String               end_at;
    public String               play_count;
    private VideoLiveUserInfo    user;
    public  RoomInfo             room;
    public  RoomInfo             apply;
    public  List<MyOrderItemDto> products;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getIs_recommended() {
        return is_recommended;
    }

    public void setIs_recommended(String is_recommended) {
        this.is_recommended = is_recommended;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLive_title() {
        return live_title;
    }

    public void setLive_title(String live_title) {
        this.live_title = live_title;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getRtmp_publish_url() {
        return rtmp_publish_url;
    }

    public void setRtmp_publish_url(String rtmp_publish_url) {
        this.rtmp_publish_url = rtmp_publish_url;
    }

    public String getRtmp_play_url() {
        return rtmp_play_url;
    }

    public void setRtmp_play_url(String rtmp_play_url) {
        this.rtmp_play_url = rtmp_play_url;
    }

    public String getHls_play_url() {
        return hls_play_url;
    }

    public void setHls_play_url(String hls_play_url) {
        this.hls_play_url = hls_play_url;
    }

    public String getHdl_play_url() {
        return hdl_play_url;
    }

    public void setHdl_play_url(String hdl_play_url) {
        this.hdl_play_url = hdl_play_url;
    }

    public String getSnap_shot_img() {
        return snap_shot_img;
    }

    public void setSnap_shot_img(String snap_shot_img) {
        this.snap_shot_img = snap_shot_img;
    }

    public String getLikers_count() {
        return likers_count;
    }

    public void setLikers_count(String likers_count) {
        this.likers_count = likers_count;
    }

    public String getPlay_count() {
        return play_count;
    }

    public void setPlay_count(String play_count) {
        this.play_count = play_count;
    }

    public VideoLiveUserInfo getUser() {
        return user;
    }

    public void setUser(VideoLiveUserInfo user) {
        this.user = user;
    }

    public RoomInfo getRoom() {
        return room;
    }

    public void setRoom(RoomInfo room) {
        this.room = room;
    }

    public String getChatter_total() {
        return chatter_total;
    }

    public void setChatter_total(String chatter_total) {
        this.chatter_total = chatter_total;
    }
}
