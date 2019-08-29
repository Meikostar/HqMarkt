package com.hqmy.market.bean;

import java.util.List;

public class MyorderItemsDto{
  List<MyOrderItemDto> data;

    public List<MyOrderItemDto> getData() {
        return data;
    }

    public void setData(List<MyOrderItemDto> data) {
        this.data = data;
    }
}
