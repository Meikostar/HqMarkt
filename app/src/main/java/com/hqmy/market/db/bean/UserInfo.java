package com.hqmy.market.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * desc:
 * 2018/6/30 17:05
 *
 * @author dahai.zhou
 * @since 2018/6/30
 */
@Entity(nameInDb = "UserInfo")
public class UserInfo {
    /*数据库字段*/
    @NotNull
    private String userId;
    @NotNull
    private String userName;
    @NotNull
    private String userPortraitUri;

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    @Generated(hash = 193276495)
    public UserInfo(@NotNull String userId, @NotNull String userName,
            @NotNull String userPortraitUri) {
        this.userId = userId;
        this.userName = userName;
        this.userPortraitUri = userPortraitUri;
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
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", userPortraitUri='").append(userPortraitUri).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
