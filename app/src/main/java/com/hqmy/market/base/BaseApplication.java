package com.hqmy.market.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
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
import com.hqmy.market.qiniu.chatroom.ChatroomKit;
import com.hqmy.market.rong.SealAppContext;

import java.util.ArrayList;
import java.util.List;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.RealTimeLocationMessageProvider;
import io.rong.imlib.ipc.RongExceptionHandler;
import io.rong.push.RongPushClient;
import io.rong.push.pushconfig.PushConfig;

/**
 * Created by rzb on 2019/4/16.
 */
public class BaseApplication extends Application{

    private static BaseApplication instance;
    private static List<Activity> activityList = new ArrayList<>();
    private static Context mContext;
    private static Handler mHandler;

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
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
            PushConfig config = new PushConfig
                    .Builder()
                    .enableHWPush(true)// 配置华为推送
                    .enableMiPush("2882303761517970088", "5571797011088")////配置小米推送
                    .enableMeiZuPush("112988", "2fa951a802ac4bd5843d694517307896") //配置魅族推送
                    .enableVivoPush(true) //vivo 推送
                    .enableFCM(true) //谷歌官方推送
                    .build();
            RongPushClient.setPushConfig(config);
            // 注意： IMKit SDK调用第一步 初始化  context上下文,只有两个进程需要初始化，主进程和 push 进程
            RongIM.setServerInfo("nav.cn.ronghub.com", "up.qbox.me");
            Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));
            try {
                //RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
                RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
                //RongIM.registerMessageType(TestMessage.class);
                // RongIM.registerMessageTemplate(new TestMessageProvider());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        RongIM.init(this);
        ChatroomKit.init(this,"82hegw5u8xsbx");
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            SealAppContext.init(this);
        }
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
