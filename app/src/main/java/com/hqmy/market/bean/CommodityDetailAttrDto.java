package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 商品详情 属性
 * Created by rzb on 2019/6/18.
 */
public class CommodityDetailAttrDto implements Serializable {
    private List<CommodityDetailAttrItemDto> data;

    public List<CommodityDetailAttrItemDto> getData() {
        return data;
    }

    public void setData(List<CommodityDetailAttrItemDto> data) {
        this.data = data;
    }
}
