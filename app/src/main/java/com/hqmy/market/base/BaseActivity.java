package com.hqmy.market.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jakewharton.rxbinding2.view.RxView;
import com.hqmy.market.R;
import com.hqmy.market.common.utils.StatusBarCompat;
import com.hqmy.market.common.utils.StatusBarUtils;
import com.hqmy.market.view.widgets.autoview.ActionbarView;
import com.hqmy.market.view.widgets.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Action;


/**
 * Activity基类
 * Created by Administrator on 2019/4/18.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseViewInterface {
    protected static final String TAG = BaseActivity.class.getSimpleName();
    protected ActionbarView actionbar;
    private static List<BaseActivity> activityList = new ArrayList<>();
    private PermissionsListener permissionsListener;
    public  LoadingDialog       loadingDialog;

    @SuppressLint({"RtlHardcoded", "InlinedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
        }
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBar();
        //把当前Activity加到列表里面
        activityList.add(this);
        //初始化绑定id
        ButterKnife.bind(this);
        registerView();
        initView();
        initData();
        initListener();
    }

    public abstract void initListener();


    protected void registerView() {
        actionbar = findViewById(R.id.custom_action_bar);
        if (actionbar != null) {
            actionbar.setDisplayHomeAsEnable(true);
            actionbar.setTitle(getTitle().toString());
            bindClickEvent(actionbar.getBackView(), () -> {
                onBackPressed();
            });
        }
    }


    @Override
    public void finish() {
        activityList.remove(this);
        super.finish();
    }

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
     * 跳转activity
     *
     * @param clz
     */
    protected void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (isCloseCurrentActivity)
            finish();
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        if (isCloseCurrentActivity)
            finish();
    }

    /**
     * 设置状态栏
     */
    private void setStatusBar() {
        StatusBarCompat.setStartBarTranslucent(this);
        StatusBarUtils.StatusBarLightMode(this);
    }

    /**
     * 基本点击事件跳转默认节流1000毫秒
     *
     * @param view   绑定的view
     * @param action 跳转的Acticvity
     */
    protected void bindClickEvent(View view, final Action action) {
        bindClickEvent(view, action, 1000);
    }

    @SuppressLint("CheckResult")
    protected void bindClickEvent(View view, final Action action, long frequency) {
        Observable<Object> observable = RxView.clicks(view);
        observable.throttleFirst(frequency, TimeUnit.MILLISECONDS).subscribe(trigger -> action.run());
    }

    /**
     * 可自定义节流点击事件跳转
     *
     * @param view 绑定的view
     * @param clz  跳转的Acticvity
     */
    protected void bindClickJumpUiEvent(View view, final Class<?> clz) {
        bindClickJumpUiEvent(view, clz, 1000);
    }

    protected void bindClickJumpUiEvent(View view, final Class<?> clz, long frequency) {
        bindClickEvent(view, () -> gotoActivity(clz), frequency);
    }

    private final int REQUEST_CODE_ADDRESS = 100;
    //    文件写权限
    public final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    //    文件读权限
    public final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    //用于访问GPS定位
    public final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    //用于拨打电话
    public final String CALL_PHONE = Manifest.permission.CALL_PHONE;
    //相机
    public final String CAMERA = Manifest.permission.CAMERA;
    //录音
    public final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;


    /**
     * 申请APP所有必需的权限
     */
    public void checkPermissioin() {
        ActivityCompat.requestPermissions(BaseActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION, CALL_PHONE, CAMERA,RECORD_AUDIO}, REQUEST_CODE_ADDRESS);
    }

    /**
     * 检查是否拥有权限
     *
     * @param permissioin
     * @return
     */
    public boolean isPermissioin(String permissioin) {
        int checkPermissioin = ContextCompat.checkSelfPermission(BaseActivity.this, permissioin);
        if (checkPermissioin == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 申请授权
     *
     * @param permissioin 权限名称
     */
    public void checkPermissioin(String permissioin) {
        ActivityCompat.requestPermissions(BaseActivity.this, new String[]{permissioin}, REQUEST_CODE_ADDRESS);
    }


    public interface PermissionsListener {
        void callbackPermissions(String permissions, boolean isSuccess);
    }

    /**
     * 监听申请权限是否成功
     */
    public void setPermissionsListener(PermissionsListener listener) {
        this.permissionsListener = listener;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_ADDRESS) {
            boolean isSuccess = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            switch (permissions[0]) {
                case WRITE_EXTERNAL_STORAGE:
                    if (permissionsListener != null) {
                        permissionsListener.callbackPermissions(WRITE_EXTERNAL_STORAGE, isSuccess);
                    }
                    break;
                case READ_EXTERNAL_STORAGE:
                    if (permissionsListener != null) {
                        permissionsListener.callbackPermissions(READ_EXTERNAL_STORAGE, isSuccess);
                    }
                    break;
                case ACCESS_FINE_LOCATION:
                    if (permissionsListener != null) {
                        permissionsListener.callbackPermissions(ACCESS_FINE_LOCATION, isSuccess);
                    }
                    break;
                case CALL_PHONE:
                    if (permissionsListener != null) {
                        permissionsListener.callbackPermissions(CALL_PHONE, isSuccess);
                    }
                    break;
                case CAMERA:
                    if (permissionsListener != null) {
                        permissionsListener.callbackPermissions(CAMERA, isSuccess);
                    }
                    break;
                default:
                    break;
            }
        }
    }


    protected void showLoadDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.show(this);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        loadingDialog.show();
    }

    /**
     * 关闭键盘
     */
    public void closeKeyBoard() {
        try {
            if(this.getCurrentFocus() == null){
                return;
            }
            IBinder iBinder = this.getCurrentFocus().getWindowToken();
            if (iBinder == null) {
                return;
            }
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(iBinder, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void dissLoadDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancelDialog();
        }
    }
}
