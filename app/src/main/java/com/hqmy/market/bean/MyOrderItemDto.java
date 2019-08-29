package com.hqmy.market.bean;

import java.util.Map;

public class MyOrderItemDto{

    String                id;
    String                type;
    String                title;
    String                cover_image;
    String                shop_id;
    String                order_id;
    String                product_id;
    String                sku_id;
    String                qty;
    String                price;
    String                rating;
    String                review;
    String                score;
    MyOrderItemExtraDto   extra;
    DataDto<ShoppableDto> shoppable;
    ProductInfo product;
    Map<String,String> options;
    Commented comment;
    String  cover;
    LearnRecordObject  object;

    CouponCodeDataInfo vcode;
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCover_image() {
        return cover_image;
    }

    public String getShop_id() {
        return shop_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getSku_id() {
        return sku_id;
    }

    public String getQty() {
        return qty;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public MyOrderItemExtraDto getExtra() {
        return extra;
    }

    public DataDto<ShoppableDto> getShoppable() {
        return shoppable;
    }

    public ProductInfo getProduct() {
        return product;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public Commented getComment() {
        return comment;
    }

    public String getScore() {
        return score;
    }

    public String getCover() {
        return cover;
    }

    public LearnRecordObject getObject() {
        return object;
    }

    public String getType() {
        return type;
    }

    public CouponCodeDataInfo getVcode() {
        return vcode;
    }
}