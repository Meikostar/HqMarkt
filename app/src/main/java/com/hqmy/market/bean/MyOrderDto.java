package com.hqmy.market.bean;

public class MyOrderDto{
    String id;
    String type;
    String shop_id;
    String no;
    String status_msg;
    String user_id;
    String shipping_price;
    String pay_total;
    String product_total;
   public int refund_to_wallet;
    String total;
    String count;
    MyorderItemsDto items;
    MyOrderAddressDetailDto address_detail;
    MyOrderShopDto shop;
    MyOrderShipDataDto ship_data;
    MyOrderExtraDto  extra;
    MyOrderUserSubDto user_sub;

    String total_amount;
    String remark;
    String paid_at;
    String payment_method;
    String payment_no;
    String refund_status;
    String refund_status_text;
    String refund_show_status;
    String refund_no;
    String closed;
    String reviewed;
    String ship_status;

    String created_at;
    String updated_at;
    String refund_at;
    String freight;
    String remaining_expires;
    String status;
    String test;
    Object delivered_at;
    String customer_phone;
    String order_id;
    MyOrderDtoObject object;
    RefundInfo refundInfo;
    OrderInfo order;
    String refund_money;
    String reason;
    String mall_order_sn;
    String date;
    AddressInfo address;


    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getShop_id() {
        return shop_id;
    }

    public String getNo() {
        return no;
    }

    public String getUser_id() {
        return user_id;
    }

    public MyorderItemsDto getItems() {
        return items;
    }

    public MyOrderAddressDetailDto getAddress_detail() {
        return address_detail;
    }

    public MyOrderShopDto getShop() {
        return shop;
    }

    public MyOrderShipDataDto getShip_data() {
        return ship_data;
    }

    public MyOrderExtraDto getExtra() {
        return extra;
    }

    public MyOrderUserSubDto getUser_sub() {
        return user_sub;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public String getRemark() {
        return remark;
    }

    public String getPaid_at() {
        return paid_at;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getPayment_no() {
        return payment_no;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public String getRefund_status_text() {
        return refund_status_text;
    }

    public String getRefund_show_status() {
        return refund_show_status;
    }

    public String getRefund_no() {
        return refund_no;
    }

    public String getClosed() {
        return closed;
    }

    public String getReviewed() {
        return reviewed;
    }

    public String getShip_status() {
        return ship_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getRefund_at() {
        return refund_at;
    }

    public String getFreight() {
        return freight;
    }

    public String getRemaining_expires() {
        return remaining_expires;
    }

    public String getStatus() {
        return status;
    }

    public String getTest() {
        return test;
    }

    public Object getDelivered_at() {
        return delivered_at;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public String getOrder_id() {
        return order_id;
    }

    public MyOrderDtoObject getObject() {
        return object;
    }

    public RefundInfo getRefundInfo() {
        return refundInfo;
    }

    public String getStatus_msg() {
        return status_msg;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public String getRefund_money() {
        return refund_money;
    }

    public String getReason() {
        return reason;
    }

    public String getMall_order_sn() {
        return mall_order_sn;
    }

    public String getDate() {
        return date;
    }

    public String getShipping_price() {
        return shipping_price;
    }

    public String getPay_total() {
        return pay_total;
    }

    public String getProduct_total() {
        return product_total;
    }

    public String getTotal() {
        return total;
    }

    public AddressInfo getAddress() {
        return address;
    }

    public String getCount() {
        return count;
    }

}
