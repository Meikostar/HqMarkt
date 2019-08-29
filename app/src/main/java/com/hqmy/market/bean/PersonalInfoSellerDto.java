package com.hqmy.market.bean;

import java.io.Serializable;

public class PersonalInfoSellerDto implements Serializable {
    //属性缺少，后期补充
    String user_id;
    String name;
    String shop_name;
    String type;
    int status; //1:待审核，2:审核成功，3:审核拒绝，4:禁用

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
