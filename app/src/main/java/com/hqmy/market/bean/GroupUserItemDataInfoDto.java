package com.hqmy.market.bean;

import java.io.Serializable;

public class GroupUserItemDataInfoDto implements Serializable{

  	private GroupUserItemDataTrueInfoDto data;

    public GroupUserItemDataTrueInfoDto getData() {
        return data;
    }

    public void setData(GroupUserItemDataTrueInfoDto data) {
        this.data = data;
    }
}
