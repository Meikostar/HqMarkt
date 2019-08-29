package com.hqmy.market.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.OnlineApplyBean;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.RegexUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 在线报名
 */
public class ActionOnlineSingupActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView   mTitleText;
    @BindView(R.id.et_name)
    EditText   mName;
    @BindView(R.id.et_phone)
    EditText   mPhone;
    @BindView(R.id.et_id_card)
    EditText   mIdCard;
    @BindView(R.id.et_email)
    EditText   mEmail;
    @BindView(R.id.rg_sex)
    RadioGroup mRadioGroup;

    private String sex;
    private int mActionId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_action_online_singup;
    }

    @Override
    public void initView() {
        mTitleText.setText("在线报名");
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            mActionId = bundle.getInt(ActionDetailActivity.ACTION_ID);
        }
    }

    @Override
    public void initListener() {
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.rb_male){
                sex = "1";//男
            }else if(checkedId == R.id.rb_female){
                sex = "0";//女
            }
        });
    }

    @OnClick({R.id.iv_title_back
            ,R.id.tv_action_singup_submit
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_action_singup_submit:
                submit();
                break;
        }
    }

    private void submit() {
        if (TextUtils.isEmpty(mName.getText().toString().trim())) {
            ToastUtil.showToast("请输入姓名");
            return;
        }

        if (TextUtils.isEmpty(sex)) {
            ToastUtil.showToast("请选择性别");
            return;
        }

        if (TextUtils.isEmpty(mPhone.getText().toString().trim())) {
            ToastUtil.showToast("请输入电话号码");
            return;
        }

        if (!RegexUtils.isMobileExact(mPhone.getText().toString().trim())) {
            ToastUtil.showToast("请输入合法手机号");
            return;
        }

        if (TextUtils.isEmpty(mIdCard.getText().toString().trim())) {
            ToastUtil.showToast("请输入身份证号");
            return;
        }

        if (!(RegexUtils.isIDCard15(mIdCard.getText().toString().trim()) || RegexUtils.isIDCard18Exact(mIdCard.getText().toString().trim()))) {
            ToastUtil.showToast("请输入合法身份证号");
            return;
        }

        if (TextUtils.isEmpty(mEmail.getText().toString().trim())) {
            ToastUtil.showToast("请输入电子邮件");
            return;
        }

        if (!RegexUtils.isEmail(mEmail.getText().toString().trim())) {
            ToastUtil.showToast("请输入合法电子邮件");
            return;
        }

        OnlineApplyBean bean = new OnlineApplyBean();
        bean.setPage_id(mActionId);
        bean.setName(mName.getText().toString().trim());
        bean.setSex(sex);
        bean.setMobile(mPhone.getText().toString().trim());
        bean.setId_card(mIdCard.getText().toString().trim());
        bean.setEmail(mEmail.getText().toString().trim());
        DataManager.getInstance().onlineApply(new DefaultSingleObserver<HttpResult<Object>>(){
            @Override
            public void onSuccess(HttpResult<Object> objectHttpResult) {
                super.onSuccess(objectHttpResult);
                ToastUtil.showToast("报名成功");
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                if(ApiException.getInstance().isSuccess()){
                    ToastUtil.showToast("报名成功");
                }else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, bean);
    }
}
