package com.hqmy.market.bean;

import java.io.Serializable;

/*
 * Created by rzb on 2019/6/22.
 */
public class FriendUserDto implements Serializable {

    private FriendUserInfoDto data;

    public FriendUserInfoDto getData() {
        return data;
    }

    public void setData(FriendUserInfoDto data) {
        this.data = data;
    }
}
