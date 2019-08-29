package com.hqmy.market.bean;

public class MyOrderExtraDto {
    String refund_type;
    String refund_reason;
    String de_point ;
    String love_point ;

    public String getRefund_type() {
        return refund_type;
    }

    public void setRefund_type(String refund_type) {
        this.refund_type = refund_type;
    }

    public String getRefund_reason() {
        return refund_reason;
    }

    public void setRefund_reason(String refund_reason) {
        this.refund_reason = refund_reason;
    }

    public String getDe_point() {
        return de_point;
    }

    public void setDe_point(String de_point) {
        this.de_point = de_point;
    }

    public String getLove_point() {
        return love_point;
    }

    public void setLove_point(String love_point) {
        this.love_point = love_point;
    }
}
