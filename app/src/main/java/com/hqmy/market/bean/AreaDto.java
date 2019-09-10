package com.hqmy.market.bean;

import java.io.Serializable;

public class AreaDto implements Serializable {

//           "id": 14,
//                   "parent_id": null,
//                   "order": 13,
//                   "title": "越南",
//                   "icon": "category_icon/越南.png",
//                   "description": null,
//                   "hidden": false,
//                   "is_recommend": false,
//                   "flag1": false,
//                   "flag2": false
    public String title;
    public String icon;
    public String logo;

    public int id;
    String name;
    long depth;
    int parent_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDepth() {
        return depth;
    }

    public void setDepth(long depth) {
        this.depth = depth;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }
}
