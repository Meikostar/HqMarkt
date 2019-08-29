package com.hqmy.market.bean;

import com.hqmy.market.http.request.BaseRequestModel;

/**
 * 请求参数实体类
 * <p>
 * 发送短信验证码
 * Created by dahai on 2018/12/06.
 */
public class GetSmsCode extends BaseRequestModel {
    private String captcha_key;
    private String captcha_code;
    private String phone;
    private int type; // 类型详细 enum SmsType

    public String getCaptcha_key() {
        return captcha_key;
    }

    public void setCaptcha_key(String captcha_key) {
        this.captcha_key = captcha_key;
    }

    public String getCaptcha_code() {
        return captcha_code;
    }

    public void setCaptcha_code(String captcha_code) {
        this.captcha_code = captcha_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
