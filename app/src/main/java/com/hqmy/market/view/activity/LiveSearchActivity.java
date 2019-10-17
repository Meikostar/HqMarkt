package com.hqmy.market.view.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.VideoBean;
import com.hqmy.market.bean.VideoLiveBean;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.adapter.RecentSearchAdapter;
import com.hqmy.market.view.mainfragment.learn.CourseWareAdapter;
import com.hqmy.market.view.mainfragment.learn.OnlineLiveItemAdapter;
import com.hqmy.market.view.widgets.RecommendViewGroup;
import com.hqmy.market.view.widgets.RecycleViewDivider_PovertyRelief;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hqmy.market.bean.HotSearchInfo;

import butterknife.BindView;
import butterknife.OnClick;

public class LiveSearchActivity extends BaseActivity {
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.et_search_room)
    EditText etSearchRoom;
    @BindView(R.id.recommendViewGroup)
    RecommendViewGroup recommendViewGroup;
    @BindView(R.id.tv_clear_all)
    TextView tvClearAll;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.recyclerView2)
    RecyclerView recyclerView2;
    @BindView(R.id.part1)
    LinearLayout part1;
    private OnlineLiveItemAdapter povertyReliefAdapter;
    private CourseWareAdapter mAdapter;
    private RecentSearchAdapter recentSearchAdapter;
    List<String> historySearchData = new ArrayList<>();
    List<String> courseWareSearchData = new ArrayList<>();
    private Boolean isCourseWare; //true为课件搜索 false 为首页搜索

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.live_search_layout;
    }

    @Override
    public void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        isCourseWare = getIntent().getBooleanExtra("isCourseWare", false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recentSearchAdapter = new RecentSearchAdapter(isCourseWare);
        recyclerView.setAdapter(recentSearchAdapter);
        recentSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                etSearchRoom.setText(recentSearchAdapter.getItem(position));
                if (isCourseWare) {
                    getCourseProducts();
                } else {
                    liveVideos();
                }
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView2.setLayoutManager(layoutManager);
        recyclerView2.addItemDecoration(new RecycleViewDivider_PovertyRelief(DensityUtil.dp2px(10), DensityUtil.dp2px(15)));
        if (isCourseWare) {
            //点播
            etSearchRoom.setHint("搜索课件名称");
            mAdapter = new CourseWareAdapter();
            recyclerView2.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent(LiveSearchActivity.this, CourseWarehouseDetailActivity.class);
                    intent.putExtra("id", mAdapter.getItem(position).getId());
                    intent.putExtra("name", mAdapter.getItem(position).getTitle());
                    startActivity(intent);
                }
            });
        } else {
            //直播
            povertyReliefAdapter = new OnlineLiveItemAdapter();
            recyclerView2.setAdapter(povertyReliefAdapter);
            povertyReliefAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    Intent intent = new Intent(LiveSearchActivity.this, LiveVideoViewActivity.class);
//                    intent.putExtra("videoPath", povertyReliefAdapter.getItem(position).getRtmp_play_url());
//                    intent.putExtra("liveStreaming", 1);
//                    intent.putExtra("videoId", povertyReliefAdapter.getItem(position).getId());
//                    if (povertyReliefAdapter.getItem(position).getRoom() != null && povertyReliefAdapter.getItem(position).getRoom().getData() != null) {
//                        intent.putExtra("roomId", povertyReliefAdapter.getItem(position).getRoom().getData().getId());
//                    }
//                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void initData() {
        if (isCourseWare) {
            String value = ShareUtil.getInstance().get("courseWareSearchData");
            if (value != null) {
                courseWareSearchData = new Gson().fromJson(value, new TypeToken<List<String>>() {
                }.getType());
                recentSearchAdapter.setNewData(courseWareSearchData);
            }
        } else {
            String value = ShareUtil.getInstance().get("historySearchData");
            if (value != null) {
                historySearchData = new Gson().fromJson(value, new TypeToken<List<String>>() {
                }.getType());
                recentSearchAdapter.setNewData(historySearchData);
            }
        }
        getHotSearch();
    }

    private void getHotSearch() {
        String include = "";
        if (!isCourseWare) {
            include = "live";
        }

        showLoadDialog();
        DataManager.getInstance().getHotSearch(new DefaultSingleObserver<HttpResult<HotSearchInfo>>() {
            @Override
            public void onSuccess(HttpResult<HotSearchInfo> result) {
                dissLoadDialog();
                recommendViewGroup.removeAllViews();
                if (result != null && result.getData() != null && result.getData().getHot_search() != null) {
                    List<String> items = new ArrayList<>();
                    if (isCourseWare) {
                        if (result.getData().getHot_search().getCourse() != null && result.getData().getHot_search().getCourse().size() > 0) {
                            items.addAll(result.getData().getHot_search().getCourse());
                        }
                    } else {
                        if (result.getData().getHot_search().getLive() != null && result.getData().getHot_search().getLive().size() > 0) {
                            items.addAll(result.getData().getHot_search().getLive());
                        }
                    }
                    for (int i = 0; i < items.size(); i++) {
                        View view = getLayoutInflater().inflate(R.layout.hot_search_item, null);
                        TextView textView = view.findViewById(R.id.tv_hot_recent_text);
                        textView.setText(items.get(i));
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etSearchRoom.setText(textView.getText().toString());
                                if (isCourseWare) {
                                    getCourseProducts();
                                } else {
                                    liveVideos();
                                }
                            }
                        });
                        recommendViewGroup.addView(view);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();


            }
        }, include);
    }

    private void liveVideos() {
        if (TextUtils.isEmpty(etSearchRoom.getText().toString())) {
            ToastUtil.showToast("请输入房间号");
            return;
        }
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("live_title", etSearchRoom.getText().toString());
        map.put("include", "room,user,cate");
        DataManager.getInstance().liveVideos(new DefaultSingleObserver<HttpResult<List<VideoLiveBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<VideoLiveBean>> result) {
                dissLoadDialog();
                historySearchData.add(etSearchRoom.getText().toString());
                ShareUtil.getInstance().save("historySearchData", new Gson().toJson(historySearchData));
                recentSearchAdapter.addData(etSearchRoom.getText().toString());
                if (result != null && result.getData() != null && result.getData().size() > 0) {
                    recyclerView2.setVisibility(View.VISIBLE);
                    part1.setVisibility(View.GONE);
                    povertyReliefAdapter.setNewData(result.getData());
                } else {
                    ToastUtil.showToast("搜索不到数据，请重新搜索");
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast("搜索不到数据，请重新搜索");

            }
        }, map);
    }

    private void getCourseProducts() {
        if (TextUtils.isEmpty(etSearchRoom.getText().toString())) {
            ToastUtil.showToast("请输入搜索内容");
            return;
        }
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("include", "course_info");
        map.put("filter[title]", etSearchRoom.getText().toString());
        DataManager.getInstance().getCourseProducts(new DefaultSingleObserver<HttpResult<List<VideoBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<VideoBean>> result) {
                dissLoadDialog();
                courseWareSearchData.add(etSearchRoom.getText().toString());
                ShareUtil.getInstance().save("courseWareSearchData", new Gson().toJson(courseWareSearchData));
                recentSearchAdapter.addData(etSearchRoom.getText().toString());
                if (result != null && result.getData() != null && result.getData().size() > 0) {
                    recyclerView2.setVisibility(View.VISIBLE);
                    part1.setVisibility(View.GONE);
                    mAdapter.setNewData(result.getData());
                } else {
                    ToastUtil.showToast("搜索不到数据，请重新搜索");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast("搜索不到数据，请重新搜索");
            }
        }, map);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_confirm, R.id.tv_clear_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
//                if (part1.getVisibility() == View.GONE) {
//                    recyclerView2.setVisibility(View.GONE);
//                    part1.setVisibility(View.VISIBLE);
//                } else {
                    finish();
//                }
                break;
            case R.id.tv_confirm:
                //搜索
                if (isCourseWare) {
                    getCourseProducts();
                } else {
                    liveVideos();
                }
                break;
            case R.id.tv_clear_all:
                //清空
                if(isCourseWare){
                    courseWareSearchData.clear();
                    ShareUtil.getInstance().save("courseWareSearchData", "");
                    mAdapter.setNewData(null);
                }else {
                    historySearchData.clear();
                    ShareUtil.getInstance().save("historySearchData", "");
                    recentSearchAdapter.setNewData(null);
                }
                ToastUtil.showToast("清空成功");
                break;
        }

    }
}
