package com.hqmy.market.bean;

import java.io.Serializable;

/**
 * Created by rzb on 2019/6/13.
 */
public class RedPacketInfoDto implements Serializable {
    private String id;
    private String user_id;
    private String type;
    private String wallet_type;
    private String total;
    private String receive_total;
    private String count;
    private String receive_count;
    private String say;
    private String created_at;
    private UserDto user;
    private CashCouponUsersDto cash_coupon_users;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWallet_type() {
        return wallet_type;
    }

    public void setWallet_type(String wallet_type) {
        this.wallet_type = wallet_type;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getReceive_total() {
        return receive_total;
    }

    public void setReceive_total(String receive_total) {
        this.receive_total = receive_total;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getReceive_count() {
        return receive_count;
    }

    public void setReceive_count(String receive_count) {
        this.receive_count = receive_count;
    }

    public String getSay() {
        return say;
    }

    public void setSay(String say) {
        this.say = say;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public CashCouponUsersDto getCash_coupon_users() {
        return cash_coupon_users;
    }

    public void setCash_coupon_users(CashCouponUsersDto cash_coupon_users) {
        this.cash_coupon_users = cash_coupon_users;
    }
}
