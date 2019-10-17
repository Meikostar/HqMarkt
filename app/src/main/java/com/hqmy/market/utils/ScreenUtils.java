package com.hqmy.market.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

import java.util.Date;

/**
 * ScreenUtils
 * Created by hanj on 14-9-25.
 */
public class ScreenUtils {
    private static int screenW;
    private static int screenH;

    public static int getScreenW(Activity mActivity){
        if (screenW == 0){
            initScreen(mActivity);
        }
        return screenW;
    }

    public static int getScreenH(Activity mActivity){
        if (screenH == 0){
            initScreen(mActivity);
        }
        return screenH;
    }

    private static void initScreen(Activity mActivity){
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
    }


    public static String formatToFileDi(long time) {
        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();
        long seconds = (currentTimeStamp - time) / 1000;

        long minutes = Math.abs(seconds / 60);
        long hours = Math.abs(minutes / 60);
        long days = Math.abs(hours / 24);

        if (seconds <= 15) {
            return "刚刚";
        } else if (seconds < 120) {
            return "1分钟前";
        } else if (minutes < 60) {
            return minutes + "分钟前";
        } else if (minutes < 120) {
            return "一小时前";
        } else if (hours < 24) {
            return hours + "小时前";
        } else if (hours < 24 * 2) {
            return "昨天";
        } else if (hours < 8760) {
            int floor = (int) Math.floor(hours / 24);
            return floor + "天前";
        } else {
            return (int) Math.floor(hours / 8760) + "年前";
            //            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
            //            String dateString = formatter.format(new Date(time));
            //            return dateString;
        }
    }
}
