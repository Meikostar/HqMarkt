package com.hqmy.market.bean;

import java.io.Serializable;

/**
 * 联盟商城品牌列表Item
 */
public class BrandListItemDto implements Serializable {
     private String id;
     private String title;
     private String logo;
     private String description;
     private ExtDto ext;
     private String created_at;
     private String updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExtDto getExt() {
        return ext;
    }

    public void setExt(ExtDto ext) {
        this.ext = ext;
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
