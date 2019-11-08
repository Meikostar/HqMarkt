package com.hqmy.market.view.activity;

import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.donkingliang.labels.LabelsView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AllCityDto;
import com.hqmy.market.bean.FirstClassItem;
import com.hqmy.market.bean.VideoLiveBean;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ScreenUtils;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.adapter.SingleFirstClassAdapter;
import com.hqmy.market.view.mainfragment.learn.CourseWareAdapter;
import com.hqmy.market.view.mainfragment.learn.OnlineLiveItemAdapter;
import com.hqmy.market.view.widgets.RecycleViewDivider_PovertyRelief;
import com.hqmy.market.view.widgets.autoview.NoScrollGridView;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LiveSearchActivity extends BaseActivity {

    @BindView(R.id.iv_search_back)
    ImageView      ivSearchBack;
    @BindView(R.id.et_search_room)
    EditText       etSearchRoom;
    @BindView(R.id.tv_cancel)
    TextView       tvCancel;
    @BindView(R.id.tv_confirm)
    TextView       tvConfirm;
    @BindView(R.id.recyclerView2)
    RecyclerView   recyclerView2;
    @BindView(R.id.tv_contury)
    TextView       tvContury;
    @BindView(R.id.iv_contury)
    ImageView      ivContury;
    @BindView(R.id.rl_contury)
    RelativeLayout rlContury;
    @BindView(R.id.tv_brand)
    TextView       tvBrand;
    @BindView(R.id.iv_brand)
    ImageView      ivBrand;
    @BindView(R.id.rl_brand)
    RelativeLayout rlBrand;
    @BindView(R.id.tv_type)
    TextView       tvType;
    @BindView(R.id.iv_type)
    ImageView      ivType;
    @BindView(R.id.rl_type)
    RelativeLayout rlType;
    @BindView(R.id.layout_opt_store)
    LinearLayout   layoutOptStore;
    private OnlineLiveItemAdapter povertyReliefAdapter;
    private CourseWareAdapter     mAdapter;

    List<String> historySearchData    = new ArrayList<>();
    List<String> courseWareSearchData = new ArrayList<>();
    private Boolean isCourseWare; //true为课件搜索 false 为首页搜索

    @Override
    public void initListener() {
      tvConfirm.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (levelsAllPopupWindowss != null && levelsAllPopupWindowss.isShowing()) {
                  levelsAllPopupWindowss.dismiss();
              }
              if (levelsAllPopupWindows != null && levelsAllPopupWindows.isShowing()) {
                  levelsAllPopupWindows.dismiss();
              }
              if (levelsAllPopupWindow != null && levelsAllPopupWindow.isShowing()) {
                  levelsAllPopupWindow.dismiss();
              }
              liveVideos();
          }
      });
        ivSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.live_search_layout;
    }

    @Override
    public void initView() {
        initRecyclerView();

        getCategorisContury();
        getConturyProduct();
        getLiveCates();
        liveVideos();

    }

    private void initRecyclerView() {
        isCourseWare = getIntent().getBooleanExtra("isCourseWare", false);

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

    private void initLevelsAllPopup1(List<AllCityDto> datas) {
        levelsAllPopupWindows = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.levels_all_popup_layout, null);
        singleLeftLV = (LabelsView) view.findViewById(R.id.tv_item_select_contury);

        levelsAllPopupWindows.setContentView(view);
        levelsAllPopupWindows.setBackgroundDrawable(new PaintDrawable());
        levelsAllPopupWindows.setFocusable(false);
        levelsAllPopupWindows.setHeight(ScreenUtils.getScreenH(this) * 1 / 2);
        levelsAllPopupWindows.setWidth(ScreenUtils.getScreenW(this));
        levelsAllPopupWindows.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        singleLeftLV.setSelectType(LabelsView.SelectType.SINGLE);
        singleLeftLV.setLabels(datas, new LabelsView.LabelTextProvider<AllCityDto>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, AllCityDto data) {
                return data.getName();
            }
        });

        //标签的选中监听
        singleLeftLV.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {

                AllCityDto dto= (AllCityDto) data;
                if(dto.getId().equals("-1")){
                    brandId=null;
                    tvBrand.setText("品牌");
                    tvBrand.setTextColor(getResources().getColor(R.color.my_color_999999));
                }else {
                    brandId=dto.getId();
                    tvBrand.setText(dto.getName());
                    tvBrand.setTextColor(getResources().getColor(R.color.my_color_F53C10));
                }
            }
        });
    }
    private String conturyId;
    private String brandId;
    private String TypeId;

    private void initLevelsAllPopup2(List<AllCityDto> datas) {
        levelsAllPopupWindow = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.levels_all_popup_layout, null);
        singleLeftLVs = (LabelsView) view.findViewById(R.id.tv_item_select_contury);

        levelsAllPopupWindow.setContentView(view);
        levelsAllPopupWindow.setBackgroundDrawable(new PaintDrawable());
        levelsAllPopupWindow.setFocusable(false);
        levelsAllPopupWindow.setHeight(ScreenUtils.getScreenH(this) * 1 / 2);
        levelsAllPopupWindow.setWidth(ScreenUtils.getScreenW(this));
        levelsAllPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        singleLeftLVs.setSelectType(LabelsView.SelectType.SINGLE);
        singleLeftLVs.setLabels(datas, new LabelsView.LabelTextProvider<AllCityDto>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, AllCityDto data) {
                return data.title;
            }
        });

        //标签的选中监听
        singleLeftLVs.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                AllCityDto dto= (AllCityDto) data;
                if(dto.getId().equals("-1")){
                    conturyId=null;
                    tvContury.setText("国家");
                    tvContury.setTextColor(getResources().getColor(R.color.my_color_999999));
                }else {
                    conturyId=dto.getId();
                    tvContury.setText(dto.title);
                    tvContury.setTextColor(getResources().getColor(R.color.my_color_F53C10));
                }
            }
        });
    }

    private void initLevelsAllPopup3(List<AllCityDto> datas) {
        levelsAllPopupWindowss = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.levels_all_popup_layout, null);
        singleLeftLVss = (LabelsView) view.findViewById(R.id.tv_item_select_contury);

        levelsAllPopupWindowss.setContentView(view);
        levelsAllPopupWindowss.setBackgroundDrawable(new PaintDrawable());
        levelsAllPopupWindowss.setFocusable(false);
        levelsAllPopupWindowss.setHeight(ScreenUtils.getScreenH(this) * 1 / 2);
        levelsAllPopupWindowss.setWidth(ScreenUtils.getScreenW(this));
        levelsAllPopupWindowss.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        singleLeftLVss.setSelectType(LabelsView.SelectType.SINGLE);
        singleLeftLVss.setLabels(datas, new LabelsView.LabelTextProvider<AllCityDto>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, AllCityDto data) {
                return data.cat_name;
            }
        });

        //标签的选中监听
        singleLeftLVss.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                AllCityDto dto= (AllCityDto) data;
                if(dto.getId().equals("-1")){
                    TypeId=null;
                    tvContury.setText("分类");
                    tvContury.setTextColor(getResources().getColor(R.color.my_color_999999));
                }else {
                    TypeId=dto.getId();
                    tvContury.setText(dto.getName());
                    tvContury.setTextColor(getResources().getColor(R.color.my_color_F53C10));
                }
            }
        });
    }

    //使用PopupWindow只显示一级分类
    private PopupWindow          levelsAllPopupWindow;
    private PopupWindow          levelsAllPopupWindows;
    private PopupWindow          levelsAllPopupWindowss;
    //只显示一个ListView
    private LabelsView           singleLeftLV;
    private LabelsView           singleLeftLVs;
    private LabelsView           singleLeftLVss;
    //分类数据
    private List<FirstClassItem> singleFirstList = new ArrayList<FirstClassItem>();


    @OnClick({R.id.rl_contury, R.id.rl_brand, R.id.rl_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_contury:
                if (levelsAllPopupWindow != null && levelsAllPopupWindow.isShowing()) {
                    setType(1,false);
                    levelsAllPopupWindow.dismiss();
                }else {
                    if(levelsAllPopupWindow==null){
                        return;
                    }else {
                        setType(1,true);
                        levelsAllPopupWindow.showAsDropDown(findViewById(R.id.store_div_line_three));
                        levelsAllPopupWindow.setAnimationStyle(-1);
                    }
                }
                if (levelsAllPopupWindows != null && levelsAllPopupWindows.isShowing()) {
                    levelsAllPopupWindows.dismiss();
                }
                if (levelsAllPopupWindowss != null && levelsAllPopupWindowss.isShowing()) {
                    levelsAllPopupWindowss.dismiss();
                }
                break;
            case R.id.rl_brand:
                if (levelsAllPopupWindows != null && levelsAllPopupWindows.isShowing()) {
                    setType(2,false);
                    levelsAllPopupWindows.dismiss();
                }else {
                    if(levelsAllPopupWindows==null){
                        return;
                    }else {
                        setType(2,true);
                        levelsAllPopupWindows.showAsDropDown(findViewById(R.id.store_div_line_three));
                        levelsAllPopupWindows.setAnimationStyle(-1);
                    }
                }
                if (levelsAllPopupWindow != null && levelsAllPopupWindow.isShowing()) {
                    levelsAllPopupWindow.dismiss();
                }
                if (levelsAllPopupWindowss != null && levelsAllPopupWindowss.isShowing()) {
                    levelsAllPopupWindowss.dismiss();
                }

                break;
            case R.id.rl_type:
                if (levelsAllPopupWindowss != null && levelsAllPopupWindowss.isShowing()) {
                    levelsAllPopupWindowss.dismiss();
                    setType(3,false);
                }else {
                    if(levelsAllPopupWindowss==null){
                        return;
                    }else {
                        setType(3,true);
                        levelsAllPopupWindowss.showAsDropDown(findViewById(R.id.store_div_line_three));
                        levelsAllPopupWindowss.setAnimationStyle(-1);
                    }
                }
                if (levelsAllPopupWindows != null && levelsAllPopupWindows.isShowing()) {
                    levelsAllPopupWindows.dismiss();
                }
                if (levelsAllPopupWindow != null && levelsAllPopupWindow.isShowing()) {
                    levelsAllPopupWindow.dismiss();
                }
                break;
        }
    }

    public void setType(int type, boolean ishow) {
        if (type == 1) {
            if (ishow) {
                ivContury.setImageResource(R.mipmap.sjx_sel);
                tvContury.setTextColor(getResources().getColor(R.color.my_color_333333));
            } else {
                ivContury.setImageResource(R.mipmap.sjx_unsel);
                tvContury.setTextColor(getResources().getColor(R.color.my_color_999999));
            }
            ivBrand.setImageResource(R.mipmap.sjx_unsel);
            tvBrand.setTextColor(getResources().getColor(R.color.my_color_999999));
            ivType.setImageResource(R.mipmap.sjx_unsel);
            tvType.setTextColor(getResources().getColor(R.color.my_color_999999));
        } else if (type == 2) {
            if (ishow) {
                ivBrand.setImageResource(R.mipmap.sjx_sel);
                tvBrand.setTextColor(getResources().getColor(R.color.my_color_333333));
            } else {
                ivBrand.setImageResource(R.mipmap.sjx_unsel);
                tvBrand.setTextColor(getResources().getColor(R.color.my_color_999999));

            }
            ivContury.setImageResource(R.mipmap.sjx_unsel);
            tvContury.setTextColor(getResources().getColor(R.color.my_color_999999));
            ivType.setImageResource(R.mipmap.sjx_unsel);
            tvType.setTextColor(getResources().getColor(R.color.my_color_999999));
        } else if (type == 3) {
            if (ishow) {
                ivType.setImageResource(R.mipmap.sjx_sel);
                tvType.setTextColor(getResources().getColor(R.color.my_color_333333));
            } else {
                ivType.setImageResource(R.mipmap.sjx_unsel);
                tvType.setTextColor(getResources().getColor(R.color.my_color_999999));
            }
            ivBrand.setImageResource(R.mipmap.sjx_unsel);
            tvBrand.setTextColor(getResources().getColor(R.color.my_color_999999));
            ivContury.setImageResource(R.mipmap.sjx_unsel);
            tvContury.setTextColor(getResources().getColor(R.color.my_color_999999));

        }
    }

    @Override
    public void initData() {
        if (isCourseWare) {
            String value = ShareUtil.getInstance().get("courseWareSearchData");
            if (value != null) {
                courseWareSearchData = new Gson().fromJson(value, new TypeToken<List<String>>() {
                }.getType());

            }
        } else {
            String value = ShareUtil.getInstance().get("historySearchData");
            if (value != null) {
                historySearchData = new Gson().fromJson(value, new TypeToken<List<String>>() {
                }.getType());

            }
        }

    }

    private void getConturyProduct() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("per_page", 200 + "");
        map.put("page", 1 + "");
        map.put("fields", "id,name");


        DataManager.getInstance().getConturyProducts(new DefaultSingleObserver<HttpResult<List<AllCityDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<AllCityDto>> result) {
                dissLoadDialog();
                if (null != result.getData() && result.getData().size() > 0) {

                    List<AllCityDto> aList = result.getData();
                    if (aList != null) {
                        AllCityDto allCityDto = new AllCityDto();
                        allCityDto.setName("全部");
                        allCityDto.setId("-1");
                        aList.add(0,allCityDto);
                        initLevelsAllPopup1(aList);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }

    public void getLiveCates() {
        showLoadDialog();
        DataManager.getInstance().getLiveCate(new DefaultSingleObserver<HttpResult<List<AllCityDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<AllCityDto>> result) {
                dissLoadDialog();
                if (null != result.getData() && result.getData().size() > 0) {

                    List<AllCityDto> aList = result.getData();
                    if (aList != null) {
                        AllCityDto allCityDto = new AllCityDto();
                        allCityDto.cat_name="全部";
                        allCityDto.setId("-1");
                        aList.add(0,allCityDto);
                        initLevelsAllPopup3(aList);
                    }
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, 0);
    }

    private void getCategorisContury() {
        //showLoadDialog();
        DataManager.getInstance().getConturyData(new DefaultSingleObserver<HttpResult<List<AllCityDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<AllCityDto>> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<AllCityDto> aList = result.getData();
                        AllCityDto allCityDto = new AllCityDto();
                        allCityDto.title="全部";
                        allCityDto.setId("-1");
                        aList.add(0,allCityDto);
                        if (aList != null) {
                            initLevelsAllPopup2(aList);
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

    private void liveVideos() {

//        if (TextUtils.isEmpty(etSearchRoom.getText().toString())) {
//            ToastUtil.showToast("请输入房间号");
//            return;
//        }
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("filter[live_title]", etSearchRoom.getText().toString());
        if(TextUtil.isNotEmpty(brandId)){
            map.put("filter[mall_brand_id]", brandId);
        }
        if(TextUtil.isNotEmpty(conturyId)){
            map.put("filter[new_category_id]", conturyId);
        }
        if(TextUtil.isNotEmpty(TypeId)){
            map.put("filter[live_video_cate_id]", TypeId);
        }


        map.put("include", "room,user,cate");
        DataManager.getInstance().liveVideos(new DefaultSingleObserver<HttpResult<List<VideoLiveBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<VideoLiveBean>> result) {
                dissLoadDialog();
                if (result != null && result.getData() != null && result.getData().size() > 0) {
                    recyclerView2.setVisibility(View.VISIBLE);
                    povertyReliefAdapter.setNewData(result.getData());
                } else {
                    povertyReliefAdapter.setNewData(null);
                    ToastUtil.showToast("搜索不到数据，请重新搜索");
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                povertyReliefAdapter.setNewData(null);
                ToastUtil.showToast("搜索不到数据，请重新搜索");

            }
        }, map);
    }


    //    @OnClick({R.id.tv_cancel, R.id.tv_confirm, R.id.tv_clear_all})
    //    public void onClick(View view) {
    //        switch (view.getId()) {
    //            case R.id.tv_cancel:
    //                //                if (part1.getVisibility() == View.GONE) {
    //                //                    recyclerView2.setVisibility(View.GONE);
    //                //                    part1.setVisibility(View.VISIBLE);
    //                //                } else {
    //                finish();
    //                //                }
    //                break;
    //            case R.id.tv_confirm:
    //                //搜索
    //
    //                liveVideos();
    //
    //                break;
    //            case R.id.tv_clear_all:
    //                //清空
    //                if (isCourseWare) {
    //                    courseWareSearchData.clear();
    //                    ShareUtil.getInstance().save("courseWareSearchData", "");
    //                    mAdapter.setNewData(null);
    //                } else {
    //                    historySearchData.clear();
    //                    ShareUtil.getInstance().save("historySearchData", "");
    //
    //                }
    //                ToastUtil.showToast("清空成功");
    //                break;
    //        }
    //
    //    }
    //
    //
    //    @Override
    //    protected void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        // TODO: add setContentView(...) invocation
    //        ButterKnife.bind(this);
    //    }
}
