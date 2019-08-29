package com.hqmy.market.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CommentListBean;
import com.hqmy.market.bean.DynamicBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.adapter.MyImageAdapter;
import com.hqmy.market.view.adapter.MyIssueDetailAdapter;
import com.hqmy.market.view.widgets.CircleImageView;
import com.hqmy.market.view.widgets.CustomDividerItemDecoration;
import com.hqmy.market.view.widgets.NoScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的发布的 正文
 */
public class MyIssueDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView           mTitleText;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView       mRecyclerView;

    CircleImageView  mAvatar;
    TextView         mName;
    TextView         mTime;
    TextView         mReadNum;
    TextView         mContent;
    TextView         mShareNum;
    TextView         mCommentNum;
    TextView         mDianzanNum;
    NoScrollGridView mGridView;


    private MyIssueDetailAdapter mAdapter;
    private int mId;
    private int mPage;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_issue_detail;
    }

    @Override
    public void initView() {
        mTitleText.setText("正文");
    }

    @Override
    public void initData() {

        Bundle bundle = getIntent().getExtras();
        mId = bundle.getInt("id");
        initRecyclerView();

        getDynamicDetailData();

        getDynamicCommentList();
    }

    /**
     * 获取动态评论列表
     */
    private void getDynamicCommentList() {
        Map<String, String> map = new HashMap<>();
        map.put("object", "Modules\\Project\\Entities\\Post");
        map.put("id", mId + "");
        map.put("include", "user");
        map.put("mPage", mPage + "");

        DataManager.getInstance().getDynamicCommentList(new DefaultSingleObserver<HttpResult<List<CommentListBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<CommentListBean>> httpResult) {
                dissLoadDialog();

                setCommentListData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }

    /**
     * 设置评论列表数据
     * @param httpResult
     */
    private void setCommentListData(HttpResult<List<CommentListBean>> httpResult) {
        if(httpResult == null || httpResult.getData() == null){
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

        if(httpResult.getMeta()!=null && httpResult.getMeta().getPagination()!=null){
            if(httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()){
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }

    /**
     * 动态详情
     */
    private void getDynamicDetailData() {
        DataManager.getInstance().getDynamicDetail(new DefaultSingleObserver<HttpResult<DynamicBean>>() {
            @Override
            public void onSuccess(HttpResult<DynamicBean> httpResult) {
                dissLoadDialog();
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, mId);
    }

    private void setData(HttpResult<DynamicBean> httpResult){
        DynamicBean bean = httpResult.getData();
        if(bean == null){
            return;
        }

        mName.setText(ShareUtil.getInstance().getString(Constants.USER_NAME,""));
        GlideUtils.getInstances().loadRoundImg(this, mAvatar, Constants.WEB_IMG_URL_UPLOADS+ShareUtil.getInstance().getString(Constants.USER_HEAD,""));
        mTime.setText(bean.getCreated_at());
        mContent.setText(bean.getContent());
        mReadNum.setText(bean.getClick()+"人阅读");
        mShareNum.setText(bean.getShares()+"");
        mCommentNum.setText(bean.getComments_count()+"");
        mDianzanNum.setText(bean.getLikers_count()+"");

        ArrayList img = httpResult.getData().getImg();
        if(img != null && img.size()>0){
            mGridView.setVisibility(View.VISIBLE);
            mGridView.setAdapter(new MyImageAdapter(this, img));
        }else {
            mGridView.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getDynamicCommentList();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getDynamicCommentList();
            }
        });
    }
    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        CustomDividerItemDecoration decoration = new CustomDividerItemDecoration(this, LinearLayoutManager.VERTICAL, R.drawable.shape_divider_f5f5f5_1);
        decoration.setOffsetLeft(DensityUtil.dp2px(16));
        decoration.setOffsetRight(DensityUtil.dp2px(25));
        mRecyclerView.addItemDecoration(decoration);
        mAdapter = new MyIssueDetailAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        initHearderView();
    }

    private void initHearderView() {
        View mHeaderView = View.inflate(this, R.layout.header_my_issue_detail, null);
        mAvatar = mHeaderView.findViewById(R.id.iv_item_issue_user_avatar);
        mName = mHeaderView.findViewById(R.id.tv_item_issue_name);
        mTime = mHeaderView.findViewById(R.id.tv_item_issue_time);
        mReadNum = mHeaderView.findViewById(R.id.tv_item_issue_read_num);
        mContent = mHeaderView.findViewById(R.id.tv_item_issue_content);
        mShareNum = mHeaderView.findViewById(R.id.tv_item_issue_share_num);
        mCommentNum = mHeaderView.findViewById(R.id.tv_item_issue_comment_num);
        mDianzanNum = mHeaderView.findViewById(R.id.tv_item_issue_dianzan_num);
        mGridView = mHeaderView.findViewById(R.id.gv_imgs);

        mAdapter.setHeaderAndEmpty(true);
        mAdapter.addHeaderView(mHeaderView);
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
