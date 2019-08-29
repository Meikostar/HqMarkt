package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class PublishInfo implements Serializable {
    private String id;
    private String cover;
    private String title;
    private String score;
    private String created_at;
    private String stock;
    private String content;
    private ArrayList<String> imgs;
    private ExtBean ext;
    private CategoryInfo category;
    private boolean is_new;
    private String category_id;
    private boolean on_sale;//true 上架 false 下架

    public String getId() {
        return id;
    }

    public String getCover() {
        return cover;
    }

    public String getTitle() {
        return title;
    }

    public String getScore() {
        return score;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getStock() {
        return stock;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public ExtBean getExt() {
        return ext;
    }

    public CategoryInfo getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public boolean isIs_new() {
        return is_new;
    }

    public String getCategory_id() {
        return category_id;
    }

    public boolean isOn_sale() {
        return on_sale;
    }
}
