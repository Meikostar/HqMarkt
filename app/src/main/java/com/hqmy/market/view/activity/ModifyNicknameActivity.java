package com.hqmy.market.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.PersonalInfoDto;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改昵称
 */
public class ModifyNicknameActivity extends BaseActivity {

    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.tv_title_right)
    TextView mRightText;
    @BindView(R.id.et_user_name)
    EditText mUserName;
    PersonalInfoDto mPersonalInfoDto;
    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_nickname;
    }

    @Override
    public void initView() {
        mTitleText.setText("修改昵称");
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText("完成");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @OnClick({  R.id.iv_title_back
            , R.id.tv_title_right
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finishActivity();
                break;
            case R.id.tv_title_right:
                commitUserName();
                break;
        }

    }

    private void commitUserName() {
        String name = mUserName.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            ToastUtil.showToast("请输入昵称");
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("name", "" + name);
        showLoadDialog();
        DataManager.getInstance().modifUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                dissLoadDialog();
                if (personalInfoDto != null) {
                    mPersonalInfoDto = personalInfoDto;
                }
                ToastUtil.showToast("修改成功");
                finish();
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(throwable.getMessage());
            }
        }, map);
    }

    private void finishActivity() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", mPersonalInfoDto);
        intent.putExtras(bundle);
        setResult(0, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }
}
