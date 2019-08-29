package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/6/22.
 */
public class NewsRecommendListDto implements Serializable {
    private List<NewsRecommendListItemDto> flag1;
    private List<NewsRecommendListItemDto> flag2;
    private List<NewsRecommendListItemDto> flag3;

    public List<NewsRecommendListItemDto> getFlag1() {
        return flag1;
    }

    public void setFlag1(List<NewsRecommendListItemDto> flag1) {
        this.flag1 = flag1;
    }

    public List<NewsRecommendListItemDto> getFlag2() {
        return flag2;
    }

    public void setFlag2(List<NewsRecommendListItemDto> flag2) {
        this.flag2 = flag2;
    }

    public List<NewsRecommendListItemDto> getFlag3() {
        return flag3;
    }

    public void setFlag3(List<NewsRecommendListItemDto> flag3) {
        this.flag3 = flag3;
    }
}
