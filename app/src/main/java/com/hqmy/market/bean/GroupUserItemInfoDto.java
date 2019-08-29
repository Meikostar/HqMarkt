package com.hqmy.market.bean;

import java.io.Serializable;

public class GroupUserItemInfoDto implements Serializable{

  	private String id;
  	private String group_id;
  	private String user_id;
  	private String group_nickname;
  	private String role;
  	private String level;
  	private String status;
  	private String gag;
  	private String extra;
  	private String created_at;
  	private GroupUserItemDataInfoDto user;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGroup_nickname() {
        return group_nickname;
    }

    public void setGroup_nickname(String group_nickname) {
        this.group_nickname = group_nickname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGag() {
        return gag;
    }

    public void setGag(String gag) {
        this.gag = gag;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public GroupUserItemDataInfoDto getUser() {
        return user;
    }

    public void setUser(GroupUserItemDataInfoDto user) {
        this.user = user;
    }
}
