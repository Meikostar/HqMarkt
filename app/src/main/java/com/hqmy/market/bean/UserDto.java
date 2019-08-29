package com.hqmy.market.bean;

import java.io.Serializable;

/**
 * Created by rzb on 2019/7/11.
 */

public class UserDto implements Serializable {
    private UserInfoDto data;

    public UserInfoDto getData() {
        return data;
    }

    public void setData(UserInfoDto data) {
        this.data = data;
    }
}
