package com.hqmy.market.view.mainfragment.learn;

import android.webkit.WebView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.VideoBean;

import butterknife.BindView;

public class CourseWarehouseGraphicFragment extends BaseFragment {
    @BindView(R.id.webView)
    WebView webView;

    VideoBean videoBean;
    public static CourseWarehouseGraphicFragment newInstance(VideoBean videoBean) {
        CourseWarehouseGraphicFragment fragment = new CourseWarehouseGraphicFragment();
        fragment.videoBean = videoBean;
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.course_warehouse_graphic_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        if (videoBean != null){
            webView.loadData(videoBean.getContent(), "text/html; charset=UTF-8", null);
        }
    }

    @Override
    protected void initListener() {

    }
}
