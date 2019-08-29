package com.hqmy.market.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.LearnRecordInfo;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.LearnRecordAdapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LearnRecordActivity extends BaseActivity {
    @BindView(R.id.recy_learn_record)
    RecyclerView recyLearnRecord;
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    private LearnRecordAdapter mAdapter;

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.learn_record_layout;
    }

    @Override
    public void initView() {
        mTitleText.setText("学习记录");
        initRecyclerView();
    }

    @Override
    public void initData() {
        showLoadDialog();
        Map<String,String> map = new HashMap<>();
        map.put("filter[have_footprint_type]","SMG\\Mall\\Models\\MallProduct");
        map.put("include","object");
        map.put("object_filter[type]","course");
        DataManager.getInstance().learnRecords(new DefaultSingleObserver<HttpResult<List<LearnRecordInfo>>>() {
            @Override
            public void onSuccess(HttpResult<List<LearnRecordInfo>> result) {
                dissLoadDialog();
                if (result != null && result.getData() != null && result.getData().size()>0) {
                    mAdapter.setNewData(result.getData());
                }else {
                    mAdapter.setEmptyView(new EmptyView(LearnRecordActivity.this));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        },map);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        recyLearnRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new LearnRecordAdapter();
        recyLearnRecord.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter.getItem(position).getObject() != null && mAdapter.getItem(position).getObject().getData() != null){
                    Intent intent = new Intent(LearnRecordActivity.this, CourseWarehouseDetailActivity.class);
                    intent.putExtra("id", mAdapter.getItem(position).getObject().getData().getId());
                    intent.putExtra("name", mAdapter.getItem(position).getObject().getData().getTitle());
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick({R.id.layout_top})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_top:
                finish();
                break;
        }

    }
}
