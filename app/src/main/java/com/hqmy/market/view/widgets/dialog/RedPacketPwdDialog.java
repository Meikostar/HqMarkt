package com.hqmy.market.view.widgets.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.hqmy.market.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * Created by Administrator on 2017/2/15.
 */

public class RedPacketPwdDialog extends Dialog {

    @BindView(R.id.et_red_packet_pwd)
    EditText et_red_packet_pwd;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;
    private OnConfirmClickListener listener;

    public RedPacketPwdDialog(Activity context, OnConfirmClickListener onClickListener) {
        super(context, R.style.dialog_with_alpha);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        this.listener = onClickListener;
        setContentView(R.layout.dialog_red_packet_pwd_layout);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {
    }

    private void initListener() {
        bindClickEvent(btn_confirm, () -> {
        if (listener != null) {
            listener.onConfirmClick(et_red_packet_pwd.getText().toString());
        }
        dismiss();
    });
    }



    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface OnConfirmClickListener {
        void onConfirmClick(String pwd);
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

    protected void bindClickEvent(View view, final Action action, long frequency) {
        Observable<Object> observable = RxView.clicks(view);
        if (frequency > 0) {
            observable.throttleFirst(frequency, TimeUnit.MILLISECONDS);
        }
        observable.subscribe(trigger -> action.run());
    }


}
