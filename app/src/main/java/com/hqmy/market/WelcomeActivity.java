package com.hqmy.market;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.TextView;

import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.LoginDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.request.UserRegister;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.MainActivity;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.adapter.SplashAdapter;
import com.tencent.smtt.sdk.TbsDownloader;

import butterknife.BindView;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.time)
    TextView  time;
    @BindView(R.id.btn)
    View      btn;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    boolean isFirstRun = false;
    long    delay      = 2000;//lunbo 时间间隔
    @BindView(R.id.home_welcome)
    WebView webView;
    private CountDownTimer timer   = new CountDownTimer(2000, 1000) {
        @Override
        public void onTick(long l) {
            time.setText("跳过 " + l / 1000 + "s");
        }

        @Override
        public void onFinish() {
            int anInt = ShareUtil.getInstance().getInt(Constants.IS_PASS, -1);
            if (anInt == 1) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                String username = ShareUtil.getInstance().getString(Constants.USER_ACCOUNT_NUMBER, "");
                String password = ShareUtil.getInstance().getString(Constants.USER_PASSWORD, "");
                if (TextUtil.isNotEmpty(username) && TextUtil.isNotEmpty(password)) {
                    login(username, password);
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        }
    };
    private Handler        handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int postion = msg.what;
            postion++;
            if (postion <= 2) {
                viewpager.setCurrentItem(postion);
                handler.sendEmptyMessageDelayed(postion, delay);
            }
        }
    };


    @Override
    public void initListener() {

    }

    private void login(String username, String password) {
        UserRegister userRegister = new UserRegister();
        userRegister.setPhone(username);
        userRegister.setPassword(password);
        userRegister.setInclude("user.userExt");

        DataManager.getInstance().login(new DefaultSingleObserver<LoginDto>() {
            @Override
            public void onSuccess(LoginDto loginDto) {
                if (loginDto.getUser().getData().userExt != null && loginDto.getUser().getData().userExt.data != null) {
                    ShareUtil.getInstance().saveInt(Constants.IS_PASS, loginDto.getUser().getData().userExt.data.status);


                } else {
                    ShareUtil.getInstance().saveInt(Constants.IS_PASS, -1);
                }
                gotoActivity(LoginActivity.class, true);
            }

            @Override
            public void onError(Throwable throwable) {

                ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, userRegister);
    }

    private void firstRun() {
        //        PreferenceManager.save("isFirstRun", false);
        ShareUtil.getInstance().saveBoolean("isFirstRun", true);
        // Toast.makeText(this,"第一次",Toast.LENGTH_LONG).show();

        viewpager.setVisibility(View.VISIBLE);
        viewpager.setAdapter(new SplashAdapter(this));
        btn.setVisibility(View.GONE);
        handler.sendEmptyMessageDelayed(0, delay);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ShareUtil.getInstance().getString(Constants.USER_ACCOUNT_NUMBER, "");
                String password = ShareUtil.getInstance().getString(Constants.USER_PASSWORD, "");
                if (TextUtil.isNotEmpty(username) && TextUtil.isNotEmpty(password)) {
                    login(username, password);
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    btn.setVisibility(View.VISIBLE);
                } else {
                    btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
            /*viewpager.setOnLastPageSelectedListener(new MyRollPagerView.OnLastPageSelectedListener() {
                @Override
                public void onLastPageSelected(boolean isLashPage) {
                    if (isLashPage) {
                        btn.setVisibility(View.VISIBLE);
                    } else {
                        btn.setVisibility(View.GONE);
                    }
                }
            });*/

        //viewpager.setHintView(new ColorPointHintView(this, Color.parseColor("#00000000"), Color.parseColor("#00000000")));
    }

    @OnClick(R.id.time)
    public void onViewClicked() {
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    private MediaController mc;

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    private Context mContext;

    @Override
    public void initView() {
        TbsDownloader.needDownload(this, false);
        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = i_getvalue.getData();
            if (uri != null) {
                String name = uri.getQueryParameter("name");
                String age = uri.getQueryParameter("age");
            }

        }
             /*       new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);*/


        isFirstRun = ShareUtil.getInstance().getBoolean("isFirstRun");

        if (!isFirstRun) {
            firstRun();
        } else {

            btn.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.setVerticalScrollBarEnabled(false); //垂直滚动条不显示
            webView.setHorizontalScrollBarEnabled(false);//水平不显示
            WebSettings webSettings = webView.getSettings();
            webSettings.setDisplayZoomControls(false);//隐藏webview缩放按钮
            webSettings.setJavaScriptEnabled(true);
            webSettings.setUseWideViewPort(true);//屏幕适配:设置webview推荐使用的窗口，设置为true
            webSettings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式，也设置为true
            webSettings.setAllowFileAccess(true);
            webSettings.setSupportZoom(true);//是否支持缩放
            webSettings.setBuiltInZoomControls(true);//添加对js功能的支持
            webView.setWebViewClient(new WebViewClient());
            webView.setBackgroundColor(0);
            String gifPath = "file:///android_asset/ic_splash.gif";
            webView.loadUrl(gifPath);

            time.setVisibility(View.GONE);
            timer.start();
        }
    }

    @Override
    public void initData() {

    }
}
