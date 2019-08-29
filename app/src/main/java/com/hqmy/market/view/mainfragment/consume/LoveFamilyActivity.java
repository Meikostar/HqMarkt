package com.hqmy.market.view.mainfragment.consume;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.CategoryListdto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.FamilyLikeAdapter;
import com.hqmy.market.view.adapter.FamilyNewAapter;
import com.hqmy.market.view.adapter.FamilySortListAapter;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.MaxRecyclerView;
import com.hqmy.market.view.widgets.autoview.NoScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by rzb on 2019/5/16.
 * 爱心家庭
 */
public class LoveFamilyActivity extends BaseActivity {
    @BindView(R.id.family_layout_back)
    LinearLayout family_layout_back;
    @BindView(R.id.gridview_family_sort)
    NoScrollGridView gridview_family_sort;
    @BindView(R.id.family_new_gv)
    NoScrollGridView family_new_gv;
    @BindView(R.id.family_push_recy)
    MaxRecyclerView family_push_recy;
    @BindView(R.id.iv_banner_family_bottom)
    ImageView iv_banner_family_bottom;
    @BindView(R.id.find)
    TextView find;
    @BindView(R.id.family_ll_publish)
    LinearLayout family_ll_publish;
    private FamilySortListAapter mFamilySortListAapter;
    private List<CategoryListdto> categoryLists = new ArrayList<CategoryListdto>();
    private FamilyNewAapter mFamilyNewAapter;
    private List<NewListItemDto> newLists = new ArrayList<NewListItemDto>();
    private FamilyLikeAdapter mFamilyLikeAapter;
    private List<NewListItemDto> likeLists = new ArrayList<NewListItemDto>();

    public LoveFamilyActivity() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_love_family;
    }

    @Override
    public void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        family_push_recy.addItemDecoration(new RecyclerItemDecoration(3, 2));
        family_push_recy.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void initData() {
        initAdapter();
        getBanner();
        getFamilyTypes();
        getFamilyNewList();
        getFamilyLikeList();
    }

    @Override
    public void initListener() {
        bindClickEvent(family_layout_back, () -> {
            finish();
        });

        bindClickEvent(find, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("includeStr", "ax");
            gotoActivity(ProductSearchActivity.class, false, bundle);
        });

        bindClickEvent(family_ll_publish, () -> {
            gotoActivity(BabyPublishActivity.class);
        });

        family_new_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(BabyDetailActivity.PRODUCT_ID, mFamilyNewAapter.getItem(position).getId());
                bundle.putString(BabyDetailActivity.MALL_TYPE, "ax");
                gotoActivity(BabyDetailActivity.class, false, bundle);
            }
        });
        gridview_family_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryListdto item = mFamilySortListAapter.getItem(i);
                Bundle bundle = new Bundle();
                bundle.putString("productId", item.getId());
                gotoActivity(LoveFamilySecondHandActivity.class,false,bundle);
            }
        });
    }

    private void getBanner() {
        showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().getLoving_family_top();
                        if (bannerList != null) {
                            GlideUtils.getInstances().loadNormalImg(LoveFamilyActivity.this, iv_banner_family_bottom,
                                    Constants.WEB_IMG_URL_UPLOADS + bannerList.get(0).getPath());
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "loving_family_top");
    }

    private void getFamilyTypes() {
        showLoadDialog();
        DataManager.getInstance().getCategoryList(new DefaultSingleObserver<HttpResult<List<CategoryListdto>>>() {
            @Override
            public void onSuccess(HttpResult<List<CategoryListdto>> result) {
                dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        categoryLists.addAll(result.getData());
                        mFamilySortListAapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "ax");
    }

    private void getFamilyNewList() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("include", "user");
        DataManager.getInstance().getFamilyNewList(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<NewListItemDto> nliList = result.getData();
                        if (nliList != null) {
                            newLists.addAll(nliList);
                            mFamilyNewAapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "ax", map);
    }

    private void getFamilyLikeList() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("include", "user");
        DataManager.getInstance().getFamilyLikeList(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<NewListItemDto> likeList = result.getData();
                        if (likeList != null) {
                            likeLists.addAll(likeList);
                            mFamilyLikeAapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "ax", map);
    }


    private void initAdapter() {
        mFamilySortListAapter = new FamilySortListAapter(this, categoryLists);
        gridview_family_sort.setAdapter(mFamilySortListAapter);

        mFamilyNewAapter = new FamilyNewAapter(this, newLists);
        family_new_gv.setAdapter(mFamilyNewAapter);

        mFamilyLikeAapter = new FamilyLikeAdapter(likeLists, this);
        family_push_recy.setAdapter(mFamilyLikeAapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            getFamilyNewList();
            getFamilyLikeList();
        }
    }
}
