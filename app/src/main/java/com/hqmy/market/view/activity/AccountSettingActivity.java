package com.hqmy.market.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.PersonalInfoDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.eventbus.LogoutEvent;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.utils.ShareUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 账户设置
 */
public class AccountSettingActivity extends BaseActivity {


    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView       mTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.tv_ls)
    TextView       tvLs;
    @BindView(R.id.tv_dz)
    TextView       tvDz;
    @BindView(R.id.tv_dp)
    TextView       tvDp;
    @BindView(R.id.tv_bbx)
    TextView       tvBbx;
    @BindView(R.id.tv_hc)
    TextView       tvHc;
    @BindView(R.id.rl_sex_container)
    RelativeLayout rlSexContainer;
    @BindView(R.id.tv_exit_login)
    TextView       tvExitLogin;

    @Override
    public int getLayoutId() {
        return R.layout.activity_account_setting;
    }

    @Override
    public void initView() {

        mTitleText.setText("设置");

    }



    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.tv_dz
            , R.id.iv_title_back
            , R.id.tv_ls
            , R.id.tv_dp
            , R.id.tv_bbx
            , R.id.tv_hc
            , R.id.tv_exit_login
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_dz:
                gotoActivity(ShippingAddressActivity.class);
                break;
            case R.id.tv_ls://流水
                break;
            case R.id.tv_dp://服务协议
                gotoUserInfoActivity();
                break;
            case R.id.tv_bbx://版本信息
                gotoUserInfoActivity();
                break;
            case R.id.tv_hc://缓存

                break;

            case R.id.tv_exit_login:
                logout();
                break;
        }
    }

    private void logout() {
        DataManager.getInstance().logout(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object obj) {
                super.onSuccess(obj);
                ShareUtil.getInstance().cleanUserInfo();
                gotoActivity(LoginActivity.class, true, null);
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("退出登录成功");
                    ShareUtil.getInstance().cleanUserInfo();
                    EventBus.getDefault().post(new LogoutEvent());
                    Bundle bundle = new Bundle();
                    bundle.putString("log_out", "LOG_OUT");
                    gotoActivity(LoginActivity.class, true, bundle);
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        });
    }

    private void gotoUserInfoActivity() {
        Intent intent = new Intent(this, UserInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", mPersonalInfoDto);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 0 && data != null) {
            PersonalInfoDto personalInfoDto = (PersonalInfoDto) data.getSerializableExtra("userInfo");
            if (personalInfoDto != null) {
                mPersonalInfoDto = personalInfoDto;
                setUserInfo();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
