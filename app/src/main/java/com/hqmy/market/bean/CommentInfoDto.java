package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/7/11.
 */
public class CommentInfoDto implements Serializable {
      private List<CommentDto> data;

    public List<CommentDto> getData() {
        return data;
    }

    public void setData(List<CommentDto> data) {
        this.data = data;
    }
}
