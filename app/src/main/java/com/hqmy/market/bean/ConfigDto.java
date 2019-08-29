package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/7/8
 */
public class ConfigDto implements Serializable {
    private HotSearchDto hot_search;
    private List<String> st_search_distance;

    public HotSearchDto getHot_search() {
        return hot_search;
    }

    public void setHot_search(HotSearchDto hot_search) {
        this.hot_search = hot_search;
    }

    public List<String> getSt_search_distance() {
        return st_search_distance;
    }

    public void setSt_search_distance(List<String> st_search_distance) {
        this.st_search_distance = st_search_distance;
    }
}
