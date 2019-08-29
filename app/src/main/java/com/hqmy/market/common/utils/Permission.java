package com.hqmy.market.common.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.hqmy.market.view.widgets.dialog.BaseDialog;
import com.hqmy.market.view.widgets.dialog.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 6.0动态权限检查
 * Created by THINK on 2017/12/27.
 */

public class Permission {
    /**
     * 6.0动态权限：
     */
    public static final String[] ps = new String[]{
            "android.permission.CAMERA",
            "android.permission.READ_CONTACTS",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.RECORD_AUDIO",
            "android.permission.READ_PHONE_STATE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.CALL_PHONE",
            "android.permission.ACCESS_COARSE_LOCATION"};
    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    public static List<String> ss = new ArrayList<>();//未授权的权限

    /**
     * 6.0权限申请：如果没有权限先申请
     */
    public static void requestPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            for (String s : ps) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, s);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ss.add(s);
                }
            }
            if (ss.size() == 0) return;
            String[] strings = new String[ss.size()];
            for (int i = 0; i < strings.length; i++) {
                strings[i] = ss.get(i);
            }
            ActivityCompat.requestPermissions(context, strings, REQUEST_CODE_ASK_CALL_PHONE);

        }

    }

    /**
     * 6.0权限检测：如果没有就向跳转到系统设置
     *
     * @param context:Context
     * @param s:权限            return true：已打开权限，
     *                        return false：没打开权限，
     */
    public static boolean checkPublishPermission(final Activity context, String s) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context, s)) {
                ConfirmDialog dialog = new ConfirmDialog(context);
                dialog.setTitle("权限开启提醒");
                dialog.setMessage("为了保证APP正常使用，请打开相应权限");
                dialog.setCancelable(false);
                dialog.setYesOnclickListener("前往开启",new BaseDialog.OnYesClickListener(){

                    @Override
                    public void onYesClick() {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        context.startActivity(intent);
                    }
                });
                dialog.setCancleClickListener("退出应用",new BaseDialog.OnCloseClickListener(){

                    @Override
                    public void onCloseClick() {
                        //用户取消设置权限
                        System.exit(0);
                    }
                });
                dialog.show();
                return false;
            }
        }
        return true;
    }

}
