package com.hqmy.market.view.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.RoomUserBean;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 直播房间用户列表
 */
public class RoomUserListActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    String roomId;
    private int mPage = 1;
    private RoomUserListAdapter mAdapter;

    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                liveChatter();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                liveChatter();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_room_user_list;
    }

    @Override
    public void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mAdapter = new RoomUserListAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        mTitleText.setText("房间用户列表");
        roomId = getIntent().getStringExtra("room_id");
        mRefreshLayout.autoRefresh();
    }

    public void liveChatter() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("room_id", roomId);
        map.put("page", mPage + "");
        DataManager.getInstance().liveChatter(new DefaultSingleObserver<HttpResult<List<RoomUserBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<RoomUserBean>> httpResult) {
                dissLoadDialog();
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, map);
    }

    private void setData(HttpResult<List<RoomUserBean>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        if (mPage <= 1) {
            mAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                //mAdapter.setEmptyView(new EmptyView(CollectionActivity.this));
            }
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setEnableRefresh(true);
            mAdapter.addData(httpResult.getData());
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }

    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }

    class RoomUserListAdapter extends BaseQuickAdapter<RoomUserBean, BaseViewHolder> {

        public RoomUserListAdapter() {
            super(R.layout.activity_room_user_item, null);
        }

        @Override
        protected void convert(BaseViewHolder helper, RoomUserBean item) {
            helper.setText(R.id.tv_id,"ID:"+item.getId())
                    .setText(R.id.tv_time,item.getCreated_at());
        }
    }
}
