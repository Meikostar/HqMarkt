package com.hqmy.market.bean;

import java.util.List;

public class MetaDto {
    MetaPaginationDto pagination;
    public List<ExtDto> brand;

    public MetaPaginationDto getPagination() {
        return pagination;
    }

    public void setPagination(MetaPaginationDto pagination) {
        this.pagination = pagination;
    }
}
