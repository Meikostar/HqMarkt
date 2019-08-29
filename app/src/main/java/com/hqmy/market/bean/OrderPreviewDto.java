package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/6/24
 */
public class OrderPreviewDto implements Serializable {
    private List<OrderShopDto>  shops;
    private String total;
    private String score;

    public List<OrderShopDto> getShops() {
        return shops;
    }

    public void setShops(List<OrderShopDto> shops) {
        this.shops = shops;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
