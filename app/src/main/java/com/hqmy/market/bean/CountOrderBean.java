package com.hqmy.market.bean;

public class CountOrderBean {
    private String cancelled;
    private String closed;
    private String completed;
    private String paid; //待发货
    private String shipped;//待评价
    private String shipping;//待收货
    private String created;//待付款

    public String getCancelled() {
        return cancelled;
    }

    public String getClosed() {
        return closed;
    }

    public String getCompleted() {
        return completed;
    }

    public String getPaid() {
        return paid;
    }

    public String getShipped() {
        return shipped;
    }

    public String getShipping() {
        return shipping;
    }

    public String getCreated() {
        return created;
    }
}
