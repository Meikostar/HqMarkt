package com.hqmy.market.view.activity;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CaptchaImgDto;
import com.hqmy.market.bean.GetSmsCode;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.type.SmsType;
import com.hqmy.market.utils.CountDownTimerUtils;
import com.hqmy.market.utils.ImagePickerUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;

    @BindView(R.id.et_forget_account_number)
    EditText  mForgetAccountNumber;
    /**图片验证码*/
    @BindView(R.id.et_forget_icon_code)
    EditText  mForgetIconCode;
    /**短信验证码*/
    @BindView(R.id.et_forget_note_code)
    EditText  mForgetNoteCode;
    /**短信验证码*/
    @BindView(R.id.tv_forget_note_code)
    TextView  mForgetNoteTextCode;
    /**密码*/
    @BindView(R.id.et_forget_password)
    EditText  mForgetPassword;
    @BindView(R.id.iv_forget_icon_code)
    ImageView mForgetIcon;
    /**图形验证码key*/
    private String mImageCodeKey;
    CountDownTimerUtils countDownTimerUtils;
    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void initView() {
        mTitleText.setText("忘记密码");
    }

    @Override
    public void initData() {
        getImageCode();
    }

    @Override
    public void initListener() {

    }

    /**
     * 加载图形验证码
     */
    private void getImageCode() {
        DataManager.getInstance().getImageCode(new DefaultSingleObserver<CaptchaImgDto>() {
            @Override
            public void onSuccess(CaptchaImgDto object) {
                mImageCodeKey = object.getKey();
                LogUtil.i(TAG, "RxLog-Thread: onSuccess() imgKey= " + object.getKey());
                Bitmap bitmap = ImagePickerUtils.getInstances().base64ToBitmap(object.getImg());
                mForgetIcon.setImageBitmap(bitmap);
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                LogUtil.i(TAG, "RxLog-Thread: onError() = " + Long.toString(Thread.currentThread().getId()));
            }
        });
    }

    /**
     * 获取短信验证码
     */
    private void getNoteCode() {
        GetSmsCode getSms = new GetSmsCode();

        getSms.setPhone(mForgetAccountNumber.getText().toString().trim());
        getSms.setCaptcha_key(mImageCodeKey);
        getSms.setType(SmsType.FIND_PASSWORD.getType());
        getSms.setCaptcha_code(mForgetIconCode.getText().toString().trim());

        DataManager.getInstance().getSmsCode(new DefaultSingleObserver<String>() {
            @Override
            public void onSuccess(String object) {
                LogUtil.i(TAG, "RxLog-Thread: onSuccess() = " + Long.toString(Thread.currentThread().getId()));
                countDownTimerUtils = new CountDownTimerUtils(ForgetPasswordActivity.this, mForgetNoteTextCode, 60000, 1000);
                countDownTimerUtils.start();
            }

            @Override
            public void onError(Throwable throwable) {
                if (!ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                    //                    tvVerification.setText("获取验证码");
                    if (countDownTimerUtils != null) {
                        countDownTimerUtils.cancel();
                        countDownTimerUtils.onFinish();
                    }
                } else {
                    ToastUtil.showToast("已发送验证码");
                }


            }
        }, getSms);
    }

    /**
     * 注册验证
     */
    private boolean verifyConditionRegister() {
        if(TextUtils.isEmpty(mForgetAccountNumber.getText().toString().trim())){
            ToastUtil.showToast("请输入用户名或手机号");
            return false;
        }

        if(TextUtils.isEmpty(mForgetIconCode.getText().toString().trim())){
            ToastUtil.showToast("请输入图形验证码");
            return false;
        }
        if(TextUtils.isEmpty(mForgetNoteCode.getText().toString().trim())){
            ToastUtil.showToast("请输入短信验证码");
            return false;
        }
        return true;
    }
    /**
     * 短信验证码验证
     */
    private boolean verifyConditionCode() {
        if(TextUtils.isEmpty(mForgetAccountNumber.getText().toString().trim())){
            ToastUtil.showToast("请输入手机号");
            return false;
        }

        if(TextUtils.isEmpty(mForgetIconCode.getText().toString().trim())){
            ToastUtil.showToast("请输入图形验证码");
            return false;
        }
        return true;
    }

    @OnClick({R.id.iv_title_back,
            R.id.iv_forget_icon_code,
            R.id.tv_forget_note_code,
            R.id.tv_confirm
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.iv_forget_icon_code://刷新图形验证码
                getImageCode();
                break;
            case R.id.tv_forget_note_code://获取短信验证码
                if(verifyConditionCode()){
                    getNoteCode();
                }
                break;
            case R.id.tv_confirm://忘记密码
                if(verifyConditionRegister()){
                    confirm();
                }
                break;
            case R.id.tv_register_protocol://点击注册协议
                break;
        }

    }

    private void confirm() {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", mForgetAccountNumber.getText().toString().trim());
        map.put("code", mForgetNoteCode.getText().toString().trim());
        map.put("password", mForgetPassword.getText().toString().trim());
        showLoadDialog();
        DataManager.getInstance().resetPwd(new DefaultSingleObserver<String>() {
            @Override
            public void onSuccess(String s) {
                dissLoadDialog();
                ToastUtil.showToast("修改成功");
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("修改成功");
                    finish();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, map);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimerUtils != null) {
            countDownTimerUtils.cancel();
        }
    }
}
