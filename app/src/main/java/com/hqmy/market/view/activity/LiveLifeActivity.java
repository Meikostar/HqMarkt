package com.hqmy.market.view.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.view.adapter.LiveLifeAdapter;
import com.hqmy.market.view.widgets.RecycleViewDivider_PovertyRelief;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 生活直播
 */
public class LiveLifeActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;

    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView       mRecyclerView;
    private LiveLifeAdapter mAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_live_life;
    }

    @Override
    public void initView() {
        mTitleText.setText("生活直播频道");
    }

    @Override
    public void initData() {
        initRecyclerView();
    }
    @Override
    public void initListener() {

    }
    private void initRecyclerView() {

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new RecycleViewDivider_PovertyRelief(DensityUtil.dp2px(10), DensityUtil.dp2px(15)));

        mAdapter = new LiveLifeAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add(i + "");
        }
        mAdapter.setNewData(data);
    }

    @OnClick({R.id.iv_title_back
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }

    }
}
