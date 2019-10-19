package com.hqmy.market.view.mainfragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.BaseDto2;
import com.hqmy.market.bean.GroupListDto;
import com.hqmy.market.bean.StoreCategoryDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.common.utils.UIUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ErrorUtil;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.MainActivity;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.activity.MessageCenterActivity;
import com.hqmy.market.view.adapter.ClassifyOneAdapter;
import com.hqmy.market.view.adapter.ClassifyTwoAdapter;
import com.hqmy.market.view.mainfragment.consume.ProductSearchActivity;
import com.hqmy.market.view.widgets.HorizontalDividerItemDecoration;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Action;

/**
 * 商品分类
 * Created by dahai on 2019/01/18.
 */

public class ShopClassFragment extends BaseFragment {
    private static final String TAG = ShopClassFragment.class.getSimpleName();

    @BindView(R.id.rv_one)
    RecyclerView rvOne;
    @BindView(R.id.rv_two)
    RecyclerView rvTwo;
    Unbinder unbinder;
    @BindView(R.id.iv_ad)
    ImageView mIvAd;

    @BindView(R.id.iv_top_message)
    ImageView iv_top_message;
    @BindView(R.id.iv_calss_top_sweep)
    ImageView iv_top_qrCode;
    @BindView(R.id.top_search_view)
    LinearLayout top_search_view;

    private ClassifyOneAdapter     mClassifyOneAdapter;
    private ClassifyTwoAdapter     mClassifyTwoAdapter;
    private List<StoreCategoryDto> mListCategorys;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_class_layout;
    }

    @Override
    protected void initView() {
        mClassifyOneAdapter = new ClassifyOneAdapter();
        rvOne.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOne.setAdapter(mClassifyOneAdapter);
        rvOne.setSelected(true);
        rvOne.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.parseColor("#f1f1f1"))
                .size(UIUtil.dp2px(1))
                .build());
        mClassifyTwoAdapter = new ClassifyTwoAdapter(getActivity());
        rvTwo.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTwo.setAdapter(mClassifyTwoAdapter);
    }

    @Override
    protected void initData() {
        findStoreCategory();
        findOneCategory();
        findTwoCategory();
        getTopBanner();
    }
   private List<BaseDto2> lists=new ArrayList<>();
    @Override
    protected void initListener() {
        rvOne.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int prePos = mClassifyOneAdapter.selctedPos;  //之前的位置
                mClassifyOneAdapter.selctedPos = position; //之后选择的位置

                if (position != prePos) {//更新item的状态
                    mClassifyOneAdapter.notifyItemChanged(prePos);
                    mClassifyOneAdapter.notifyItemChanged(position);
                    if(position==0){
                         id=-1;
                        mClassifyTwoAdapter.setNewData(lists);
                        setAdData(mListCategorys.get(position).getImgUrl(), mListCategorys.get(position).getAdClickUrl());
                    }else if(position==1){
                        id=-2;
                        mClassifyTwoAdapter.setNewData(mDto1);
                        setAdData(mListCategorys.get(position).getImgUrl(), mListCategorys.get(position).getAdClickUrl());
                    }else {
                        id=(int)mListCategorys.get(position-2).getId();
                        mClassifyTwoAdapter.setNewData(mListCategorys.get(position-2).children.data);
                        setAdData(mListCategorys.get(position).getImgUrl(), mListCategorys.get(position-2).getAdClickUrl());
                    }

                }

                getTopBanner();
            }
        });

        bindClickEvent(top_search_view, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("includeStr", "consume_index");
            gotoActivity(ProductSearchActivity.class, false, bundle);
        });
        bindClickEvent(iv_top_qrCode, () -> {
//            Intent intent = new Intent();
//            intent.setAction(MainActivity.ACTION_SHOP_CLASS_CAPTURE_CODE);
//            getActivity().sendBroadcast(intent);
        }, 2500);
        bindClickEvent(iv_top_message, () -> {
            gotoActivity(MessageCenterActivity.class);
        });
    }
    private int id=0;
    private void getTopBanner() {
        //showLoadDialog();
        Map<String, String> map = new HashMap<>();
        if(id>=0){
            map.put("category_list_recommend_top", ""+id);

        }else if(id==-1){
            map.put("category_list_brand_list_top", ""+1);
        }else if(id==-2){
            map.put("category_list_recommend_top", ""+1);
        }

        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().getIndex_top();


                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "category_list_recommend_top",map);
    }
    private List<StoreCategoryDto> allData=new ArrayList<>();
    private void findStoreCategory() {
//        showLoadDialog();
        DataManager.getInstance().findStoreCategory(new DefaultSingleObserver<HttpResult<List<StoreCategoryDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<StoreCategoryDto>> data) {

                //                LogUtil.i(TAG, "--RxLog-Thread: onSuccess() = " + data);
//                dissLoadDialog();
                mListCategorys = data.getData();
                List<StoreCategoryDto> data1 = data.getData();
                StoreCategoryDto dto=new StoreCategoryDto();
                dto.title="为你推荐";
                dto.setId(-1);
                StoreCategoryDto dto1=new StoreCategoryDto();
                dto1.title="品牌管";
                dto1.setId(-2);
                allData.add(dto);
                allData.add(dto1);
                allData.addAll(data1);

                mClassifyOneAdapter.setNewData(allData);
                if (mListCategorys.size() > 0) {
                    StoreCategoryDto firstData = mListCategorys.get(0);
                    setAdData(firstData.getImgUrl(), firstData.getAdClickUrl());
                    mClassifyTwoAdapter.setNewData(firstData.children.data);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                String errMsg = ErrorUtil.getInstance().getErrorMessage(throwable);
                if(errMsg != null) {
                    if (errMsg.equals("appUserKey过期")) {
                        ToastUtil.showToast("appUserKey已过期，请重新登录");
                        ShareUtil.getInstance().cleanUserInfo();
                        gotoActivity(LoginActivity.class, true, null);
                    }
                }
//                dissLoadDialog();
            }
        });
    }
    private List<BaseDto2> mDto1;
    private List<BaseDto2> mDto2;
    private void findOneCategory() {
//        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("no_tree", ""+1);
        map.put("include", "mallBrands");
        map.put("filter[scopeHasMallBrands]", ""+1);

        DataManager.getInstance().findOtherCategory(new DefaultSingleObserver<HttpResult<List<BaseDto2>>>() {
            @Override
            public void onSuccess(HttpResult<List<BaseDto2>> data) {

                //                LogUtil.i(TAG, "--RxLog-Thread: onSuccess() = " + data);
//                dissLoadDialog();
                mDto1 = data.getData();

            }

            @Override
            public void onError(Throwable throwable) {
                String errMsg = ErrorUtil.getInstance().getErrorMessage(throwable);
                if(errMsg != null) {
                    if (errMsg.equals("appUserKey过期")) {
                        ToastUtil.showToast("appUserKey已过期，请重新登录");
                        ShareUtil.getInstance().cleanUserInfo();
                        gotoActivity(LoginActivity.class, true, null);
                    }
                }
//                dissLoadDialog();
            }
        },map);
    }
    private void findTwoCategory() {
//        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("no_tree", ""+1);
        map.put("filter[is_recommend]", ""+1);

        DataManager.getInstance().findOtherCategory(new DefaultSingleObserver<HttpResult<List<BaseDto2>>>() {
            @Override
            public void onSuccess(HttpResult<List<BaseDto2>> data) {

                //                LogUtil.i(TAG, "--RxLog-Thread: onSuccess() = " + data);
//                dissLoadDialog();
                mDto2=data.getData();
                BaseDto2 baseDto2 = new BaseDto2();
                baseDto2.title="常用分类";

                List<BaseDto2> dates=new ArrayList<>();
                dates.addAll(mDto2);
                BaseDto2 bD2 = new BaseDto2();
                bD2.data=dates;
                baseDto2.children=bD2;
                lists.add(baseDto2);
            }

            @Override
            public void onError(Throwable throwable) {
                String errMsg = ErrorUtil.getInstance().getErrorMessage(throwable);
                if(errMsg != null) {
                    if (errMsg.equals("appUserKey过期")) {
                        ToastUtil.showToast("appUserKey已过期，请重新登录");
                        ShareUtil.getInstance().cleanUserInfo();
                        gotoActivity(LoginActivity.class, true, null);
                    }
                }
//                dissLoadDialog();
            }
        },map);
    }



    private void setAdData(String imgUrl, String adClickUrl) {
        GlideUtils.getInstances().loadNormalImg(getActivity(), mIvAd, Constants.WEB_IMG_URL_UPLOADS + imgUrl, R.mipmap.img_default_3);
        bindClickEvent(mIvAd, new Action() {
            @Override
            public void run() throws Exception {
                //TODO 实现 跳转 adClickUrl
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
