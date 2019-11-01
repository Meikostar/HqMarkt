package com.hqmy.market.view.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.BaseDto3;
import com.hqmy.market.bean.FootInfoDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.bean.ShopCartDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.SwipeRefreshLayoutUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.activity.MyFootprintActivity;
import com.hqmy.market.view.adapter.BrandsAdapter;
import com.hqmy.market.view.adapter.ProductListAdapter;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.autoview.SuperSwipeRefreshLayout;
import com.hqmy.market.view.widgets.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 购物车
 * Created by dahai on 2019/01/18.
 */

public class BrandFragment extends BaseFragment implements LoadingDialog.LoadingListener {
    private static final String TAG = BrandFragment.class.getSimpleName();


    Unbinder unbinder;
    @BindView(R.id.common_recycler)
    RecyclerView            recySpikeCart;
    @BindView(R.id.common_refresh)
    SuperSwipeRefreshLayout refreshLayout;

//    private ShopSpikeAdapter shopSpikeAdapter;
    SwipeRefreshLayoutUtil swipeRefreshLayoutUtil;

    private Set<ShopCartDto> selectList = new HashSet<>();//被选中的资源
    public String            id;

    public void setId(String id) {
        this.id = id;
    }
    public int state;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_brand_layout;
    }

    private LoadingDialog loadingDialog;
    private BrandsAdapter mAdapter;
    @Override
    protected void initView() {
        loadingDialog = LoadingDialog.show(getActivity());
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCallbackListener(this);

        recySpikeCart.addItemDecoration(new RecyclerItemDecoration(0, 1));
        recySpikeCart.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mAdapter = new BrandsAdapter(datas,getActivity());
        recySpikeCart.setAdapter(mAdapter);



    }

    private List<NewListItemDto>       datas = new ArrayList<>();
    private HashMap<String, String> mParamsMaps;
    private             int                     mCurrentPage = 1;
    @Override
    protected void initData() {

        mParamsMaps = new HashMap<>();
        getConturyProduct();


    }


    private void getConturyProduct() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("page",  currentPage+"");
        if(!id.equals("-1")){
            map.put("filter[scopeMallCategory]", id + "");
        }

        DataManager.getInstance().getConturyProduct(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();
                if (null != result.getData() && result.getData().size() > 0) {

                    if (currentPage == 1) {

                        mAdapter.setNewData(result.getData());
                        refreshLayout.setRefreshing(false);
                    } else {

                        mAdapter.addData(result.getData());
                        refreshLayout.setLoadMore(false);
                    }



                } else {
                    EmptyView emptyView = new EmptyView(getActivity());

                    emptyView.setTvEmptyTip("暂无数据");

                    mAdapter.setEmptyView(emptyView);


                }
                swipeRefreshLayoutUtil.isMoreDate(mCurrentPage, Constants.PAGE_SIZE, result.getMeta().getPagination().getTotal());


            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }

    /**
     * 滚动监听
     *
     * @param rows 每页加载行数
     */
    private int currentPage=1;
    private void setScrollListener(int rows) {
        swipeRefreshLayoutUtil = new SwipeRefreshLayoutUtil();
        swipeRefreshLayoutUtil.setBgColor(R.color.my_color_greens);
//        swipeRefreshLayout.setEnablePull(false);
        swipeRefreshLayoutUtil.setSwipeRefreshView(refreshLayout, new SwipeRefreshLayoutUtil.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                currentPage = Constants.PAGE_NUM;
                swipeRefreshLayoutUtil.setCanLoadMore(true);
                getConturyProduct();

            }

            @Override
            public void onLoadMore() {
                swipeRefreshLayoutUtil.setCanLoadMore(true);
                currentPage++;
                getConturyProduct();

            }
        },R.color.my_color_f5f5f5);
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
        }
    }

    @Override
    public void onResume() {
        selectList.clear();


        super.onResume();
    }
    @Override
    protected void dissLoadDialog() {
        super.dissLoadDialog();
        if (refreshLayout != null) {
            if(loadingDialog!=null&&refreshLayout!=null){
                loadingDialog.cancelDialog();
                refreshLayout.setRefreshing(false);
                refreshLayout.setLoadMore(false);
            }

        }
    }
    @Override
    protected void initListener() {


        setScrollListener(20);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadCompleted(int level) {
        if (level == 5) {
            loadingDialog.cancelDialog();

        }
    }
}
