package com.hqmy.market.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.hqmy.market.common.Constants;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.widgets.dialog.LoadingDialog;

import java.util.concurrent.TimeUnit;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutId(), null);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initListener();
        initMapView(savedInstanceState);
    }

    protected void initMapView(Bundle savedInstanceState) {
    }


    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();
    protected abstract void initListener();

    /**
     * 是否需要登录跳转
     *
     * @param clz  跳转的Acticvity
     */
    protected void bindClickIsLoginJumpUiEvent( final Class<?> clz) {
        gotoActivity(!TextUtils.isEmpty(ShareUtil.getInstance().getString(Constants.USER_TOKEN, "")) ? clz : LoginActivity.class);
    }

    /**
     * 跳转activity
     *
     * @param clz
     */
    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (isCloseCurrentActivity)
            getActivity().finish();
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle bundle, int requestCode) {
        Intent intent = new Intent(getActivity(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        if (isCloseCurrentActivity)
            getActivity().finish();
    }

    protected void bindClickJumpUiEvent(View view, final Class<?> clz, long frequency) {
        bindClickEvent(view, () -> gotoActivity(clz), frequency);
    }

    protected void bindClickJumpUiEvent(View view, final Class<?> clz) {
        bindClickJumpUiEvent(view, clz, 1000);
    }


    protected void bindClickEvent(View view, final Action action) {
        RxView.clicks(view).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(trigger -> action.run());
    }

    protected void bindClickEvent(View view, final Action action, long frequency) {
        Observable<Object> observable = RxView.clicks(view);
        observable.throttleFirst(frequency, TimeUnit.MILLISECONDS)
                .subscribe(trigger -> action.run());
    }

    protected void showLoadDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.show(getActivity());
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        loadingDialog.show();
    }

    protected void dissLoadDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
