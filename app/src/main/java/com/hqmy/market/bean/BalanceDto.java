package com.hqmy.market.bean;

public class BalanceDto {

    private double  money;//      0
    private boolean pay_password;//      	false
    private int     user_id;//  1
    private String score;
    public BalanceDto data;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public boolean isPay_password() {
        return pay_password;
    }

    public void setPay_password(boolean pay_password) {
        this.pay_password = pay_password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getScore() {
        return score;
    }
}
