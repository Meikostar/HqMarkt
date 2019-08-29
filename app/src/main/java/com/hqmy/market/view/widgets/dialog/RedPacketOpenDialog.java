package com.hqmy.market.view.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.hqmy.market.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 拆红包
 * Created by Administrator on 2017/2/15.
 */

public class RedPacketOpenDialog extends Dialog {

    @BindView(R.id.rl_red_packet_open)
    RelativeLayout rl_red_packet_open;
    private OnRedPacketClickListener listener;

    public RedPacketOpenDialog(Context context, OnRedPacketClickListener onClickListener) {
        super(context, R.style.dialog_with_alpha);
        //按空白处不能取消动画
//        setCanceledOnTouchOutside(false);
        this.listener = onClickListener;
        setContentView(R.layout.dialog_red_packet_layout);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {
    }

    private void initListener() {
        bindClickEvent(rl_red_packet_open, () -> {
        if (listener != null) {
            listener.onRedPacketClick();
        }
        dismiss();
    });
    }



    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface OnRedPacketClickListener {
        void onRedPacketClick();
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
