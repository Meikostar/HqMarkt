package com.hqmy.market.db.bean;

/**
 * <p>
 * 产品列表 类型
 * Created by dahai on 2018/12/06.
 */
public enum ProductListType {
    synthesize("综合", 1), sales("销量", 2), priceLitre("价格+", 3), priceDrop("价格+", 4),screening("筛选", 5);

    private int type;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private ProductListType(String value, int type) {
        this.value = value;
        this.type = type;
    }
}
