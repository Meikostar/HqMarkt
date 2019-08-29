package com.hqmy.market.view.activity;

import android.view.View;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.utils.CallPhone;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 帮助中心
 */
public class HelpCenterActivity extends BaseActivity {

    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @Override
    public int getLayoutId() {
        return R.layout.activity_help_center;
    }

    @Override
    public void initView() {
        mTitleText.setText("帮助中心");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @OnClick({  R.id.iv_title_back
            ,R.id.rl_help_about_us
            ,R.id.rl_help_message_feedback
            ,R.id.rl_help_enterprise_profile
            ,R.id.rl_help_service_tel
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.rl_help_about_us://关于我们
                gotoActivity(AboutUsActivity.class);
                break;
            case R.id.rl_help_message_feedback://留言反馈
                ToastUtil.showToast("留言反馈");
                gotoActivity(MessageFeedbackActivity.class);
                break;
            case R.id.rl_help_enterprise_profile://企业简介
                gotoActivity(EnterpriseProfileActivity.class);
                break;
            case R.id.rl_help_service_tel://拨打电话
                callTel();
                break;
        }
    }

    /**
     * 拨打客服电话
     */
    private void callTel() {
        CallPhone.diallPhone(this, "0755-123456000");
    }

}
