package com.hqmy.market.bean;

public class VideoLiveUserInfo {


    private VideoLiveUserBean data;
    public VideoLiveUserBean is_followed;
    public VideoLiveUserBean followers_count;
    public String name;
    public String avatar;
    public String id;

    public VideoLiveUserBean getData() {
        return data;
    }

    public void setData(VideoLiveUserBean data) {
        this.data = data;
    }
}
