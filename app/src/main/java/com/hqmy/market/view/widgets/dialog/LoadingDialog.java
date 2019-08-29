package com.hqmy.market.view.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import com.hqmy.market.R;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * 加载动画
 * Created by uqduiba on 10/27/2017.
 */

public class LoadingDialog extends Dialog implements DialogInterface.OnCancelListener {

    private static LoadingDialog lastShowingDialog;

    private static int theme;

    private static int layout;

    private static int contentView;

    private static int animationView;

    private RotateAnimation rotateAnimation;

    private final List<Disposable> todoList;
    private LoadingListener loadingListener;

    protected LoadingDialog(Context context, String content) {
        super(context, LoadingDialog.theme);
        setContentView(LoadingDialog.layout);
        todoList = new ArrayList<>();
        initView(content);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lastShowingDialog = this;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        rotateAnimation.start();
    }

    // dismiss()调用此方法
    @Override
    protected void onStop() {
        lastShowingDialog = null;
        super.onStop();
        rotateAnimation.cancel();
        rotateAnimation.reset();
    }

    @Override
    public void hide() {
        super.hide();
        rotateAnimation.cancel();
        rotateAnimation.reset();
    }

    public static LoadingDialog show(Context context, int theme, int layout, int animationView, int contentView, String content) {

        LoadingDialog.theme = theme;
        LoadingDialog.layout = layout;
        LoadingDialog.animationView = animationView;
        LoadingDialog.contentView = contentView;

        LoadingDialog dialog;
        if (lastShowingDialog == null) {
            dialog = new LoadingDialog(context, content);
            dialog.show();
        } else {
            lastShowingDialog.show();
            ((TextView) lastShowingDialog.findViewById(contentView)).setText(content == null || content.equals("") ? "请稍后..." : content);
            dialog = lastShowingDialog;
        }
        return dialog;
    }

    public static LoadingDialog show(Context context, int theme, int layout, int animationView, int contentView) {
        return show(context, theme, layout, animationView, contentView, null);
    }

    public static LoadingDialog show(Context context) {
        return show(context, R.style.progress_dialog, R.layout.dialog_progress_layout, R.id.pb_load_img, R.id.id_tv_loadingmsg, null);
    }

    public void cancelDialog() {
        cancel();
    }

    public void stopLoading(Disposable disposable) {
        // 取消当前loading对应的异步操作
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        todoList.remove(disposable);
        if (todoList.isEmpty()) {
            if (this.isShowing())
                cancel();
        }
    }

    public void keepLoadingFor(Disposable disposable) {
        todoList.add(disposable);
    }

    private void initView(String content) {
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        setOnCancelListener(this);

        rotateAnimation = new RotateAnimation(0, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1500);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());

        findViewById(animationView).setAnimation(rotateAnimation);
        ((TextView) findViewById(contentView)).setText(content == null || content.equals("") ? "请稍后..." : content);

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (!todoList.isEmpty()) {
            Observable.fromIterable(todoList).forEach(Disposable::dispose);
        }
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setCallbackListener(LoadingListener listener) {
        this.loadingListener = listener;
    }

    public interface LoadingListener {
        void onLoadCompleted(int level);
    }


    public void setLoadinglevel(int loadingLevel) {
        if (null != loadingListener) {
            loadingListener.onLoadCompleted(loadingLevel);
        }
    }
}
