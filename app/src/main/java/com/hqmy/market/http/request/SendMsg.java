package com.hqmy.market.http.request;

/**
 * 请求短信验证码
 * * Created by dahai on 2019/02/21.
 */
public class SendMsg extends BaseRequestModel {
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
