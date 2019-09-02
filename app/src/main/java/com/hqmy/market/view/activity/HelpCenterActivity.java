package com.hqmy.market.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CountOrderBean;
import com.hqmy.market.bean.DetailDto;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.CallPhone;
import com.hqmy.market.view.adapter.HelperAdapter;
import com.hqmy.market.view.widgets.autoview.ClearEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 帮助中心
 */
public class HelpCenterActivity extends BaseActivity {

    @BindView(R.id.tv_title_text)
    TextView       mTitleText;
    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.et_search_str)
    ClearEditText  etSearchStr;
    @BindView(R.id.listview)
    ListView       listview;

    @Override
    public int getLayoutId() {
        return R.layout.activity_help_center;
    }
     private HelperAdapter mAdapter;
    @Override
    public void initView() {
        mTitleText.setText("帮助中心");
        getHelpInfo();

    }
    private void getHelpInfo() {
        DataManager.getInstance().getHelpData(new DefaultSingleObserver<HttpResult<List<DetailDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<DetailDto>> countOrderBean) {
                if (countOrderBean != null && countOrderBean.getData() != null) {
                    mAdapter=new HelperAdapter(HelpCenterActivity.this,countOrderBean.getData());
                    listview.setAdapter(mAdapter);

                }
            }

            @Override
            public void onError(Throwable throwable) {
            }
        },"help");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

//    @OnClick({R.id.iv_title_back
//            , R.id.rl_help_about_us
//            , R.id.rl_help_message_feedback
//            , R.id.rl_help_enterprise_profile
//            , R.id.rl_help_service_tel
//    })
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.iv_title_back:
//                finish();
//                break;
//            case R.id.rl_help_about_us://关于我们
//                gotoActivity(AboutUsActivity.class);
//                break;
//            case R.id.rl_help_message_feedback://留言反馈
//                ToastUtil.showToast("留言反馈");
//                gotoActivity(MessageFeedbackActivity.class);
//                break;
//            case R.id.rl_help_enterprise_profile://企业简介
//                gotoActivity(EnterpriseProfileActivity.class);
//                break;
//            case R.id.rl_help_service_tel://拨打电话
//                callTel();
//                break;
//        }
//    }

    /**
     * 拨打客服电话
     */
    private void callTel() {
        CallPhone.diallPhone(this, "0755-123456000");
    }


}
