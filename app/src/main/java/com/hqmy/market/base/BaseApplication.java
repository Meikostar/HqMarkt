package com.hqmy.market.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.hqmy.market.qiniu.chatroom.ChatroomKit;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.meiqia.core.MQManager;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.qiniu.pili.droid.streaming.StreamingEnv;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.hqmy.market.R;
import com.hqmy.market.db.DaoManager;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by rzb on 2019/4/16.
 */
public class BaseApplication extends Application{

    private static BaseApplication instance;
    private static List<Activity> activityList = new ArrayList<>();
    private static Context mContext;
    private static Handler mHandler;
    public static int  isSetPay;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        mHandler = new Handler();
        initSmartRefreshLayout();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        //GreenDao数据库管理初始化
        DaoManager.getInstance().init(this);
        initMeiqiaSDK();
        //初始化融云
        initRongCloud();
        StreamingEnv.init(getApplicationContext());
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();
    }

    private void initMeiqiaSDK() {
        MQManager.setDebugMode(true);

        // 替换成自己的key
//        String meiqiaKey = "a71c257c80dfe883d92a64dca323ec20";
        String meiqiaKey = "c391f32ffa6cd59b339a8a1d7847c354";
        MQConfig.init(this, meiqiaKey, new OnInitCallback() {
            @Override
            public void onSuccess(String clientId) {
//                Toast.makeText(BaseApplication.this, "init success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String message) {
                Toast.makeText(BaseApplication.this, "int failure message = " + message, Toast.LENGTH_SHORT).show();
            }
        });

        // 可选
        customMeiqiaSDK();
    }
    private void customMeiqiaSDK() {
        // 配置自定义信息
        MQConfig.ui.titleGravity = MQConfig.ui.MQTitleGravity.LEFT;
//        MQConfig.ui.backArrowIconResId = R.drawable.ic_arrow_back_white_24dp;
        //        MQConfig.ui.titleBackgroundResId = R.color.test_red;
        //        MQConfig.ui.titleTextColorResId = R.color.test_blue;
        //        MQConfig.ui.leftChatBubbleColorResId = R.color.test_green;
        //        MQConfig.ui.leftChatTextColorResId = R.color.test_red;
        //        MQConfig.ui.rightChatBubbleColorResId = R.color.test_red;
        //        MQConfig.ui.rightChatTextColorResId = R.color.test_green;
        //        MQConfig.ui.robotEvaluateTextColorResId = R.color.test_red;
        //        MQConfig.ui.robotMenuItemTextColorResId = R.color.test_blue;
        //        MQConfig.ui.robotMenuTipTextColorResId = R.color.test_blue;
    }

    private static void initSmartRefreshLayout() {
        //static 代码段可以防止内存泄露

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.my_color_F0F0F0, R.color.my_color_2E2E2E);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }


    /**
     * 关闭每一个list内的activity
     */
    public void finishAll() {
        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加activity管理
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }


    private void initRongCloud() {
        ChatroomKit.init(this,"pvxdm17jposrr");


    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static Context getContext() {
        return mContext;
    }
}
