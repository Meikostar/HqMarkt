package com.hqmy.market.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.base.BaseApplication;
import com.hqmy.market.bean.CaptchaImgDto;
import com.hqmy.market.bean.GetSmsCode;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.type.SmsType;
import com.hqmy.market.utils.CountDownTimerUtils;
import com.hqmy.market.utils.ImagePickerUtils;
import com.hqmy.market.utils.ShareUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置支付密码
 */
public class SettingPasswordActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView       tvTitleText;
    @BindView(R.id.et_title_1)
    EditText       etTitle1;
    @BindView(R.id.et_title_2)
    EditText       etTitle2;
    @BindView(R.id.et_title_3)
    EditText       etTitle3;
    @BindView(R.id.et_title_4)
    EditText       etTitle4;
    @BindView(R.id.iv_register_icon_code)
    ImageView      ivRegisterIconCode;
    @BindView(R.id.tv_register_note_code)
    TextView       tvRegisterNoteCode;
    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.tv_title_1)
    TextView       tvTitle1;
    @BindView(R.id.line1)
    View           line1;
    @BindView(R.id.rl_bg1)
    RelativeLayout rlBg1;
    @BindView(R.id.tv_title_2)
    TextView       tvTitle2;
    @BindView(R.id.line2)
    View           line2;
    @BindView(R.id.rl_bg2)
    RelativeLayout rlBg2;
    @BindView(R.id.tv_title_0)
    TextView       tvTitle0;
    @BindView(R.id.et_title_0)
    EditText       etTitle0;
    @BindView(R.id.rl_bg3)
    RelativeLayout rlBg3;
    @BindView(R.id.tv_title_3)
    TextView       tvTitle3;
    @BindView(R.id.tv_title_4)
    TextView       tvTitle4;
    @BindView(R.id.tv_confirm)
    TextView       tvConfirm;
    @BindView(R.id.tv_forget)
    TextView       tv_forget;

    /**
     * 图形验证码key
     */
    private String mImageCodeKey;
    CountDownTimerUtils countDownTimerUtils;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting_password;
    }

    @Override
    public void initView() {
        if( BaseApplication.isSetPay ==1){
            tvTitleText.setText("修改支付密码");
            tvTitle3.setText("新  密  码");
            rlBg1.setVisibility(View.GONE);
            rlBg2.setVisibility(View.GONE);
            rlBg3.setVisibility(View.VISIBLE);
            tv_forget.setVisibility(View.VISIBLE);
        }else {
            rlBg3.setVisibility(View.GONE);
            tv_forget.setVisibility(View.GONE);
            tvTitleText.setText("设置支付密码");
            tvTitle3.setText("支付密码");
        }

    }

    @Override
    public void initData() {
        getImageCode();
    }

    private void getData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void initListener() {
        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SettingPasswordActivity.this,ForgetPayPasswordActivity.class),100);
            }
        });
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
                ivRegisterIconCode.setImageBitmap(bitmap);
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                LogUtil.i(TAG, "RxLog-Thread: onError() = " + Long.toString(Thread.currentThread().getId()));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK){
            finish();
        }
    }

    /**
     * 获取短信验证码
     */
    private void getNoteCode() {
        String phone = ShareUtil.getInstance().getString(Constants.USER_PHONE, "");
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("手机号为空");
            return;
        }
        GetSmsCode getSms = new GetSmsCode();
        getSms.setPhone(phone);
        getSms.setCaptcha_key(mImageCodeKey);
        getSms.setType(SmsType.SET_PAY_PASSWORD.getType());
        getSms.setCaptcha_code(etTitle1.getText().toString().trim());

        DataManager.getInstance().getSmsCodes(new DefaultSingleObserver<String>() {
            @Override
            public void onSuccess(String object) {
                LogUtil.i(TAG, "RxLog-Thread: onSuccess() = " + Long.toString(Thread.currentThread().getId()));
                countDownTimerUtils = new CountDownTimerUtils(SettingPasswordActivity.this, tvRegisterNoteCode, 60000, 1000);
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

    private void confirm() {
        Map<String, Object> map = new HashMap<>();
        if( BaseApplication.isSetPay ==1){
            if (TextUtils.isEmpty(etTitle0.getText().toString())) {
                ToastUtil.showToast("请输入原支付密码");
                return;
            }
            if (etTitle0.getText().toString().length()!=6) {
                ToastUtil.showToast("请输入6位原支付密码");
                return;
            }
            if (TextUtils.isEmpty(etTitle3.getText().toString())) {
                ToastUtil.showToast("请输入支付密码");
                return;
            }
            if (etTitle3.getText().toString().length()!=6) {
                ToastUtil.showToast("请输入6位支付密码");
                return;
            }
            if (TextUtils.isEmpty(etTitle4.getText().toString())) {
                ToastUtil.showToast("请重复输入支付密码");
                return;
            }
            if (!etTitle3.getText().toString().equals(etTitle4.getText().toString())) {
                ToastUtil.showToast("两次密码输入不一致");
                return;
            }
            map.put("old_pay_password", etTitle0.getText().toString());
            map.put("pay_password", etTitle3.getText().toString());
        }else {
            if (TextUtils.isEmpty(etTitle1.getText().toString())) {
                ToastUtil.showToast("请输入图形验证码");
                return;
            }
            if (TextUtils.isEmpty(etTitle2.getText().toString())) {
                ToastUtil.showToast("请输入短信验证码");
                return;
            }
            if (TextUtils.isEmpty(etTitle3.getText().toString())) {
                ToastUtil.showToast("请输入支付密码");
                return;
            }
            if (TextUtils.isEmpty(etTitle4.getText().toString())) {
                ToastUtil.showToast("请重复输入支付密码");
                return;
            }
            if (!etTitle3.getText().toString().equals(etTitle4.getText().toString())) {
                ToastUtil.showToast("两次密码输入不一致");
                return;
            }
            map.put("code", etTitle2.getText().toString());
            map.put("pay_password", etTitle3.getText().toString());
        }




        showLoadDialog();
        DataManager.getInstance().setPayPassword(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                dissLoadDialog();
                ToastUtil.showToast("设置成功");
                BaseApplication.isSetPay=1;
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("设置成功");
                    BaseApplication.isSetPay=1;
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
//                    ToastUtil.showToast(ApiException.getInstance().getErrorMsg());
                }
            }
        }, map);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_confirm, R.id.iv_register_icon_code, R.id.tv_register_note_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_confirm:
                confirm();
                break;
            case R.id.iv_register_icon_code:
                //获取图形验证码
                getImageCode();
                break;
            case R.id.tv_register_note_code:
                //获取短信验证码
                getNoteCode();
                break;
        }
    }


}
