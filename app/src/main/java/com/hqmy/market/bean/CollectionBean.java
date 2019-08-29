package com.hqmy.market.bean;

import java.util.ArrayList;

public class CollectionBean {
    private ArrayList<CollectionDto> data;

    private Meta meta;

    public ArrayList<CollectionDto> getData() {
        return data;
    }

    public void setData(ArrayList<CollectionDto> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
