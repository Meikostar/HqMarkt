package com.hqmy.market.common.utils;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.view.widgets.autoview.SuperSwipeRefreshLayout;

/**
 * Created by rzb on 2019/4/22
 */
public class SwipeRefreshLayoutUtil {
    //Header View
    //private ProgressBar progressBar;
    private ImageView      headerImg;
    private RelativeLayout head_container;
    private TextView       textView;
    AnimationDrawable animationDrawable;
    AnimationDrawable footerAnimationDrawable;
    //Footer View
    private ImageView footerProgressBar;
    private TextView footerTextView;
    private boolean canLoadMore = true;
    /**
     * 下拉刷新和加载更多
     * @param swipeRefreshLayout
     * @param listener
     */
    public void setSwipeRefreshView(final SuperSwipeRefreshLayout swipeRefreshLayout, final OnRefreshAndLoadMoreListener listener,int color) {
        swipeRefreshLayout.setHeaderView(createHeaderView(swipeRefreshLayout,color));// 自定义头部
        swipeRefreshLayout.setFooterView(createFooterView(swipeRefreshLayout));//自定义尾部
        swipeRefreshLayout.setTargetScrollWithLayout(true);
        swipeRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("onPullDistance", "onPullDistance=正在刷新===");
                if (animationDrawable != null) {
                    animationDrawable.start();
                }
                textView.setText("正在刷新");
                //                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onRefresh();
                        }
                        //                        swipeRefreshLayout.setRefreshing(false);
                        //                        progressBar.setVisibility(View.GONE);
                    }
                }, 200);
            }

            @Override
            public void onPullDistance(int distance) {
                // pull distance
                Log.d("onPullDistance", "onPullDistance====" + distance);
                if (distance == 0 && animationDrawable != null) {
                    animationDrawable.stop();
                }
            }
            @Override
            public void onPullEnable(boolean enable) {
                Log.d("onPullDistance", "onPullDistance=onPullEnable===" + enable);
                textView.setText(enable ? "松开刷新" : "下拉刷新");
            }
        });
        swipeRefreshLayout.setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (canLoadMore) {
                    footerTextView.setText("正在加载...");
                    footerProgressBar.setVisibility(View.VISIBLE);
                    if (footerAnimationDrawable != null) {
                        footerAnimationDrawable.start();
                    }
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            //                            footerProgressBar.setVisibility(View.GONE);
                            if (listener != null) {
                                listener.onLoadMore();
                            }
                            //                            swipeRefreshLayout.setLoadMore(false);
                        }
                    }, 200);
                } else {
                    swipeRefreshLayout.setLoadMore(false);
                }
            }

            @Override
            public void onPushEnable(boolean enable) {
                if (canLoadMore) {
                    footerTextView.setText(enable ? "松开加载" : "上拉加载");
                } else {
                    footerTextView.setText("数据已经加载完毕");
                }

            }

            @Override
            public void onPushDistance(int distance) {
                if (distance == 0 && footerProgressBar != null) {
                    footerProgressBar.setVisibility(View.GONE);
                    if (footerAnimationDrawable != null) {
                        footerAnimationDrawable.stop();
                    }
                }
            }
        });
    }
    /**
     * 下拉刷新和加载更多
     * @param swipeRefreshLayout
     * @param listener
     */
    public void setSwipeRefreshView(final SuperSwipeRefreshLayout swipeRefreshLayout, final OnRefreshAndLoadMoreListener listener) {
        swipeRefreshLayout.setHeaderView(createHeaderView(swipeRefreshLayout,0));// 自定义头部
        swipeRefreshLayout.setFooterView(createFooterView(swipeRefreshLayout));//自定义尾部
        swipeRefreshLayout.setTargetScrollWithLayout(true);
        swipeRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("onPullDistance", "onPullDistance=正在刷新===");
                if (animationDrawable != null) {
                    animationDrawable.start();
                }
                textView.setText("正在刷新");
//                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onRefresh();
                        }
//                        swipeRefreshLayout.setRefreshing(false);
//                        progressBar.setVisibility(View.GONE);
                    }
                }, 200);
            }

            @Override
            public void onPullDistance(int distance) {
                // pull distance
                Log.d("onPullDistance", "onPullDistance====" + distance);
                if (distance == 0 && animationDrawable != null) {
                    animationDrawable.stop();
                }
            }
            @Override
            public void onPullEnable(boolean enable) {
                Log.d("onPullDistance", "onPullDistance=onPullEnable===" + enable);
                textView.setText(enable ? "松开刷新" : "下拉刷新");
            }
        });
        swipeRefreshLayout.setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (canLoadMore) {
                    footerTextView.setText("正在加载...");
                    footerProgressBar.setVisibility(View.VISIBLE);
                    if (footerAnimationDrawable != null) {
                        footerAnimationDrawable.start();
                    }
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
//                            footerProgressBar.setVisibility(View.GONE);
                            if (listener != null) {
                                listener.onLoadMore();
                            }
//                            swipeRefreshLayout.setLoadMore(false);
                        }
                    }, 200);
                } else {
                    swipeRefreshLayout.setLoadMore(false);
                }
            }

            @Override
            public void onPushEnable(boolean enable) {
                if (canLoadMore) {
                    footerTextView.setText(enable ? "松开加载" : "上拉加载");
                } else {
                    footerTextView.setText("数据已经加载完毕");
                }

            }

            @Override
            public void onPushDistance(int distance) {
                if (distance == 0 && footerProgressBar != null) {
                    footerProgressBar.setVisibility(View.GONE);
                    if (footerAnimationDrawable != null) {
                        footerAnimationDrawable.stop();
                    }
                }
            }
        });
    }

    public View createFooterView(SuperSwipeRefreshLayout swipeRefreshLayout) {
        View footerView = LayoutInflater.from(swipeRefreshLayout.getContext()).inflate(R.layout.layout_footer, null);
        footerProgressBar = (ImageView) footerView.findViewById(R.id.footer_pb_view);
        footerTextView = (TextView) footerView.findViewById(R.id.footer_text_view);
        footerAnimationDrawable = (AnimationDrawable) footerProgressBar.getBackground();
        footerProgressBar.setVisibility(View.GONE);
        footerTextView.setText("上拉加载更多...");
        return footerView;
    }

    public View createHeaderView(SuperSwipeRefreshLayout swipeRefreshLayout,int color) {
        View headerView = LayoutInflater.from(swipeRefreshLayout.getContext()).inflate(R.layout.layout_head, null);
//        progressBar = (ProgressBar) headerView.findViewById(R.id.pb_view);
        headerImg = (ImageView) headerView.findViewById(R.id.image_view);
        animationDrawable = (AnimationDrawable) headerImg.getBackground();
        textView = (TextView) headerView.findViewById(R.id.text_view);
        head_container = (RelativeLayout) headerView.findViewById(R.id.head_container);
        if(color!=0){
            head_container.setBackgroundResource(color);
        }

        textView.setText("下拉刷新");
//        progressBar.setVisibility(View.GONE);
        return headerView;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }
    public void setBgColor(int color) {
//
    }
    public interface OnRefreshAndLoadMoreListener {
        void onRefresh();
        void onLoadMore();
    }

    /**
     * 是否无更多数据
     * @param currentPage 当前页
     * @param rows 加载行数
     * @param total 总数
     */
    public void isMoreDate(int currentPage,int rows,long total) {
        if(currentPage*rows >= total){
            setMoreEnable();
        }
    }

    public void setMoreEnable(){
        canLoadMore = false;
        if(footerTextView != null){
            footerTextView.setText("数据已经加载完毕");
        }
    }

}
