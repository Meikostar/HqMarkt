package com.hqmy.market.bean;

public class CouponBean {
    private String id;
    private String shop_id;
    private String name;
    private String description;
    private String coupon_type;
    private String price;
    private String discount_type;
    private String discount_value;
    private String total;
    private int limit;
    private String day_type;
    private String show_start;
    private String show_end;
    private String days;
    private int user_coupons_count;
    private String created_at;

    public String getId() {
        return id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public String getDiscount_value() {
        return discount_value;
    }

    public String getTotal() {
        return total;
    }

    public int getLimit() {
        return limit;
    }

    public String getDay_type() {
        return day_type;
    }

    public String getShow_start() {
        return show_start;
    }

    public String getShow_end() {
        return show_end;
    }

    public String getDays() {
        return days;
    }

    public int getUser_coupons_count() {
        return user_coupons_count;
    }

    public String getCreated_at() {
        return created_at;
    }
}
