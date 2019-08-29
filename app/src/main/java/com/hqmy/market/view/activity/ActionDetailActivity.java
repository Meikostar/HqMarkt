package com.hqmy.market.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.ActionBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 活动详情
 */
public class ActionDetailActivity extends BaseActivity {

    public static final String ACTION_ID = "ACTION_ID";
    @BindView(R.id.tv_title_text)
    TextView  mTitleText;
    @BindView(R.id.iv_action_detail_icon)
    ImageView mActionIcon;
    @BindView(R.id.tv_action_detail_name)
    TextView  mActionTitle;
    @BindView(R.id.tv_action_detail_content)
    TextView  mActionContent;
    @BindView(R.id.tv_action_detail_address)
    TextView  mActionAddress;
    @BindView(R.id.tv_action_detail_time)
    TextView  mActionTime;
    @BindView(R.id.tv_action_detail_phone)
    TextView  mActionPhone;
    @BindView(R.id.tv_action_detail_sponsor)
    TextView  mActionSponsor;
    private int mActionId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_action_detail;
    }

    @Override
    public void initView() {
        mTitleText.setText("活动详情");
    }

    @Override
    public void initData() {
        mActionId = getIntent().getExtras().getInt(ACTION_ID);
        getActionDetailData();
    }

    private void getActionDetailData() {
        DataManager.getInstance().getActionDetailData(new DefaultSingleObserver<HttpResult<ActionBean>>() {
            @Override
            public void onSuccess(HttpResult<ActionBean> httpResult) {
                super.onSuccess(httpResult);

                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        }, mActionId);
    }

    private void setData(HttpResult<ActionBean> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        ActionBean bean = httpResult.getData();
        GlideUtils.getInstances().loadNormalImg(this, mActionIcon, Constants.WEB_IMG_URL_UPLOADS + bean.getImg());
        mActionTitle.setText(bean.getTitle());
        mActionContent.setText(bean.getContent());
        mActionAddress.setText(bean.getExtra().getData().getAddress());
        mActionPhone.setText(bean.getExtra().getData().getPhone());
        mActionTime.setText(bean.getExtra().getData().getDate());
        mActionSponsor.setText(bean.getExtra().getData().getOrganizer());
    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.iv_title_back
            ,R.id.tv_online_sign_up
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_online_sign_up:
                Bundle bundle = new Bundle();
                bundle.putInt(ActionDetailActivity.ACTION_ID, mActionId);
                gotoActivity(ActionOnlineSingupActivity.class, false, bundle);
                break;
        }
    }
}
