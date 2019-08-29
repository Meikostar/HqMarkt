package com.hqmy.market.view.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.hqmy.market.R;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 分享商品
 * Created by dahai on 2019/01/25.
 */
public class ShareProductDialog extends Dialog {
    private Context mContext;
    @BindView(R.id.iv_share_product_img)
    ImageView iv_share_product_img;
    @BindView(R.id.iv_product_img)
    ImageView iv_product_img;
    @BindView(R.id.ll_product_img)
    RelativeLayout ll_product_img;


    public ShareProductDialog(Context context, String imgUrl, String QeCodeUrl) {
        super(context, R.style.dialog_with_alpha1);
//        setCanceledOnTouchOutside(false);//设置点击外部不可以取消;
        this.mContext = context;
        setContentView(R.layout.dialog_share_product_img);
        ButterKnife.bind(this);
        initView();
        GlideUtils.getInstances().loadNormalImg(mContext, iv_product_img, imgUrl, R.mipmap.img_default_6);
        GlideUtils.getInstances().loadNormalImg(mContext, iv_share_product_img, QeCodeUrl, R.mipmap.img_default_6);
        bindClickEvent(ll_product_img, () -> {
            hide();
        });
    }

    private void initView() {
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (display.getWidth());
        p.height = (display.getHeight());
        Window window = getWindow();
        window.setAttributes(p);
        window.setGravity(Gravity.TOP);
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
