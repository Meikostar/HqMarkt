package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupUserInfoDto implements Serializable{
    private ArrayList<GroupUserItemInfoDto> data;

    public ArrayList<GroupUserItemInfoDto> getData() {
        return data;
    }

    public void setData(ArrayList<GroupUserItemInfoDto> data) {
        this.data = data;
    }
}
