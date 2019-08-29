package com.hqmy.market.bean;

/**
 * Created by winder on 2019/7/13.
 */

public class LiveVideoInfo {
    //开播推流
    private String id;
    String rtmp_publish_url;
    PersonalInfoDto user;
    RoomInfo room;
    public String getRtmp_publish_url() {
        return rtmp_publish_url;
    }

    public PersonalInfoDto getUser() {
        return user;
    }

    public RoomInfo getRoom() {
        return room;
    }

    public String getId() {
        return id;
    }
}
