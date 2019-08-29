package com.hqmy.market.bean;

public class MyOrderShipDataDto {
    String express_id;
    String express_no;
    Object  delivered_at;
    String express_code;
    String express_name;

    public String getExpress_id() {
        return express_id;
    }

    public void setExpress_id(String express_id) {
        this.express_id = express_id;
    }

    public String getExpress_no() {
        return express_no;
    }

    public void setExpress_no(String express_no) {
        this.express_no = express_no;
    }


    public String getExpress_code() {
        return express_code;
    }

    public void setExpress_code(String express_code) {
        this.express_code = express_code;
    }

    public String getExpress_name() {
        return express_name;
    }

    public void setExpress_name(String express_name) {
        this.express_name = express_name;
    }

    public Object getDelivered_at() {
        return delivered_at;
    }

    public void setDelivered_at(Object delivered_at) {
        this.delivered_at = delivered_at;
    }
}
