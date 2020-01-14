package com.hqmy.market.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.LoginDto;
import com.hqmy.market.bean.PersonalInfoDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.eventbus.FinishEvent;
import com.hqmy.market.eventbus.LogoutEvent;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.request.UserRegister;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView  mTitleText;
    @BindView(R.id.iv_title_back)
    ImageView mBackImage;

    @BindView(R.id.et_login_account_number)
    EditText mAccountNumber;
    @BindView(R.id.et_login_password)
    EditText mPassword;

    @BindView(R.id.cb_remember_password)
    CheckBox mRememberPassword;
    private boolean isRememberPasswordChecked;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FinishEvent event) {
        finish();
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);

        mTitleText.setText("登录");
        mBackImage.setImageResource(R.mipmap.arrow_topbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initData() {
        isRememberPasswordChecked = ShareUtil.getInstance().getBoolean(Constants.REMEMBER_PASSWORD,false);
//        actionbar.getBackView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finishAll();
//            }
//        });
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("log_out").equals("LOG_OUT")){
                isRememberPasswordChecked = true;
            }
        }
        if (isRememberPasswordChecked){        //若记住密码，则在SharedPreferences中获取进行自动登录
            String username = ShareUtil.getInstance().getString(Constants.USER_ACCOUNT_NUMBER,"");
            String password = ShareUtil.getInstance().getString(Constants.USER_PASSWORD,"");
            mAccountNumber.setText(username);
            mPassword.setText(password);
            mRememberPassword.setChecked(true);
            //记住密码自动登录
//            login(username,password);
        }else{
            isRememberPasswordChecked = false;
            mRememberPassword.setChecked(false);
            ShareUtil.getInstance().saveBoolean(Constants.REMEMBER_PASSWORD,false);
            ShareUtil.getInstance().save(Constants.USER_PASSWORD,"");
        }
        mRememberPassword.setChecked(true);
        int anInt = ShareUtil.getInstance().getInt(Constants.IS_PASS, -1);
        if(anInt==0){
            ToastUtil.showToast("审核中，请耐心等待审核!");
        }else if(anInt==2) {
            ToastUtil.showToast("审核失败，请重新提交审核");
            gotoActivity(AfterLoginActivity.class, false);
        }

    }

    @Override
    public void initListener() {
        mRememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRememberPasswordChecked  =isChecked;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAccountNumber.setText(ShareUtil.getInstance().getString(Constants.USER_ACCOUNT_NUMBER, ""));
    }

    @OnClick({R.id.iv_title_back,
            R.id.tv_login,
            R.id.tv_login_forget_password,
            R.id.tv_login_register
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finishAll();
                break;
            case R.id.tv_login:
                if (verifyConditionLogin()) {
                    login(mAccountNumber.getText().toString().trim(), mPassword.getText().toString());
                }
                break;
            case R.id.tv_login_forget_password:
                forgetPassword();
                break;
            case R.id.tv_login_register:
                register();
                break;
        }

    }

    private void register() {

        startActivityForResult(new Intent(this,RegisterActivity.class),100);
    }

    private void forgetPassword() {
        gotoActivity(ForgetPasswordActivity.class);
    }
    private String initvate;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK){
            String phone = data.getStringExtra("phone");
            String phones = data.getStringExtra("phones");
            if(TextUtil.isNotEmpty(phones)){
                initvate=phones;
            }
            mAccountNumber.setText(phone);
        }
    }

    /**
     * 注册验证
     */
    private boolean verifyConditionLogin() {
        if(TextUtils.isEmpty(mAccountNumber.getText().toString().trim())){
            ToastUtil.showToast("请输入账号");
            return false;
        }

        if(TextUtils.isEmpty(mPassword.getText().toString().trim())){
            ToastUtil.showToast("请输入密码");
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        finishAll();
    }
    private void login(String username, String password) {
        UserRegister userRegister = new UserRegister();
        userRegister.setPhone(username);
        userRegister.setPassword(password);
        userRegister.setInclude("user.userExt");
        showLoadDialog();
        DataManager.getInstance().login(new DefaultSingleObserver<LoginDto>() {
            @Override
            public void onSuccess(LoginDto object) {
                dissLoadDialog();
                loginSuccess(object,false);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, userRegister);
    }

    private void loginSuccess(LoginDto loginDto, boolean wxLogin) {
//        ShareUtil.getInstance().save(Constants.USER_TOKEN, loginDto.getAccess_token());
        String phone = "";
        if (loginDto.getUser() != null && loginDto.getUser().getData() != null) {
            PersonalInfoDto personalInfoDto = loginDto.getUser().getData();
            phone = personalInfoDto.getPhone();
            ShareUtil.getInstance().saveBoolean(Constants.NO_LOGIN_SUCCESS, true);
            ShareUtil.getInstance().save(Constants.USER_ID,   personalInfoDto.getId());
            ShareUtil.getInstance().save(Constants.USER_PHONE,personalInfoDto.getPhone());
            ShareUtil.getInstance().save(Constants.USER_HEAD, personalInfoDto.getAvatar());
            if(personalInfoDto.getName() != null){
                ShareUtil.getInstance().save(Constants.USER_NAME, personalInfoDto.getName());
            }else{
                ShareUtil.getInstance().save(Constants.USER_NAME, personalInfoDto.getPhone());
            }
            ShareUtil.getInstance().save(Constants.USER_TOKEN,loginDto.getAccess_token());
            ShareUtil.getInstance().save(Constants.APP_USER_KEY, personalInfoDto.getIm_token());
            String nameStr = null;
            String avatarStr = null;
            if(personalInfoDto.getName() != null) {
                nameStr = personalInfoDto.getName();
            }else{
                nameStr = personalInfoDto.getPhone();
            }
            if(personalInfoDto.getAvatar() != null){
                avatarStr = Constants.WEB_IMG_URL_UPLOADS + personalInfoDto.getAvatar();
            }else{
                avatarStr = "android.resource://" + getApplicationContext().getPackageName() + "/" +R.mipmap.ic_head_img;
            }
        }
          ShareUtil.getInstance().save(Constants.USER_ACCOUNT_NUMBER, mAccountNumber.getText().toString().trim());
          if(mRememberPassword.isChecked()) {
              ShareUtil.getInstance().save(Constants.USER_PASSWORD, mPassword.getText().toString().trim());
              ShareUtil.getInstance().saveBoolean(Constants.REMEMBER_PASSWORD,true);

          }else{
              ShareUtil.getInstance().saveBoolean(Constants.REMEMBER_PASSWORD,false);
              ShareUtil.getInstance().save(Constants.USER_PASSWORD,"");
          }
          /*if (wxLogin && TextUtils.isEmpty(phone)) {//去绑定手机号码
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.INTENT_FLAG, true);
            gotoActivity(UpdatePhoneActivity.class, false, bundle, Constants.INTENT_REQUESTCODE_BIND_PHONE_SUCCESS);
            return;
          }*/
          //setResult(Activity.RESULT_OK);
          //finish();
          if(loginDto.getUser().getData().userExt!=null&&loginDto.getUser().getData() .userExt.data!=null){
              ShareUtil.getInstance().saveInt(Constants.IS_PASS, loginDto.getUser().getData().userExt.data.status);
              if(loginDto.getUser().getData().userExt.data.status==1){
                  ShareUtil.getInstance().saveInt(Constants.IS_PASS, 1);
                  gotoActivity(MainActivity.class, true);
              }else if(loginDto.getUser().getData().userExt.data.status==2) {
                  ToastUtil.showToast("审核失败，请重新提交审核");
                  Intent intent = new Intent(LoginActivity.this, AfterLoginActivity.class);
//                  ShareUtil.getInstance().cleanUserInfo();
//                  EventBus.getDefault().post(new LogoutEvent());
                  if(TextUtil.isNotEmpty(initvate)){
                      intent.putExtra("phone",initvate);
                  }
                  startActivity(intent);
//                  gotoActivity(AfterLoginActivity.class, false);
              }else if(loginDto.getUser().getData().userExt.data.status==0){
                  ToastUtil.showToast("审核中，请耐心等待审核!");
                  ShareUtil.getInstance().cleanUserInfo();
                  EventBus.getDefault().post(new LogoutEvent());
              }


          }else {
              ToastUtil.showToast("审核失败，请重新提交审核");

              Intent intent = new Intent(LoginActivity.this, AfterLoginActivity.class);

              if(TextUtil.isNotEmpty(initvate)){
                  intent.putExtra("phone",initvate);
              }
              startActivity(intent);
          }

          LogUtil.i(TAG, "RxLog-Thread: onSuccess() = " + Long.toString(Thread.currentThread().getId()));
    }


}
