package com.hqmy.market.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.bean.VideoLiveBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.LuboItemAdapter;
import com.hqmy.market.view.widgets.autoview.CustomView;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单
 */
public class HostManActivity extends BaseActivity {


    @BindView(R.id.header)
    MaterialHeader     header;
    @BindView(R.id.iv_setting)
    ImageView          ivSetting;
    @BindView(R.id.iv_img)
    ImageView          ivImg;
    @BindView(R.id.tv_name)
    TextView           tvName;
    @BindView(R.id.tv_cout)
    TextView           tvCout;
    @BindView(R.id.iv_care)
    ImageView          ivCare;
    @BindView(R.id.tv_care)
    TextView           tvCare;
    @BindView(R.id.ll_chat)
    LinearLayout       llChat;
    @BindView(R.id.ll_care)
    LinearLayout       llCare;
    @BindView(R.id.rl_mine_user_info)
    RelativeLayout     rlMineUserInfo;
    @BindView(R.id.tv_live_count)
    TextView           tvLiveCount;
    @BindView(R.id.ll_liveing)
    LinearLayout       llLiveing;
    @BindView(R.id.consume_push_recy)
    RecyclerView       recyclerLive;
    @BindView(R.id.consume_srl)
    SmartRefreshLayout mRefreshLayout;
    private LuboItemAdapter mAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_host_man;
    }

    @Override
    public void initView() {
        liver_user_id=getIntent().getStringExtra("userId");
        videoId=getIntent().getStringExtra("videoId");
        mRefreshLayout.autoRefresh();
        recyclerLive.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter=new LuboItemAdapter(this);
        recyclerLive.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        getHostInfo();
    }

    @Override
    public void initListener() {

        llCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAttention) {
                    deleteAttention();
                } else {
                    postAttention();
                }
            }
        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getLuBoLive();
            }
        });


        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getLuBoLive();
            }
        });
    }
    private boolean isAttention;
    private int                    mPage        = 1;
    private void postAttention() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("id", liver_user_id);
        DataManager.getInstance().postAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                dissLoadDialog();
                ToastUtil.toast("关注成功");
                isAttention = true;
                tvCare.setText("取消关注");
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("关注成功");
                    isAttention = true;
                    tvCare.setText("取消关注");
                } else {
                    ToastUtil.toast("关注失败");
                }
            }
        }, map);
    }

    private void deleteAttention() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("id", liver_user_id);
        DataManager.getInstance().deleteAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                dissLoadDialog();
                ToastUtil.toast("取消关注成功");
                isAttention = false;
                tvCare.setText("关注");
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("取消关注成功");
                    isAttention = false;
                    tvCare.setText("关注");
                } else {
                    ToastUtil.toast("取消关注失败");
                }
            }
        }, map);
    }
    public void getHostInfo(){
        showLoadDialog();
        DataManager.getInstance().liveApply(new DefaultSingleObserver<HttpResult<VideoLiveBean>>() {
            @Override
            public void onSuccess(HttpResult<VideoLiveBean> result) {
                dissLoadDialog();
                if (result != null && result.getLive_apply() != null&& result.getLive_apply().getUser() != null) {
                    GlideUtils.getInstances().loadRoundImg(HostManActivity.this, ivImg, Constants.WEB_IMG_URL_UPLOADS + result.getLive_apply().getUser().avatar, R.drawable.moren_ren);

                    if (TextUtils.isEmpty(result.getLive_apply().getUser().name)) {
                        tvName.setText(result.getData().getUser().getData().getName());
                    }
                    if (result.getLive_apply().getUser().is_followed!=null) {
                        tvCare.setText("取消关注");
                        isAttention=true;
                    } else {
                        isAttention=false;
                        tvCare.setText("关注");
                    }
                    if (result.getLive_apply().getUser().followers_count!=null) {
                        tvCout.setText("关注:"+result.getLive_apply().getUser().followers_count.count);
                    } else {
                        tvCout.setText("关注:0");
                    }
                }else {
                    tvCare.setText("关注");
                }


            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, liver_user_id);
    }

    private String liver_user_id;
    private String videoId;
    private void getLuBoLive() {

                showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("page", mPage + "");
        map.put("live_type", "2");
        map.put("liver_user_id", liver_user_id);
        DataManager.getInstance().liveVideos(new DefaultSingleObserver<HttpResult<List<VideoLiveBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<VideoLiveBean>> result) {
                                dissLoadDialog();
                dissLoadDialog();
                setData(result);


            }

            @Override
            public void onError(Throwable throwable) {
                                dissLoadDialog();


            }
        }, map);
    }

    private void setData(HttpResult<List<VideoLiveBean>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }


        if (mPage <= 1) {
            mAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mAdapter.setEmptyView(new EmptyView(this));
            }
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setEnableRefresh(true);
            mAdapter.addData(httpResult.getData());
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }

    }


}
