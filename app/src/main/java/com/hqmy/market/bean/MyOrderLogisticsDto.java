package com.hqmy.market.bean;

public class MyOrderLogisticsDto {
    private String no;
    private String score;
    private MyorderItemsDto items;
    private ShipmentsInfo shipments;
    private MyOrderAddressDetailDtoInfo address;

    public String getNo() {
        return no;
    }

    public String getScore() {
        return score;
    }

    public MyorderItemsDto getItems() {
        return items;
    }

    public ShipmentsInfo getShipments() {
        return shipments;
    }

    public MyOrderAddressDetailDtoInfo getAddress() {
        return address;
    }
}
