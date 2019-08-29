package com.hqmy.market.bean;

import java.io.Serializable;

/**
 * 商品颜色，规格等属性
 * Created by rzb on 2019/6/13
 */
public class GoodsAttrItemDto implements Serializable {
    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
