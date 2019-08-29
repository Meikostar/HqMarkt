package com.hqmy.market.type;

/**
 * <p>
 * 发送短信验证码 - 类型
 * Created by dahai on 2018/12/06.
 */
public enum SmsType {
    REGISTER("注册", 1),
    FIND_PASSWORD("找回密码", 2), CHANGE_PHONE("更改手机", 3), ADD_CARD("添加卡", 4),
    SET_PAY_PASSWORD("设置支付密码", 5),
    ADD_BAND_CARD("添加银行卡", 4);

    private int type;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private SmsType(String value, int type) {
        this.value = value;
        this.type = type;
    }
}
