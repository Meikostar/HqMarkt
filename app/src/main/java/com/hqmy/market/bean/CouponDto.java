package com.hqmy.market.bean;

public class CouponDto {
    private String id;
    private String shop_id;
    private String user_id;
    private String type;
    private Boolean used;
    private String mall_coupon_id;
    private String start_at;
    private String expire_at;
    private CouponInfo coupon;

    public CouponInfo getCoupon() {
        return coupon;
    }

    public String getId() {
        return id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getType() {
        return type;
    }

    public Boolean getUsed() {
        return used;
    }

    public String getMall_coupon_id() {
        return mall_coupon_id;
    }

    public String getStart_at() {
        return start_at;
    }

    public String getExpire_at() {
        return expire_at;
    }
}
