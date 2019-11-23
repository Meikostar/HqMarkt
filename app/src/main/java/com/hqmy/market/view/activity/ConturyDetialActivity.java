package com.hqmy.market.view.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AreaDto;
import com.hqmy.market.bean.BaseDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.adapter.AreaImageAdapter;
import com.hqmy.market.view.adapter.Areadapter;
import com.hqmy.market.view.adapter.ConsumePushAdapter;
import com.hqmy.market.view.adapter.ConturyAdapter;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.ActionbarView;
import com.hqmy.market.view.widgets.autoview.CustomView;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.autoview.NoScrollGridView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class ConturyDetialActivity extends BaseActivity {


    @BindView(R.id.custom_action_bar)
    ActionbarView      customActionBar;
    @BindView(R.id.consume_push_recy)
    RecyclerView       consumePushRecy;
    @BindView(R.id.consume_srl)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.consume_scrollView)
    CustomView         consumeScrollView;

    @Override
    public void initListener() {

    }

    private int state;

    @Override
    public int getLayoutId() {
        return R.layout.ui_contury_detail_layout;
    }

    //    private SpikeChooseTimeAdapter testAdapter;
    private ConturyAdapter       testAdapter;
    private boolean              isShow;
    private BaseDto              spikeDto;
    private AreaImageAdapter     mHomedapter;
    private ConsumePushAdapter   mProcutAdapter;
    private List<NewListItemDto> goodsLists = new ArrayList<NewListItemDto>();

    @Override
    public void initView() {
        id=getIntent().getStringExtra("id");
        title=getIntent().getStringExtra("title");
        actionbar.setImgStatusBar(R.color.my_color_white);
        if(TextUtil.isNotEmpty(title)){
            actionbar.setTitle(title);
        }

        actionbar.setRightImageAction(R.mipmap.black_message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(MessageCenterActivity.class);
            }
        });
        getCategorisContury();
        getConturyProducts();
        mHomedapter = new AreaImageAdapter(this);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };


        mProcutAdapter = new ConsumePushAdapter(goodsLists, this);
        consumePushRecy.addItemDecoration(new RecyclerItemDecoration(5, 2));
        consumePushRecy.setLayoutManager(gridLayoutManager2);
        consumePushRecy.setAdapter(mProcutAdapter);
        initHeadView();
    }

    private NoScrollGridView   gridContent;
    private View view;
    public void initHeadView(){
        view = LayoutInflater.from(this).inflate(R.layout.contury_head_view, null);
        gridContent= view.findViewById(R.id.grid_content);
        gridContent.setAdapter(mHomedapter);
        mProcutAdapter.addHeaderView(view);
    }

    private void setData(HttpResult<List<NewListItemDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        mProcutAdapter.setNewData(httpResult.getData());
        if (httpResult.getData() == null || httpResult.getData().size() == 0) {
            mProcutAdapter.setEmptyView(new EmptyView(this));
        }
        refreshLayout.finishRefresh();


    }

    private void getCategorisContury() {
        //showLoadDialog();
        DataManager.getInstance().getBrandsByCate(new DefaultSingleObserver<HttpResult<List<AreaDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<AreaDto>> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {

                        mHomedapter.setData(result.getData());



                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        },id);
    }
    private String id;
    private String title;

    private void getConturyProducts() {
        //showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("sort", "-sales_count");
        map.put("include", "category");
        map.put("filter[scopeBrandCategory]", id + "");
        DataManager.getInstance().findGoodsList(map,new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();
                setData(result);
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        });
    }

    @Override
    public void initData() {

    }


}
