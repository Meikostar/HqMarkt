package com.hqmy.market.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.base.BaseApplication;
import com.hqmy.market.bean.PersonalInfoDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.eventbus.LogoutEvent;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.utils.DataCleanManager;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.SettingPasswordActivity;
import com.hqmy.market.view.widgets.dialog.BaseDialog;
import com.hqmy.market.view.widgets.dialog.ConfirmDialog;

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
    @BindView(R.id.tv_pay)
    TextView       tv_pay;

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
        tvHc.setText(DataCleanManager.getExternalCacheSize(AccountSettingActivity.this));
    }


    @Override
    public void initData() {
        if( BaseApplication.isSetPay ==1){
            tv_pay.setText("修改支付密码");
        }else {
            tv_pay.setText("设置支付密码");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if( BaseApplication.isSetPay ==1){
            tv_pay.setText("修改支付密码");
        }else {
            tv_pay.setText("设置支付密码");
        }
    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.tv_dz
            , R.id.iv_title_back
            , R.id.tv_pay
            , R.id.tv_ls
            , R.id.tv_dp
            , R.id.tv_bbx
            , R.id.rl_sex_container
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
            case R.id.tv_pay:
                gotoActivity(SettingPasswordActivity.class);
                break;
            case R.id.tv_ls://流水
                gotoActivity(IncomeActivity.class);

                break;
            case R.id.tv_dp://服务协议
                Intent intent = new Intent(this, WebUtilsActivity.class);
                intent.putExtra("type",5);
                startActivity(intent);
                break;
            case R.id.tv_bbx://版本信息
                gotoUserInfoActivity();
                break;
            case R.id.rl_sex_container://缓存

                showLoginHintDialog();
                break;

            case R.id.tv_exit_login:
                logout();
                break;
        }
    }


    private void showLoginHintDialog() {
        ConfirmDialog dialog = new ConfirmDialog(AccountSettingActivity.this);
        dialog.setTitle("温馨提示");
        dialog.setMessage("是否清除缓存?");
        dialog.setYesOnclickListener("确定", new BaseDialog.OnYesClickListener() {
            @Override
            public void onYesClick() {
                dialog.dismiss();
                showLoadDialog();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataCleanManager.deleteFolderFile(getExternalCacheDir().getPath(), true);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (DataCleanManager.getExternalCacheSize(AccountSettingActivity.this).equals("0.0Byte")) {

                                    ToastUtil.showToast("已清除");
                                } else {
                                    ToastUtil.showToast("清除成功" );
                                    tvHc.setText(DataCleanManager.getExternalCacheSize(AccountSettingActivity.this));

                                }
                                dissLoadDialog();

                            }
                        });
                    }
                }).start();

                DataCleanManager.clearAllCache(AccountSettingActivity.this);
                try {
                    String size = DataCleanManager.getTotalCacheSize(AccountSettingActivity.this);
                    tvHc.setText(size);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.setCancleClickListener("取消", new BaseDialog.OnCloseClickListener() {
            @Override
            public void onCloseClick() {
                String cacheToken = ShareUtil.getInstance().getString(Constants.APP_USER_KEY, "");

            }
        });
        dialog.show();
    }
    private void logout() {
        ToastUtil.showToast("退出登录成功");
        ShareUtil.getInstance().cleanUserInfo();
        EventBus.getDefault().post(new LogoutEvent());
        Bundle bundle = new Bundle();
        bundle.putString("log_out", "LOG_OUT");
        gotoActivity(LoginActivity.class, true, bundle);
    }

    private void gotoUserInfoActivity() {
        Intent intent = new Intent(this, AboutUsActivity.class);

        startActivity(intent);
    }




}
