package com.hqmy.market.type;

/**
 * <p>
 * 性别 - 类型
 * Created by dahai on 2018/12/06.
 */
public enum SexType {
    NAN("男", 1), NV("女", 2);

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

    private SexType(String value, int type) {
        this.value = value;
        this.type = type;
    }
}
