package com.hqmy.market.bean;

/**
 * 分类列表
 * Created by winder on 2019/7/9.
 */

public class LiveCatesBean {
    private String id;
    private String cat_name;
    private String sorts;
    private Videos videos;
    private RoomInfo room;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getSorts() {
        return sorts;
    }

    public void setSorts(String sorts) {
        this.sorts = sorts;
    }

    public Videos getVideos() {
        return videos;
    }

    public void setVideos(Videos videos) {
        this.videos = videos;
    }

    public RoomInfo getRoom() {
        return room;
    }
}
