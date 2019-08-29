package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/7/11.
 */
public class CashCouponUsersDto implements Serializable {
    private List<CashCouponUserInfoDto> data;

    public List<CashCouponUserInfoDto> getData() {
        return data;
    }

    public void setData(List<CashCouponUserInfoDto> data) {
        this.data = data;
    }
}
