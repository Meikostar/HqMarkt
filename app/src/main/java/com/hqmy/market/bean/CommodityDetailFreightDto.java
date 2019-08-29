package com.hqmy.market.bean;

import java.io.Serializable;

/**
 * 商品详细 邮费
 * Created by rzb on 2018/12/18.
 */

public class CommodityDetailFreightDto implements Serializable {
    private String freight;
    private String is_free;

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getIs_free() {
        return is_free;
    }

    public void setIs_free(String is_free) {
        this.is_free = is_free;
    }
}
