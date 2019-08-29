package com.hqmy.market.bean;

import java.util.List;

public class CourseBean {
    private String id;
    private String introduce;
    private String author;
    private String video;
    private List<PPT> ppt;
    private List<String> audio;
    private Boolean paid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public List<PPT> getPpt() {
        return ppt;
    }

    public void setPpt(List<PPT> ppt) {
        this.ppt = ppt;
    }

    public List<String> getAudio() {
        return audio;
    }

    public void setAudio(List<String> audio) {
        this.audio = audio;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }
}
