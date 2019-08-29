package com.hqmy.market.bean;

import com.google.gson.annotations.SerializedName;

public class RateBean {
    float description;
    float shipment;
    float service;
    @SerializedName("default")
    float defaultName;

    public RateBean(float description, float shipment, float service, float defaultName) {
        this.description = description;
        this.shipment = shipment;
        this.service = service;
        this.defaultName = defaultName;
    }
}
