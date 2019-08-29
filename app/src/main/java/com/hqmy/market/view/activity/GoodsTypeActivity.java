package com.hqmy.market.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 编辑宝贝
 */
public class GoodsTypeActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.goods_type_layout;
    }

    @Override
    public void initView() {
        tvTitleText.setText("选择类型");
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.iv_title_back, R.id.tv_tupianyinxiang, R.id.tv_jiayongdianqi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_tupianyinxiang:
                Intent intent = new Intent();
                intent.putExtra("typeName", "图片音像");
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.tv_jiayongdianqi:
                Intent intent1 = new Intent();
                intent1.putExtra("typeName", "家用电器");
                setResult(Activity.RESULT_OK, intent1);
                finish();
                break;
        }

    }
}
