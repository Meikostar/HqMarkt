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
 * 选择相片
 * Created by dahai on 2018/12/18.
 */
public class PhotoSelectDialog extends Dialog {
    @BindView(R.id.photo_select_img)
    TextView photo_select_img;
    @BindView(R.id.photo_select_camera)
    TextView photo_select_camera;
    @BindView(R.id.photo_select_cancel)
    TextView photo_select_cancel;
    private PhotoSelectListener photoSelectListener;
    private Context mContext;

    public PhotoSelectDialog(Context context, PhotoSelectListener listener) {
        super(context, R.style.dialog_with_alpha);
//        setCanceledOnTouchOutside(false);//设置点击外部不可以取消;
        this.mContext = context;
        this.photoSelectListener = listener;
        setContentView(R.layout.dialog_photo_select);
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
        bindClickEvent(photo_select_img, () -> {
            if(null != photoSelectListener){
                photoSelectListener.callbackPhotoSelect(1);
            }
            this.cancel();
        });
        bindClickEvent(photo_select_camera, () -> {
            if(null != photoSelectListener){
                photoSelectListener.callbackPhotoSelect(2);
            }
            this.cancel();
        });
        bindClickEvent(photo_select_cancel, () -> {
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

    public interface PhotoSelectListener {
        void callbackPhotoSelect(int index);
    }


}
