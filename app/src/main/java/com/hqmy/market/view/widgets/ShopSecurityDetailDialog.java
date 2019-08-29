package com.hqmy.market.view.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import com.jakewharton.rxbinding2.view.RxView;
import com.hqmy.market.R;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 商家保障说明
 * Created by dahai on 2018/12/18.
 */
public class ShopSecurityDetailDialog extends Dialog {
    private Context mContext;
    @BindView(R.id.iv_dialog_shop_security_submit)
    Button butSubmit;
    @BindView(R.id.iv_dialog_shop_security_clean)
    ImageView clean;

    public ShopSecurityDetailDialog(Context context) {
        super(context, R.style.dialog_with_alpha);
//        setCanceledOnTouchOutside(false);//设置点击外部不可以取消;
        this.mContext = context;
        setContentView(R.layout.dialog_shop_security_detail);
        ButterKnife.bind(this);
        initView();
        initData();
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
        bindClickEvent(butSubmit, () -> {
            hide();
        });
        bindClickEvent(clean, () -> {
            hide();
        });

    }

    private void initData() {
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
