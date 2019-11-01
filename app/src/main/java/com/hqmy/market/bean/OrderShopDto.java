package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/6/25.
 */
public class OrderShopDto implements Serializable {
    private List<OrderProductDto> products;
    private String shop_id;
    private OrderFreightDto freight;
    private boolean available_coupon;
    private String coupon_discount;
    private String total;
    public String shop_name;
    public String product_total;
    private String score;

    public List<OrderProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductDto> products) {
        this.products = products;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public boolean isAvailable_coupon() {
        return available_coupon;
    }

    public String getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(String coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public OrderFreightDto getFreight() {
        return freight;
    }

    public void setFreight(OrderFreightDto freight) {
        this.freight = freight;
    }
}
