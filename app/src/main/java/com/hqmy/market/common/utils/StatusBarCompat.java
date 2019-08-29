package com.hqmy.market.common.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 透明状态栏
 */
public class StatusBarCompat {

    private static final int COLOR_TRANSLUCENT = Color.parseColor("#00000000");

    public static final int DEFAULT_COLOR_ALPHA = 112;

    public static void setStatusBarColor(Activity activity, int statusColor) {
        Window win = activity.getWindow();
        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //After LOLLIPOP not translucent status bar
            win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //Then call setStatusBarColor.
            win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            win.setStatusBarColor(statusColor);
            //set child View not fill the system window
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        }else{
//            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setNavigationBarTintEnabled(true);
            // 自定义颜色
//            tintManager.setTintColor(statusColor);
        }
    }

    public static void setStartBarTranslucent(Activity activity){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else if((Build.VERSION.SDK_INT< Build.VERSION_CODES.M)&&(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT)){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }


    public static void setBottomBarTransluent(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }



}