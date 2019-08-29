package com.hqmy.market.bean;

import java.util.List;

/**
 * Created by lihaoqi on 2019/1/28.
 */

public class CommodityDetailListDto {

    private int total;

    private List<CommodityDetailInfoDto> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CommodityDetailInfoDto> getRows() {
        return rows;
    }

    public void setRows(List<CommodityDetailInfoDto> rows) {
        this.rows = rows;
    }
}
