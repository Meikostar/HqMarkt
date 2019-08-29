package com.hqmy.market.http.request;

/**
 * 请求注册,登录
 * * Created by dahai on 2018/12/07.
 */
public class UserRegister extends BaseRequestModel {
    private String phone;
    private String code;
    private String password;
    private String include;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }
}
