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
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.type.SmsType;
import com.hqmy.market.utils.CountDownTimerUtils;
import com.hqmy.market.utils.ImagePickerUtils;
import com.hqmy.market.utils.RegexUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加银行卡
 */
public class AddBandCardActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.et_bank_card_cardholder)
    EditText mBankCardCardholder;
    @BindView(R.id.et_bank_name)
    EditText mBankName;
    @BindView(R.id.et_bank_card_num)
    EditText mBankCardNum;
    @BindView(R.id.et_bank_sub_name)
    EditText mBankSubName;
    @BindView(R.id.et_bank_card_band_phone)
    EditText mBankCardBindPhone;
    @BindView(R.id.et_bank_card_img_code)
    EditText mBankCardImgCode;
    @BindView(R.id.iv_bank_card_icon_code)
    ImageView mBankCardImageCode;
    @BindView(R.id.et_bank_card_code)
    EditText mBankCardCode;
    @BindView(R.id.tv_bank_card_code)
    TextView mBankCardGetCode;
    /**
     * 图形验证码key
     */
    private String mImageCodeKey;
    CountDownTimerUtils countDownTimerUtils;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_bank_card;
    }

    @Override
    public void initView() {
        mTitleText.setText("添加银行卡");
    }

    @Override
    public void initData() {
        getImageCode();
    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.iv_title_back, R.id.iv_bank_card_icon_code
            , R.id.tv_bank_card_add, R.id.tv_bank_card_code
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_bank_card_add:
                addBankCard();
                break;
            case R.id.iv_bank_card_icon_code:
                getImageCode();
                break;
            case R.id.tv_bank_card_code:
                getNoteCode();
                break;
        }
    }

    /**
     * 获取图形验证码
     */
    private void getImageCode() {
        DataManager.getInstance().getImageCode(new DefaultSingleObserver<CaptchaImgDto>() {
            @Override
            public void onSuccess(CaptchaImgDto object) {
                mImageCodeKey = object.getKey();
                LogUtil.i(TAG, "RxLog-Thread: onSuccess() imgKey= " + object.getKey());
                Bitmap bitmap = ImagePickerUtils.getInstances().base64ToBitmap(object.getImg());
                mBankCardImageCode.setImageBitmap(bitmap);
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                LogUtil.i(TAG, "RxLog-Thread: onError() = " + Long.toString(Thread.currentThread().getId()));
            }
        });
    }

    private void addBankCard() {
        if (!verify()) {
            return;
        }
        if(!RegexUtils.isMobileExact(mBankCardBindPhone.getText().toString().trim())){
            ToastUtil.showToast("请输入合法手机号");
            return;
        }
        if(!RegexUtils.checkBankCard(mBankCardBindPhone.getText().toString().trim())){
            ToastUtil.showToast("请输入正确的卡号");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("name", mBankCardCardholder.getText().toString());
        map.put("branch", mBankSubName.getText().toString());
        map.put("phone", mBankCardBindPhone.getText().toString());
        map.put("code", mBankCardCode.getText().toString());
        map.put("number", mBankCardNum.getText().toString());
        DataManager.getInstance().addBankCard(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                if (!ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                } else {
                    finish();
                }
            }
        }, map);

    }

    /**
     * 获取短信验证码
     */
    private void getNoteCode() {
        if (TextUtils.isEmpty(mBankCardBindPhone.getText().toString().trim())) {
            ToastUtil.showToast("请输入银行预留手机号");
            return;
        }
        if (TextUtils.isEmpty(mBankCardImgCode.getText().toString().trim())) {
            ToastUtil.showToast("请输入图形码");
            return;
        }
        GetSmsCode getSms = new GetSmsCode();
        getSms.setPhone(mBankCardBindPhone.getText().toString().trim());
        getSms.setCaptcha_key(mImageCodeKey);
        getSms.setType(SmsType.ADD_BAND_CARD.getType());
        getSms.setCaptcha_code(mBankCardImgCode.getText().toString().trim());

        DataManager.getInstance().getSmsCode(new DefaultSingleObserver<String>() {
            @Override
            public void onSuccess(String object) {
                LogUtil.i(TAG, "RxLog-Thread: onSuccess() = " + Long.toString(Thread.currentThread().getId()));

                countDownTimerUtils = new CountDownTimerUtils(AddBandCardActivity.this, mBankCardGetCode, 60000, 1000);
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

    private boolean verify() {
        if (TextUtils.isEmpty(mBankCardCardholder.getText().toString().trim())) {
            ToastUtil.showToast("请输入持卡人姓名");
            return false;
        }

        if (TextUtils.isEmpty(mBankName.getText().toString().trim())) {
            ToastUtil.showToast("请输入银行名称");
            return false;
        }
        if (TextUtils.isEmpty(mBankCardNum.getText().toString().trim())) {
            ToastUtil.showToast("请输入银行卡号");
            return false;
        }
        if (TextUtils.isEmpty(mBankSubName.getText().toString().trim())) {
            ToastUtil.showToast("请输入支行");
            return false;
        }
        if (TextUtils.isEmpty(mBankCardBindPhone.getText().toString().trim())) {
            ToastUtil.showToast("请输入银行预留手机号");
            return false;
        }
        if (TextUtils.isEmpty(mBankCardImgCode.getText().toString().trim())) {
            ToastUtil.showToast("请输入图形码");
            return false;
        }
        if (TextUtils.isEmpty(mBankCardCode.getText().toString().trim())) {
            ToastUtil.showToast("请输入验证码");
            return false;
        }
        return true;

    }
}
