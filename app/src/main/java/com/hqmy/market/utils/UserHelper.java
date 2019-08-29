package com.hqmy.market.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hqmy.market.common.Constants;
import com.hqmy.market.view.activity.LoginActivity;

public class UserHelper {
    /**
     * 判断是否登录
     * @return
     */
    public static boolean isLogin(){
        if(TextUtils.isEmpty(ShareUtil.getInstance().getString(Constants.USER_TOKEN, ""))){
            return false;
        }
        return true;
    }
    /**
     * 判断是否登录,未登录则跳转到登录页面
     * @return
     */
    public static boolean isLogin(Context context) {
        if (isLogin()) {
            return true;
        }
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        return false;
    }
}
