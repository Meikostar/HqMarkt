package com.hqmy.market.bean;

import java.io.Serializable;

/*
 * Created by rzb on 2019/6/22.
 */
public class FriendPageDto implements Serializable {
    private FriendUserInfoDto user;

    public FriendUserInfoDto getUser() {
        return user;
    }

    public void setUser(FriendUserInfoDto user) {
        this.user = user;
    }
}
