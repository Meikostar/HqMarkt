package com.hqmy.market.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HotSearch {
    private List<String> consume_index;
    private List<String> gc;
    private List<String> lm;
    private List<String> ax;
    private List<String> st;
    private List<String> course;
    private List<String> live;

    @SerializedName("default")
    public List<String> mdefault;

    public List<String> getLive() {
        return live;
    }

    public void setLive(List<String> live) {
        this.live = live;
    }

    public List<String> getCourse() {
        return course;
    }

    public void setCourse(List<String> course) {
        this.course = course;
    }

    public List<String> getConsume_index() {
        return consume_index;
    }

    public void setConsume_index(List<String> consume_index) {
        this.consume_index = consume_index;
    }

    public List<String> getGc() {
        return gc;
    }

    public void setGc(List<String> gc) {
        this.gc = gc;
    }

    public List<String> getLm() {
        return lm;
    }

    public void setLm(List<String> lm) {
        this.lm = lm;
    }

    public List<String> getAx() {
        return ax;
    }

    public void setAx(List<String> ax) {
        this.ax = ax;
    }

    public List<String> getSt() {
        return st;
    }

    public void setSt(List<String> st) {
        this.st = st;
    }
}
