package com.hqmy.market.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BankCardDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.BankCardManagerAdapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 银行卡管理
 */
public class BankCardManagerActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.tv_title_right)
    TextView mTitleRight;

    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private BankCardManagerAdapter mAdapter;
    private int currentPage = 1;
    boolean isSelCard;
    @Override
    public int getLayoutId() {
        return R.layout.activity_bank_card_manager;
    }

    @Override
    public void initView() {
        mTitleText.setText("银行卡管理");
        mTitleRight.setVisibility(View.VISIBLE);
        mTitleRight.setText("添加");
        initRecyclerView();
        isSelCard = getIntent().getBooleanExtra("selCard",false);
    }

    @Override
    public void initData() {
//        mRefreshLayout.autoRefresh();

    }

    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = 1;
                getBankCardList();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++currentPage;
                getBankCardList();
            }
        });
    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new BankCardManagerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.card_content:
                        if(isSelCard){
                            Intent intent = new Intent();
                            intent.putExtra("cardItem",(BankCardDto)mAdapter.getItem(position));
                            setResult(Activity.RESULT_OK,intent);
                            finish();
                        }
                        break;
                    case R.id.card_content_del:
                        delBankCard((BankCardDto) adapter.getItem(position));
                        break;
                }
            }
        });


    }

    private void delBankCard(BankCardDto bankCardDto) {
        showLoadDialog();
        DataManager.getInstance().deleteBankCard(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                super.onSuccess(result);
                dissLoadDialog();
                if (mRefreshLayout != null) {
                    mRefreshLayout.autoRefresh();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    if (mRefreshLayout != null) {
                        mRefreshLayout.autoRefresh();
                    }

                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, bankCardDto.getId() + "");
    }

    private void getBankCardList() {

        DataManager.getInstance().getBankCardList(new DefaultSingleObserver<HttpResult<List<BankCardDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<BankCardDto>> httpResult) {
                if (currentPage <= Constants.PAGE_NUM) {
                    mAdapter.setNewData(httpResult.getData());
                    if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                        mAdapter.setEmptyView(new EmptyView(BankCardManagerActivity.this));
                    }
                    mRefreshLayout.finishRefresh();
                    mRefreshLayout.setEnableLoadMore(true);
                } else {
                    mAdapter.addData(httpResult.getData());
                    mRefreshLayout.finishLoadMore();
                    mRefreshLayout.setEnableRefresh(true);
                }
                if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
                    if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                        mRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.setEnableRefresh(true);

            }
        }, currentPage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshLayout.autoRefresh();
    }

    @OnClick({R.id.iv_title_back
            , R.id.tv_title_right
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                gotoActivity(AddBandCardActivity.class);
                break;
        }
    }

}
