package com.hqmy.market.view.mainfragment.community;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.NewsOtherListItemDto;
import com.hqmy.market.bean.NewsRecommendListDto;
import com.hqmy.market.bean.NewsRecommendListItemDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.NewsOneAdapter;
import com.hqmy.market.view.adapter.NewsOtherAdapter;
import com.hqmy.market.view.adapter.NewsThreeAdapter;
import com.hqmy.market.view.adapter.NewsTwoAdapter;
import com.hqmy.market.view.widgets.autoview.NoScrollListView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * Created by rzb on 2018/4/18.
 */
public class NewsContentFragment extends BaseFragment {
    private String tab;
    @BindView(R.id.community_news_recycle_one)
    RecyclerView     community_news_recycle_one;
    @BindView(R.id.community_news_recycle_two)
    NoScrollListView community_news_recycle_two;
    @BindView(R.id.community_news_recycle_three)
    NoScrollListView community_news_recycle_three;
    private NewsOneAdapter   mNewsOneAapter;
    private NewsTwoAdapter  mNewsTwoAdapter;
    private NewsThreeAdapter mNewsThreeAdapter;
    private NewsOtherAdapter mNewsOtherAdapter;
    private List<NewsRecommendListItemDto> oneList = new ArrayList<NewsRecommendListItemDto>();
    private List<NewsRecommendListItemDto> twoList = new ArrayList<NewsRecommendListItemDto>();
    private List<NewsRecommendListItemDto> threeList = new ArrayList<NewsRecommendListItemDto>();
    private List<NewsOtherListItemDto> otherList = new ArrayList<NewsOtherListItemDto>();

    public static Fragment newInstance(String tab) {
        NewsContentFragment fragment = new NewsContentFragment();
        fragment.tab = tab;
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_content;
    }

    @Override
    protected void initView() {
        initAdapter();
    }

    @Override
    protected void initData() {
        if(tab != null) {
            if (tab.equals("每日推荐")) {
                getNewsRecommendList();
            } else {
                getNewsList(tab);
            }
        }
    }

    @Override
    protected void initListener() {
        community_news_recycle_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HuoDonDetailActivity.HUODON, mNewsTwoAdapter.getItem(position));
                gotoActivity(HuoDonDetailActivity.class, false, bundle);
            }
        });

        community_news_recycle_three.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HuoDonDetailActivity.HUODON, mNewsTwoAdapter.getItem(position));
                gotoActivity(HuoDonDetailActivity.class, false, bundle);
            }
        });
    }

    private void initAdapter() {
        if(tab != null) {
            if (tab.equals("每日推荐")) {
                community_news_recycle_two.setVisibility(View.VISIBLE);
                community_news_recycle_three.setVisibility(View.VISIBLE);

                community_news_recycle_one.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
                mNewsOneAapter = new NewsOneAdapter(oneList, this.getContext());
                community_news_recycle_one.setAdapter(mNewsOneAapter);

                mNewsTwoAdapter = new NewsTwoAdapter(this.getContext(), twoList);
                community_news_recycle_two.setAdapter(mNewsTwoAdapter);

                mNewsThreeAdapter = new NewsThreeAdapter(this.getContext(), threeList);
                community_news_recycle_three.setAdapter(mNewsThreeAdapter);
            } else {
                community_news_recycle_two.setVisibility(View.GONE);
                community_news_recycle_three.setVisibility(View.GONE);

                community_news_recycle_one.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
                mNewsOtherAdapter = new NewsOtherAdapter(otherList, this.getContext());
                community_news_recycle_one.setAdapter(mNewsOtherAdapter);
            }
        }
    }

    //获取新闻列表(每日推荐)
    private void getNewsRecommendList() {
        DataManager.getInstance().getNewsRecommendList(new DefaultSingleObserver<NewsRecommendListDto>() {
            @Override
            public void onSuccess(NewsRecommendListDto newsRecommendListDto) {
                dissLoadDialog();
                if(newsRecommendListDto != null) {
                        NewsRecommendListDto dynamicListDto = newsRecommendListDto;
                        oneList.addAll(dynamicListDto.getFlag1());
                        mNewsOneAapter.notifyDataSetChanged();
                        twoList.addAll(dynamicListDto.getFlag2());
                        mNewsTwoAdapter.notifyDataSetChanged();
                        threeList.addAll(dynamicListDto.getFlag3());
                        mNewsThreeAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        });
    }

    //获取新闻列表（其他）
    private void getNewsList(String withAllTags) {
        DataManager.getInstance().getNewsList(new DefaultSingleObserver<HttpResult<List<NewsOtherListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewsOtherListItemDto>> result) {
                dissLoadDialog();
                if(result != null) {
                    if(result.getData() != null) {
                        List<NewsOtherListItemDto> nolList = result.getData();
                        otherList.addAll(nolList);
                        mNewsOtherAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, withAllTags);
    }
}
