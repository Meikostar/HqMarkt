package com.hqmy.market.view.mainfragment.community;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.TopicListItemDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.TopicListAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 热门话题
 * Created by rzb on 2018/4/20.
 */
public class TopicContentFragment extends BaseFragment implements TopicListAdapter.TopListItemClick{
    private String tab;
    @BindView(R.id.topic_refreshLayout)
    RefreshLayout topic_refreshLayout;
    @BindView(R.id.recycle_topic)
    RecyclerView  recycle_topic;
    private int mPage = 1;
    private TopicListAdapter mAdapter;
    private List<TopicListItemDto> topicList = new ArrayList<TopicListItemDto>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topic_content;
    }

    public static Fragment newInstance(String tab) {
        TopicContentFragment fragment = new TopicContentFragment();
        fragment.tab = tab;
        return fragment;
    }

    @Override
    protected void initView() {
        initAdapter();
    }

    @Override
    protected void initData() {
       if(tab != null) {
           if (tab.equals("每日推荐")) {
               getTopicRecommendList();
           } else {
               getTopicList(tab);
           }
       }
    }

    @Override
    protected void initListener() {
        topic_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                if(tab.equals("每日推荐")){
                    getTopicRecommendList();
                }else{
                    getTopicList(tab);
                }
            }
        });

        topic_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                if(tab.equals("每日推荐")){
                    getTopicRecommendList();
                }else{
                    getTopicList(tab);
                }
            }
        });
    }

    private void initAdapter() {
        recycle_topic.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new TopicListAdapter(this.getContext(), this, topicList);
        recycle_topic.setAdapter(mAdapter);
    }

    //获取热门话题列表(推荐)
    private void getTopicRecommendList() {
        Map<String, String> map = new HashMap<>();
        map.put("page", mPage + "");
        DataManager.getInstance().getTopicRecommendList(new DefaultSingleObserver<HttpResult<List<TopicListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<TopicListItemDto>> result) {
                dissLoadDialog();
                if(result != null) {
                    if(result != null) {
                        setData(result);
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }


    //获取热门话题列表(其他)
    private void getTopicList(String scopeWithAllTags) {
        Map<String, String> map = new HashMap<>();
        map.put("page", mPage + "");
        DataManager.getInstance().getTopicList(new DefaultSingleObserver<HttpResult<List<TopicListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<TopicListItemDto>> result) {
                dissLoadDialog();
                if(result != null) {
                    setData(result);
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, scopeWithAllTags, map);
    }


    private void setData(HttpResult<List<TopicListItemDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        if (mPage <= 1) {
            topicList.clear();
            topicList.addAll(httpResult.getData());
            mAdapter.notifyDataSetChanged();
            if(httpResult.getData() == null || httpResult.getData().size() == 0); {
            }
            topic_refreshLayout.finishRefresh();
            topic_refreshLayout.setEnableLoadMore(true);
        } else {
            topic_refreshLayout.finishLoadMore();
            topic_refreshLayout.setEnableRefresh(true);
            topicList.addAll(httpResult.getData());
            mAdapter.notifyDataSetChanged();
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                topic_refreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }





    @Override
    public void follow(TopicListItemDto post, int position) {

    }

    @Override
    public void share(TopicListItemDto post, int position) {
    }

    @Override
    public void comment(TopicListItemDto post, int position) {
    }

    @Override
    public void thumbUpClick(TopicListItemDto post, int position) {
    }

    @Override
    public void onItemClick(TopicListItemDto post, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TopicDetailActivity.TOPIC_ID, post.getId());
        gotoActivity(TopicDetailActivity.class, false, bundle);
    }
}
