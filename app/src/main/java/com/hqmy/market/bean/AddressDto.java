package com.hqmy.market.bean;

import java.io.Serializable;

public class AddressDto implements Serializable {
    private long id;
    private String name;
    private String mobile;
    private String area;
    private String area_ids;
    private String detail;
    private String is_default;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea_ids() {
        return area_ids;
    }

    public void setArea_ids(String area_ids) {
        this.area_ids = area_ids;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }
}
