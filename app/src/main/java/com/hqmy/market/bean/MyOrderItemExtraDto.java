package com.hqmy.market.bean;

public class MyOrderItemExtraDto {
    String price;
    String sku_title;
    String level1_price;
    String level2_price;
    String user_price_key;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSku_title() {
        return sku_title;
    }

    public void setSku_title(String sku_title) {
        this.sku_title = sku_title;
    }

    public String getLevel1_price() {
        return level1_price;
    }

    public void setLevel1_price(String level1_price) {
        this.level1_price = level1_price;
    }

    public String getLevel2_price() {
        return level2_price;
    }

    public void setLevel2_price(String level2_price) {
        this.level2_price = level2_price;
    }

    public String getUser_price_key() {
        return user_price_key;
    }

    public void setUser_price_key(String user_price_key) {
        this.user_price_key = user_price_key;
    }
}
