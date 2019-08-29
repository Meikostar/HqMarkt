package com.hqmy.market.bean;

import java.util.ArrayList;

/**
 * 被评价的话题
 */
public class CommentTopic {

    private String            address;//            广东省 深圳市 南山区 高新南一道9-6号 中科大厦(高新南三道)
    private int               area_id;//        1939
    private int               click;//            10
    private String            content;//                孔雀东南飞，五里一徘徊，十里一回头！！
    private String            cover;//            image/201905/25/HXWl02jSxgxzEPVWuFbmNnSIPUJNoRQjzMlMTp5I.jpeg
    private String            created_at;//                        2019-05-25 14:25:04
    private int               id;//        1
    private ArrayList<String> img;//        Array
    private String ip;//                119.137.55.198
    private String lat;//                22.53881
    private String lng;//                113.95316
    private int    shares;//            0
    private int    status;//                    1
    private String    type;//            1
    private String updated_at;//                    2019-05-29 10:40:29
    private int    user_id;//                        3
    private User   user;
    private String title;
    private String price;
    private String shop_id;

    public String getAddress() {
        return address;
    }

    public int getArea_id() {
        return area_id;
    }

    public int getClick() {
        return click;
    }

    public String getContent() {
        return content;
    }

    public String getCover() {
        return cover;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getImg() {
        return img;
    }

    public String getIp() {
        return ip;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public int getShares() {
        return shares;
    }

    public int getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getShop_id() {
        return shop_id;
    }
}
