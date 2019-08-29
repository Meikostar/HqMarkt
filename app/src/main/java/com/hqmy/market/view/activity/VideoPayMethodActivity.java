package com.hqmy.market.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.OrderCheckoutBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.MyCommonWebView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoPayMethodActivity extends BaseActivity {
    @BindView(R.id.iv_cover)
    ImageView iv_cover;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_author)
    TextView tv_author;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    private String product_id;

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.video_pay_method_layout;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mTitleText.setText("支付方式");
        GlideUtils.getInstances().loadNormalImg(this, iv_cover, getIntent().getStringExtra("cover"));
        product_id = getIntent().getStringExtra("product_id");
        tv_title.setText(getIntent().getStringExtra("title"));
        tv_author.setText(getIntent().getStringExtra("author"));
        tv_money.setText("￥" + getIntent().getStringExtra("money"));
    }

    private void orderCheckout() {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("product_id", product_id);
        DataManager.getInstance().orderCheckout(new DefaultSingleObserver<HttpResult<List<OrderCheckoutBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<OrderCheckoutBean>> result) {
                dissLoadDialog();
                if (result != null && result.getData() != null && result.getData().size() > 0 && !TextUtils.isEmpty(result.getData().get(0).getNo())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.INTENT_WEB_URL, Constants.BASE_URL + "to_pay?order_id=" + result.getData().get(0).getNo());
                    gotoActivity(MyCommonWebView.class, false, bundle);
                    finish();
                } else {
                    ToastUtil.showToast("支付失败");
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast("支付失败");
            }
        }, map);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_to_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_to_pay:
                orderCheckout();
                break;
        }

    }
}
