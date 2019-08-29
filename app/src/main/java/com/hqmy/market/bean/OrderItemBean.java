package com.hqmy.market.bean;

import java.util.HashMap;

public class OrderItemBean {
    private String id;
    private String shop_id;
    private String mall_order_id;
    private String product_id;
    private String qty;
    private String title;
    private String price;
    private String score;
    private String sku_id;
    private HashMap<String,String> options;

    public String getId() {
        return id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public String getMall_order_id() {
        return mall_order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getQty() {
        return qty;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getScore() {
        return score;
    }

    public String getSku_id() {
        return sku_id;
    }

    public HashMap<String, String> getOptions() {
        return options;
    }
}
