package com.hqmy.market.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.hqmy.market.db.bean.UserInfo;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseApplication;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.ToastUtil;

import java.io.File;
import java.util.List;
import java.util.Map;




public class ShareUtil {

    private static ShareUtil shareUtil;
    private static SharedPreferences sharedPreferences;
    public static final String FILENAME = "com.hqmy.market";

    /**
     * --------------------------设备相关相关------------------------------------
     */
    public static final String TOKEN = "device_token"; // 设备唯一id
    public static final String TIMESTAMP = "timeStamp"; // 服务器的时间

    /**
     * --------------------------用户相关------------------------------------
     */
    public static final String USERID = "USERID"; // 用户ID
    // public static final String NOTFIRST = "NOTFIRST"; //第一次使用App(默认为false)
    public static final String ACCESS_TOKEN = "access_token";//登录后获取的access_token

    /**
     * --------------------------设置相关------------------------------------
     */
    public static final String NO_FIRST_START = "NO_FIRST_START"; // 程序第一次启动
    public static final String PLAYONNET = "PLAYONNET"; // 普通网络也能播放
    public static final String RECEIVEPUSH = "RECEIVEPUSH";

    public static final String SHARE_APP_ID ="wxb581256530affa17";

    private ShareUtil() {
        if (shareUtil == null) {
            sharedPreferences = BaseApplication.getInstance().getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        }
    }

    public static ShareUtil getInstance() {
        if (shareUtil == null) {
            shareUtil = new ShareUtil();
        }
        return shareUtil;
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return sharedPreferences.getString(key, null);
    }

    /**
     * 取出数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * 保存偏好设置
     *
     * @param key
     * @param value
     */
    public void save(String key, String value) {
        Editor editor = sharedPreferences.edit();
        if (value == null) {
            editor.putString(key, "");
        } else {
            editor.putString(key, value);
        }
        editor.commit();
    }

    /**
     * 保存偏好设置
     *
     * @param map 需要保存的map集合
     */
    public void save(Map<String, String> map) {
        Editor editor = sharedPreferences.edit();
        for (Map.Entry<String, String> entry : map.entrySet()) {

            if (entry.getValue().length() == 0) {
                editor.putString(entry.getKey(), null);

            } else {
                editor.putString(entry.getKey(), entry.getValue());
            }
        }
        editor.commit();
    }

    // 保存int类型
    public void saveInt(String key, int value) {
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();

    }

    // 获取int类型
    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    // 获取int类型
    public int getInt(String key, int defaultInt) {
        return sharedPreferences.getInt(key, defaultInt);
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * 获取数据
     *
     * @param key
     * @param value
     * @return
     */
    public boolean getBoolean(String key, boolean value) {
        return sharedPreferences.getBoolean(key, value);
    }

    /**
     * 保存偏好设置
     *
     * @param key
     * @param value
     */
    public void saveBoolean(String key, boolean value) {
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    /**
     * 清空指定的Key
     */
    public void removeKey(String key) {
        Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
    /**
     * 退出时候清空用户信息
     */
    public void cleanUserInfo() {
        Uri RongHeadImg = Uri.parse(Constants.WEB_IMG_URL_UPLOADS + ShareUtil.getInstance().getString(Constants.USER_HEAD, null));
        String userId = ShareUtil.getInstance().getString(Constants.USER_ID, "");
        String nickName = ShareUtil.getInstance().getString(Constants.USER_NAME, "");
//        UserInfo userInfo = new UserInfo(userId, nickName, RongHeadImg);
//        RongIM.getInstance().refreshUserInfoCache(userInfo);//更新的用户缓存数据
//        RongIM.getInstance().logout();//退出融云
        Editor editor = sharedPreferences.edit();
        editor.putString(Constants.APP_USER_KEY, "");
        editor.putString(Constants.USER_TOKEN, "");
        editor.putString(Constants.USER_NAME, "");
        //editor.putString(Constants.USER_PHONE, "");
        editor.putString(Constants.USER_HEAD, "");
        editor.putString(Constants.USER_ID, "");
        //editor.clear();
        //清空但保存不是一登录
        editor.putBoolean(Constants.NO_LOGIN_SUCCESS, false);
        //设置是否第一次登录的标志
        editor.putBoolean(Constants.IS_FIRST_RUN,true);
        //清空但保存提示信息
        //editor.putBoolean(Constants.NOTFIRST, true);
        editor.commit();
    }

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     * @param context
     */
    public void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
     */
    public void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /**
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
     */
    public void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 按名字清除本应用数据库
     *
     * @param context
     * @param dbName
     */
    public void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     *
     * @param context
     */
    public void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    public void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     *
     * @param filePath
     */
    public void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 清除本应用所有的数据
     *
     * @param context
     * @param filepath
     */
    public void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     *
     * @param directory
     */
    private void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /**
     * 用户是否登录
     * @return
     */
    public boolean isLogin(){
        if (!TextUtils.isEmpty(ShareUtil.getInstance().getString(Constants.USER_TOKEN, ""))) {
           return true;
        } else {
            return false;
        }
    }
  //判断微信,qq,微博是否安装

    public static boolean isAvailable(Context context, String packageName) {

        final PackageManager packageManager = context.getPackageManager();

        // 获取packagemanager

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);

        // 获取所有已安装程序的包信息

        if (pinfo != null) {

            for (int i = 0; i < pinfo.size(); i++) {

                String pn = pinfo.get(i).packageName;

                if (pn.equals(packageName)) {

                    return true;

                }

            }

        }

        return false;

    }
    public static void sendToWeaChat(Context mContext,boolean isTimelineCb, String title, String url) {

        if (!ShareUtil.isAvailable(mContext, "com.tencent.mm")) {
            ToastUtil.showToast("你的手机尚未安装微信客户端,请先安装微信客户端");
            return;
        }
        final IWXAPI api = WXAPIFactory.createWXAPI(mContext, null);
        // 将该app注册到微信
        api.registerApp(ShareUtil.SHARE_APP_ID);
        //初始化一个WXWebpageObject填写url
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;
        //用WXWebpageObject对象初始化一个WXMediaMessage，天下标题，描述
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = title;
        msg.description = "环球贸易";
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_logo);

        Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb, 120, 120, true);
        msg.setThumbImage(thumbBmp);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
        thumbBmp.recycle();
        thumb.recycle();
    }
}
