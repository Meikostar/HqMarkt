package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.List;

public class BrandListBean implements Serializable {
    private String          id;
    private String          headImg;
    private String          name;
    private String          ratNum;
    private List<BrandBean> bbLsit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRatNum() {
        return ratNum;
    }

    public void setRatNum(String ratNum) {
        this.ratNum = ratNum;
    }

    public List<BrandBean> getBbLsit() {
        return bbLsit;
    }

    public void setBbLsit(List<BrandBean> bbLsit) {
        this.bbLsit = bbLsit;
    }
}
