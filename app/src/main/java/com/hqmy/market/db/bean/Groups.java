package com.hqmy.market.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * desc:
 * 2018/6/30 17:05
 *
 * @author dahai.zhou
 * @since 2018/6/30
 */
@Entity(nameInDb = "Groups")
public class Groups {
    /*数据库字段*/
    @Id(autoincrement = true)
    private Long groupsId;
    private String groupsName;
    private String groupHeader;
    private String role; //角色
    @NotNull
    private String userId;
    private String userName;
    @NotNull
    private String userPortraitUri;

    @Generated(hash = 893039872)
    public Groups() {
    }

    @Generated(hash = 327875941)
    public Groups(Long groupsId, String groupsName, String groupHeader, String role, @NotNull String userId, String userName,
            @NotNull String userPortraitUri) {
        this.groupsId = groupsId;
        this.groupsName = groupsName;
        this.groupHeader = groupHeader;
        this.role = role;
        this.userId = userId;
        this.userName = userName;
        this.userPortraitUri = userPortraitUri;
    }

    public Long getGroupsId() {
        return groupsId;
    }

    public void setGroupsId(Long groupsId) {
        this.groupsId = groupsId;
    }

    public String getGroupsName() {
        return groupsName;
    }

    public void setGroupsName(String groupsName) {
        this.groupsName = groupsName;
    }

    public String getGroupHeader() {
        return groupHeader;
    }

    public void setGroupHeader(String groupHeader) {
        this.groupHeader = groupHeader;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPortraitUri() {
        return userPortraitUri;
    }

    public void setUserPortraitUri(String userPortraitUri) {
        this.userPortraitUri = userPortraitUri;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Groups{");
        sb.append("  groupsId=").append(groupsId);
        sb.append(", groupsName='").append(groupsName).append('\'');
        sb.append(", groupHeader='").append(groupHeader).append('\'');
        sb.append(", role='").append(role).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", userPortraitUri='").append(userPortraitUri).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
