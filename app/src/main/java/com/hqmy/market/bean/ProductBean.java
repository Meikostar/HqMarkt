package com.hqmy.market.bean;

import java.util.List;

public class ProductBean {
    String id;
    String title;
    String cover;
    Double price;
    boolean flag2;
    List<String> imgs;
    boolean on_sale;
    boolean is_new;
    boolean is_hot;
    boolean is_recommend;
    String type;
    public String getId() {
        return id;
    }

    public String getCover() {
        return cover;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public String getTitle() {
        return title;
    }

    public boolean isOn_sale() {
        return on_sale;
    }

    public boolean isIs_new() {
        return is_new;
    }

    public boolean isIs_hot() {
        return is_hot;
    }

    public boolean isIs_recommend() {
        return is_recommend;
    }

    public Double getPrice() {
        return price;
    }

    public boolean isFlag2() {
        return flag2;
    }

    public String getType() {
        return type;
    }
}
