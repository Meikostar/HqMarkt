package com.hqmy.market.view.mainfragment.consume;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.FamilyLikeAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by rzb on 2019/5/16.
 * 爱心家庭-二手家电
 */
public class LoveFamilySecondHandActivity extends BaseActivity {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    FamilyLikeAdapter mFamilyLikeAapter;
    String productId;

    @Override
    public void initListener() {
        bindClickEvent(ivTitleBack, () -> {
            finish();
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                getData();
            }
        });

        mRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_love_family_second_hand;
    }

    @Override
    public void initView() {
        tvTitleText.setText("二手家用电器");
        tvTitleText.setTextColor(getResources().getColor(R.color.white));
        ivTitleBack.setImageResource(R.mipmap.ic_back_white);
        initAdapter();
        productId = getIntent().getStringExtra("productId");
    }

    @Override
    public void initData() {
        mRefreshLayout.autoRefresh();
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mFamilyLikeAapter = new FamilyLikeAdapter(null, this);
        mRecyclerView.setAdapter(mFamilyLikeAapter);
    }

    private void getData() {
        Map map = new HashMap();
        map.put("include", "user");
        map.put("filter[category_id]", productId);
        DataManager.getInstance().getFamilyNewList(
                new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
                    @Override
                    public void onSuccess(HttpResult<List<NewListItemDto>> httpResult) {
                        mFamilyLikeAapter.setNewData(httpResult.getData());
                        mRefreshLayout.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                         mRefreshLayout.finishRefresh();
                    }
                }
                , "ax", map);
    }
}
