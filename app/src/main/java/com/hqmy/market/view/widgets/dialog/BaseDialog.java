package com.hqmy.market.view.widgets.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.jakewharton.rxbinding2.view.RxView;
import com.hqmy.market.R;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Action;


/**
 * Created by Administrator on 2017/11/6.
 */

public abstract class BaseDialog extends Dialog {

    protected Activity activity;
    protected String titleStr;//从外界设置的title文本
    protected String messageStr;//从外界设置的消息文本
    protected Button btn_sure;//确定按钮
    protected Button btn_cancel;//取消按钮
    protected TextView titleTv;//消息标题文本
    //确定文本和取消文本的显示内容
    protected String yesStr, noStr;
    protected ImageView iv_close;
    protected OnNoClickListener onNoClickListener;//取消按钮被点击了的监听器
    protected OnYesClickListener onYesClickListener;//确定按钮被点击了的监听器
    protected OnCloseClickListener onCloseClickListener;

    public BaseDialog(Activity activity) {
        super(activity, R.style.dialog_with_alpha);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }


    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

    /**
     * 基本点击事件跳转默认节流1000毫秒
     *
     * @param view   绑定的view
     * @param action 跳转的Acticvity
     */
    protected void bindClickEvent(View view, final Action action) {
        bindClickEvent(view, action, 1000);
    }

    protected void bindClickEvent(View view, final Action action, long frequency) {
        Observable<Object> observable = RxView.clicks(view);
        if (frequency > 0) {
            observable.throttleFirst(frequency, TimeUnit.MILLISECONDS);
        }
        observable.subscribe(trigger -> action.run());
    }

    public void setHideSingle() {
        if (btn_cancel != null)
            btn_cancel.setVisibility(View.GONE);
    }

    public void setCancelText(String noStr) {
        if (!TextUtils.isEmpty(noStr)) {
            this.noStr = noStr;
        }
    }

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoClickListener
     */
    public void setNoOnclickListener(String str, OnNoClickListener onNoClickListener) {
        if (!TextUtils.isEmpty(str)) {
            this.noStr = str;
        }
        this.onNoClickListener = onNoClickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesClickListener
     */
    public void setYesOnclickListener(String str, OnYesClickListener onYesClickListener) {
        if (!TextUtils.isEmpty(str)) {
            this.yesStr = str;
        }
        this.onYesClickListener = onYesClickListener;
    }

    /**
     * 设置关闭按钮的显示内容和监听
     *
     * @param onCloseClickListener
     */
    public void setCancleClickListener(String str,OnCloseClickListener onCloseClickListener) {
        if (!TextUtils.isEmpty(str)) {
            this.noStr = str;
        }
        this.onCloseClickListener = onCloseClickListener;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface OnYesClickListener {
        void onYesClick();
    }

    public interface OnNoClickListener {
        void onNoClick();
    }

    public interface OnCloseClickListener {
        void onCloseClick();
    }

}
