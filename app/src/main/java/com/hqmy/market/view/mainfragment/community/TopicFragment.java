package com.hqmy.market.view.mainfragment.community;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.TagBean;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.view.adapter.TabAdapter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 热门话题
 * Created by rzb on 2019/4/20.
 */
public class TopicFragment extends BaseFragment {

    @BindView(R.id.topic_tab)
    TabLayout topic_tab;
    @BindView(R.id.topic_fragmentViewPager)
    ViewPager topic_fragmentViewPager;
    private List<Fragment> fragments;
    private List<String> broadcasts = new ArrayList<>();//显示的导航标题:
    private TabAdapter tabAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topic;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        initTags();
    }

    @Override
    protected void initListener() {

    }

    private void initTags() {
        getTagList();
    }

    //获取新闻标签
    private void getTagList() {
        DataManager.getInstance().getTagList(new DefaultSingleObserver<TagBean>() {
            @Override
            public void onSuccess(TagBean tagBean) {
                //dissLoadDialog();
                if(tagBean != null) {
                    List<String> tagList = tagBean.getPost();
                    if (tagList != null) {
                        if (tagList.size() > 0) {
                            initFragment(tagList);
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }

    private void initFragment(List<String> tList) {
        broadcasts.add("每日推荐");
        broadcasts.addAll(tList);
        if (fragments == null) {
            fragments = new ArrayList<>();
        } else {
            fragments.clear();
        }
        for (int i = 0; i < broadcasts.size(); i++) {
            fragments.add(TopicContentFragment.newInstance(broadcasts.get(i)));
        }
        //设置列表碎片的适配器
        tabAdapter = new TabAdapter(getChildFragmentManager(), fragments, broadcasts);
        topic_fragmentViewPager.setAdapter(tabAdapter);
        topic_fragmentViewPager.setOffscreenPageLimit(fragments.size());
        //将TabLayout和ViewPager关联起来。
        topic_tab.setupWithViewPager(topic_fragmentViewPager);
        //设置可以滑动
        topic_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

}
