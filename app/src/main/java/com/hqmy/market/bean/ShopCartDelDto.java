package com.hqmy.market.bean;

import java.io.Serializable;

/**
 * Created by rzb on 2019/6/18.
 */
public class ShopCartDelDto implements Serializable {
     private ShopCartInfoDto data;

    public ShopCartInfoDto getData() {
        return data;
    }

    public void setData(ShopCartInfoDto data) {
        this.data = data;
    }
}
