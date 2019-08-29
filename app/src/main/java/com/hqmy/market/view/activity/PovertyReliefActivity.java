package com.hqmy.market.view.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.VideoLiveBean;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.mainfragment.learn.OnlineLiveItemAdapter;
import com.hqmy.market.view.widgets.RecycleViewDivider_PovertyRelief;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扶贫频道(双创课堂)页面
 */
public class PovertyReliefActivity extends BaseActivity {

    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private String id;
    private OnlineLiveItemAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_poverty_relief;
    }

    @Override
    public void initView() {
        id = getIntent().getStringExtra("id");
        mTitleText.setText(getIntent().getStringExtra("name"));
    }

    @Override
    public void initData() {
        initRecyclerView();
        getData();
    }

    @Override
    public void initListener() {

    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new RecycleViewDivider_PovertyRelief(DensityUtil.dp2px(10), DensityUtil.dp2px(15)));
        mAdapter = new OnlineLiveItemAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(PovertyReliefActivity.this, LiveVideoViewActivity.class);
                intent.putExtra("videoPath", mAdapter.getItem(position).getRtmp_play_url());
                intent.putExtra("videoId", mAdapter.getItem(position).getId());
                if (mAdapter.getItem(position).getRoom() != null && mAdapter.getItem(position).getRoom().getData() != null) {
                    intent.putExtra("roomId", mAdapter.getItem(position).getRoom().getData().getId());
                }
                intent.putExtra("liveStreaming", 1);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("cate_id", id);
        map.put("include", "room,user,cate");
        DataManager.getInstance().liveVideos(new DefaultSingleObserver<HttpResult<List<VideoLiveBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<VideoLiveBean>> result) {
                dissLoadDialog();
                if (result != null) {
                    mAdapter.setNewData(result.getData());
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, map);
    }

    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }

    }
}
