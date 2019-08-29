package com.hqmy.market.bean;

import java.io.Serializable;

public class DataDto<T> implements Serializable {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
