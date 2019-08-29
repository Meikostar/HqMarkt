package com.hqmy.market.bean;

/**
 * 图片验证码返回类
 * Created by dahai on 2018/12/05.
 */
public class CaptchaImgDto {
    private boolean sensitive;
    private String key;
    private String img;

    public boolean isSensitive() {
        return sensitive;
    }

    public void setSensitive(boolean sensitive) {
        this.sensitive = sensitive;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
