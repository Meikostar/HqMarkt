package com.hqmy.market.common.utils;

import android.util.Log;

import com.hqmy.market.common.Constants;

public class LogUtil {

    /**
     * 是否打印日志
     */
    private static boolean OPEN_LOG = Constants.open_log;

    /**
     * 异常肯定打印
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (OPEN_LOG)
            Log.w(tag, msg);
    }

    /**
     * 异常肯定打印
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (OPEN_LOG)
            Log.e(tag, msg);
    }

    public static void system_print(String msg) {
        if (OPEN_LOG)
            System.out.println(msg);
    }

    public static void d(String tag, String msg) {
        if (OPEN_LOG)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (OPEN_LOG) {
            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.i(tag, msg);
            } else {
                while (msg.length() > segmentSize) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize);
                    msg = msg.replace(logContent, "");
                    Log.i(tag, logContent);
                }
                Log.i(tag, msg);// 打印剩余日志
            }
        }
    }

    public static void v(String tag, String msg) {
        if (OPEN_LOG)
            Log.v(tag, msg);
    }

    public static void wtf(String tag, String msg) {
        if (OPEN_LOG)
            Log.wtf(tag, msg);
    }

}
