package com.hqmy.market.bean;

import java.io.Serializable;

/**
 * 消费商品推荐
 * Created by rzb on 2019/4/20.
 */

public class GoodsPushBean implements Serializable  {
    private long id;
    private String goodsName;
    private String goodsImg;
    private String goodsPrice;
    private String appraiseTotal;
    private String appraiseRate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getAppraiseTotal() {
        return appraiseTotal;
    }

    public void setAppraiseTotal(String appraiseTotal) {
        this.appraiseTotal = appraiseTotal;
    }

    public String getAppraiseRate() {
        return appraiseRate;
    }

    public void setAppraiseRate(String appraiseRate) {
        this.appraiseRate = appraiseRate;
    }
}
