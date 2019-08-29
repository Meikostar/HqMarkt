package com.hqmy.market.bean;

import java.io.Serializable;

public class RedPacketDrawBean implements Serializable {

    /**
     * amount (number, optional): 红包金额 ,
     * createTime (string, optional): 领取时间 ,
     * redPacketId (string, optional): 红包id ,
     * tradeStatus (integer, optional): 状态：1成功,0失败 ,
     * updateTime (string, optional): 修改时间 ,
     * userHeader (string, optional): 用户头像 ,
     * userId (string, optional),
     * userName (string, optional): 用户昵称
     */
    private String amount;
    private String createTime;
    private String redPacketId;
    private String tradeStatus;
    private String updateTime;
    private String userName;
    private String userId;
    private String userHeader;
    private boolean maxAmount = false;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    public boolean isMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(boolean maxAmount) {
        this.maxAmount = maxAmount;
    }
}
