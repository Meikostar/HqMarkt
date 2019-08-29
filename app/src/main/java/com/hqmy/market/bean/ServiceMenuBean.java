package com.hqmy.market.bean;

public class ServiceMenuBean {


    /**
     * id : 1
     * parent_id : 0
     * images : images/mLA8S2vR31UYFMDzjxQKwUPbOy1g0NwhXERaMDTM.png
     * title : 商家入驻
     * click_event_type : http
     * click_event_value : shop_in
     */

    private int id;
    private int parent_id;
    private String images;
    private String title;
    private String click_event_type;
    private String click_event_value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClick_event_type() {
        return click_event_type;
    }

    public void setClick_event_type(String click_event_type) {
        this.click_event_type = click_event_type;
    }

    public String getClick_event_value() {
        return click_event_value;
    }

    public void setClick_event_value(String click_event_value) {
        this.click_event_value = click_event_value;
    }
}
