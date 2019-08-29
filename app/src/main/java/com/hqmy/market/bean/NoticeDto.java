package com.hqmy.market.bean;

import java.io.Serializable;

/**
 * Created by rzb on 2019/7/10.
 */

public class NoticeDto implements Serializable {
    private String id;
    private String type;
    private NoticeDetailDto data;
    private String read_at;
    private String created_at;
    private String updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NoticeDetailDto getData() {
        return data;
    }

    public void setData(NoticeDetailDto data) {
        this.data = data;
    }

    public String getRead_at() {
        return read_at;
    }

    public void setRead_at(String read_at) {
        this.read_at = read_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
