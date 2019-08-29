package com.hqmy.market.bean;

import java.io.Serializable;

public class ExtBean implements Serializable {
    private String city;
    private String district;
    private String province;

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getProvince() {
        return province;
    }
}
