package com.hqmy.market.bean;

import java.io.Serializable;

public class DeMemberDto implements Serializable {
    int level;
    String level_str;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevel_str() {
        return level_str;
    }

    public void setLevel_str(String level_str) {
        this.level_str = level_str;
    }
}
