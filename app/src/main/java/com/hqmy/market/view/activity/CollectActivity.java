package com.hqmy.market.view.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.TopicListItemDto;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.CollectGoodsAdapter;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收藏
 */
public class CollectActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView          ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView           tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView           tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout     layoutTop;
    @BindView(R.id.header)
    MaterialHeader     header;
    @BindView(R.id.recyclerView)
    RecyclerView       mRecyclerView;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshlayout;
    @BindView(R.id.tv_cout)
    TextView           tvCout;
    @BindView(R.id.iv_delete)
    ImageView          ivDelete;
    @BindView(R.id.ll_bg)
    LinearLayout       llBg;
    private CollectGoodsAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect;
    }

    private ArrayList<TopicListItemDto> mCollectionDatas = new ArrayList<>();
    private int                         type;

    @Override
    public void initView() {
        tvTitleText.setText("我收藏的商品");
        tvTitleRight.setText("编辑");
        tvTitleRight.setVisibility(View.VISIBLE);
        getCollectData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new CollectGoodsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(CommodityDetailActivity.PRODUCT_ID, mAdapter.getItem(position).getId());
                bundle.putString(CommodityDetailActivity.MALL_TYPE, mAdapter.getItem(position).getType());
                bundle.putString(CommodityDetailActivity.FROM, "gc");
                gotoActivity(CommodityDetailActivity.class, false, bundle);
            }
        });
        mAdapter.setChooseListener(new CollectGoodsAdapter.ChooseListener() {
            @Override
            public void choose() {
                int i=0;
                List<TopicListItemDto> data = mAdapter.getData();
                for(TopicListItemDto dto:data){
                    if(dto.isChoose){
                        ++i;
                    }
                }
                tvCout.setText(i+"");
            }
        });
    }

    private void setData(HttpResult<List<TopicListItemDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }
        if (mPage <= 1) {
            mCollectionDatas.clear();
            mCollectionDatas.addAll(httpResult.getData());
            mAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mAdapter.setEmptyView(new EmptyView(this));
            }
            refreshlayout.finishRefresh();
//            refreshlayout.setEnableLoadMore(true);
        } else {
            refreshlayout.finishLoadMore();
//            refreshlayout.setEnableRefresh(true);
            mAdapter.addData(httpResult.getData());
            mCollectionDatas.addAll(httpResult.getData());
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                refreshlayout.finishLoadMoreWithNoMoreData();
            }
        }
    }

    private int mPage = 1;

    /**
     * 获取收藏数据
     */
    private void getCollectData() {
        Map<String, String> map = new HashMap<>();
        map.put("object", "SMG\\Mall\\Models\\MallProduct");
        map.put("include", "shop,user,brand.category");

        DataManager.getInstance().getCollectionList(new DefaultSingleObserver<HttpResult<List<TopicListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<TopicListItemDto>> httpResult) {
                dissLoadDialog();
                setData(httpResult);
                int i=0;
                List<TopicListItemDto> data = mAdapter.getData();
                if(data!=null&&data.size()>0){
                    for(TopicListItemDto dto:data){
                        if(dto.isChoose){
                            ++i;
                        }
                    }
                    tvCout.setText(i+"");
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }

    /**
     * 移除收藏夹
     *
     * @param
     */
    private void removeCollect(String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("object", "SMG\\Mall\\Models\\MallProduct");
        map.put("id", id);

        DataManager.getInstance().delCollection(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                ToastUtil.showToast("删除成功");

                getCollectData();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("成功");
                    getCollectData();
                } else {
                    dissLoadDialog();
                    ApiException.getHttpExceptionMessage(throwable);
                }
            }
        }, map);
    }


    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        tvTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0) {
                    type = 1;
                    mAdapter.setType(1);
                    tvTitleRight.setText("完成");
                    llBg.setVisibility(View.VISIBLE);
                } else {
                    type = 0;
                    mAdapter.setType(0);
                    tvTitleRight.setText("编辑");
                    llBg.setVisibility(View.GONE);
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = "";
                int i = 0;
                List<TopicListItemDto> data = mAdapter.getData();
                for (TopicListItemDto dto : data) {
                    if (dto.isChoose) {

                        if (i == 0) {
                            id = dto.getId();
                        } else {
                            id = id + "," + dto.getId();
                        }
                        i++;
                    }
                }
                if (TextUtils.isEmpty(id)) {
                    ToastUtil.showToast("您还未选择要删除的数据");
                    return;
                }
                removeCollect(id);

            }
        });
    }

    private String id;

    @OnClick({R.id.iv_title_back
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_right:

                break;
        }
    }


}

