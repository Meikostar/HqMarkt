package com.hqmy.market.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CaptchaImgDto;
import com.hqmy.market.bean.GetSmsCode;
import com.hqmy.market.bean.PersonalInfoDto;
import com.hqmy.market.bean.RegisterDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.eventbus.LogoutEvent;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.request.UserRegister;
import com.hqmy.market.type.SmsType;
import com.hqmy.market.utils.CountDownTimerUtils;
import com.hqmy.market.utils.ImagePickerUtils;
import com.hqmy.market.utils.RegexUtils;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XgPwdActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView       mTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.et_pwd1)
    EditText       etPwd1;
    @BindView(R.id.tv_register_password)
    TextView       tvRegisterPassword;
    @BindView(R.id.et_register_password)
    EditText       etRegisterPassword;
    @BindView(R.id.tv_sure)
    TextView       tvSure;
    @BindView(R.id.cb_register_check_box)
    CheckBox       cbRegisterCheckBox;
    @BindView(R.id.tv_register_protocol)
    TextView       tvRegisterProtocol;



    @Override
    public int getLayoutId() {
        return R.layout.activity_xg_pwd;
    }

    @Override
    public void initView() {
        mTitleText.setText("修改密码");
    }

    @Override
    public void initData() {
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etPwd1.getText().toString().trim())){
                    ToastUtil.showToast("请输入不少于6位数密码");
                    return ;
                }

                if(TextUtils.isEmpty(etRegisterPassword.getText().toString().trim())||etRegisterPassword.getText().toString().trim().length()<6){
                    ToastUtil.showToast("请输入不少于6位数密码");
                    return ;
                }
                commitSex(etPwd1.getText().toString().trim(),etRegisterPassword.getText().toString().trim());
            }
        });
    }


    @Override
    public void initListener() {

    }









    private void commitSex(String old_password,String password) {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("password", "" + password);
        map.put("old_password", "" + old_password);
        showLoadDialog();
        DataManager.getInstance().modifUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                ToastUtil.showToast("修改成功");
                ShareUtil.getInstance().cleanUserInfo();
                EventBus.getDefault().post(new LogoutEvent());
                Bundle bundle = new Bundle();
                bundle.putString("log_out", "LOG_OUT");
                gotoActivity(LoginActivity.class, true, bundle);
                dissLoadDialog();

            }
            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                dissLoadDialog();

            }
        }, map);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
