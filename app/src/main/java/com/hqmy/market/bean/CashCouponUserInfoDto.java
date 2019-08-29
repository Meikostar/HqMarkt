package com.hqmy.market.bean;

import java.io.Serializable;

/**
 * Created by rzb on 2019/7/11.
 */
public class CashCouponUserInfoDto implements Serializable {
    private String id;
    private String cash_coupon_id;
    private String user_id;
    private String total;
    private String status;
    private String created_at;
    private String updated_at;
    private UserDto user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCash_coupon_id() {
        return cash_coupon_id;
    }

    public void setCash_coupon_id(String cash_coupon_id) {
        this.cash_coupon_id = cash_coupon_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
