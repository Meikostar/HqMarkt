package com.hqmy.market.bean;

public class OrderBean {
    private String id;
    public String product_total;
    public String pay_total;
    public String count;
    MyOrderShopDto shop;
    MyorderItemsDto items;
    public String getId() {
        return id;
    }

    public MyOrderShopDto getShop() {
        return shop;
    }

    public MyorderItemsDto getItems() {
        return items;
    }
}
