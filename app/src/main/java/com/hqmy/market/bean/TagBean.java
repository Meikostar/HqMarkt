package com.hqmy.market.bean;
import java.io.Serializable;
import java.util.List;

public class TagBean implements Serializable {
    private List<String> news;
    private List<String> post;
    private List<String> st;
    private List<String> st_services;

    public List<String> getNews() {
        return news;
    }

    public void setNews(List<String> news) {
        this.news = news;
    }

    public List<String> getPost() {
        return post;
    }

    public void setPost(List<String> post) {
        this.post = post;
    }

    public List<String> getSt() {
        return st;
    }

    public void setSt(List<String> st) {
        this.st = st;
    }

    public List<String> getSt_services() {
        return st_services;
    }

    public void setSt_services(List<String> st_services) {
        this.st_services = st_services;
    }
}
