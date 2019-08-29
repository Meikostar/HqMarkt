package com.hqmy.market.bean;

import java.util.ArrayList;

/**
 * 分页
 */
public class Pagination {
    private int       count;//     1
    private int       current_page;//         1
    private ArrayList links;// Array
    private int       per_page;//     15
    private int       total;//     1
    private int       total_pages;//     	1

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public ArrayList getLinks() {
        return links;
    }

    public void setLinks(ArrayList links) {
        this.links = links;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }
}
