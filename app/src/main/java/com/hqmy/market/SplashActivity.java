package com.hqmy.market;

import android.text.TextUtils;
import android.util.Log;

import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.RxTimerUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.qiniu.chatroom.ChatroomKit;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.MainActivity;

import io.rong.imlib.RongIMClient;

/**
 * 启动页面
 * Created by rzb on 2019/6/14.
 */
public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    @Override
    public int getLayoutId() {
        return R.layout.ui_splash_layout;
    }

    @Override
    public void initView() {






    }

    @Override
    public void initData() {
        RxTimerUtil.timer(100, number -> {
            gotoActivity(MainActivity.class, true);
        });
    }

    @Override
    public void initListener() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
