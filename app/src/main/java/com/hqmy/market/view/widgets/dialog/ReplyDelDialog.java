package com.hqmy.market.view.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.hqmy.market.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 朋友圈删除自己评论内容
 */
public class ReplyDelDialog extends Dialog {
    @BindView(R.id.tv_reply_del)
    TextView tv_reply_del;
    @BindView(R.id.tv_reply_del_cancel)
    TextView tv_reply_del_cancel;

    private ReplySelectListener photoSelectListener;
    private Context mContext;

    public ReplyDelDialog(Context context, ReplySelectListener listener) {
        super(context, R.style.dialog_with_alpha);
//        setCanceledOnTouchOutside(false);//设置点击外部不可以取消;
        this.mContext = context;
        this.photoSelectListener = listener;
        setContentView(R.layout.dialog_reply_del_layout);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (display.getWidth());
        Window window = getWindow();
        window.setAttributes(p);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomAnimStyle);
    }

    private void initListener() {
        bindClickEvent(tv_reply_del, () -> {
            if(null != photoSelectListener){
                photoSelectListener.delItem();
            }
            this.cancel();
        });

        bindClickEvent(tv_reply_del_cancel, () -> {
            this.cancel();
        });
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

    public interface ReplySelectListener {
        void delItem();
    }


}
