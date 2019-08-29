package com.hqmy.market.view.mainfragment.consume;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AddressDto;
import com.hqmy.market.bean.ShopCartInfoDto;
import com.hqmy.market.bean.ShopCartListDto;
import com.hqmy.market.bean.ShopCartListItemDto;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.activity.AddShippingAddressActivity;
import com.hqmy.market.view.adapter.ShopCartAdapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.autoview.MaxRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import butterknife.BindView;

/**
 * Created by rzb on 2019/6/2.
 */
public class ShopCartActivity extends BaseActivity {
    public static final String MALL_TYPE = "mall_type";
    @BindView(R.id.iv_shop_crat_back)
    ImageView       iv_shop_crat_back;
    @BindView(R.id.tv_title_right)
    TextView        tv_title_right;
    @BindView(R.id.recy_shop_cart)
    MaxRecyclerView shopCartRecyclerView;
    @BindView(R.id.rb_cart_factory)
    RadioButton     rb_cart_factory;
    @BindView(R.id.rb_cart_mall)
    RadioButton     rb_cart_mall;
    @BindView(R.id.tv_shop_cart_submit)
    TextView        tv_shop_cart_submit;
    @BindView(R.id.cb_shop_cart_all_sel)
    CheckBox        cb_shop_cart_all_sel;
    @BindView(R.id.cb_shop_cart_price)
    TextView        cb_shop_cart_price;
    private ShopCartAdapter shopCartAdapter;
    private List<AddressDto> mAddressDatas = new ArrayList<>();
    private AddressDto defaultAddress = null;
    private String strMallType = "gc";
    private ArrayList<String> rowList  = new ArrayList<String>();
    private Set<ShopCartListItemDto> shopSelectList = new HashSet<>();//被选中的资源
    private boolean again;
    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_cart;
    }

    @Override
    public void initView() {
        if(getIntent() != null){
            Bundle bundle = getIntent().getExtras();
            if(bundle != null) {
                strMallType = bundle.getString(MALL_TYPE);
                again = bundle.getBoolean("again");
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShopCartActivity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        shopCartRecyclerView.setLayoutManager(linearLayoutManager);
        shopCartAdapter = new ShopCartAdapter(null, strMallType);
        shopCartRecyclerView.setAdapter(shopCartAdapter);
    }

    @Override
    public void initData() {
        getAddressListData();
        if(strMallType.equals("gc")){
            rb_cart_factory.setChecked(true);
            rb_cart_mall.setChecked(false);
        }else if(strMallType.equals("lm")){
            rb_cart_factory.setChecked(false);
            rb_cart_mall.setChecked(true);
        }
        if (again){
            again();
        }else {
            findShoppingCartList(strMallType);
        }
    }

    @Override
    public void initListener() {
        bindClickEvent(iv_shop_crat_back, () -> {
            finish();
        });
        bindClickEvent(tv_title_right, () -> {
            String strRight = tv_title_right.getText().toString().trim();
            if(strRight.equals("编辑")){
                tv_title_right.setText("完成");
                tv_shop_cart_submit.setText("删除");

            }else if(strRight.equals("完成")){
                tv_title_right.setText("编辑");
                tv_shop_cart_submit.setText("去结算");
            }
        });
        bindClickEvent(rb_cart_factory, () -> {
            findShoppingCartList("gc");
            strMallType = "gc";
        });
        bindClickEvent(rb_cart_mall, () -> {
            findShoppingCartList("lm");
            strMallType = "lm";
        });

        shopCartAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ShopCartListDto item = shopCartAdapter.getItem(position);
                List<ShopCartListItemDto> itemList = item.getProducts();
                CheckBox checkBox = view.findViewById(R.id.cb_shop_cart);
                switch (view.getId()) {
                    case R.id.cb_shop_cart:
                        if (!checkBox.isChecked()) {
                            item.setSelect(false);
                            if (itemList.size() > 0) {
                                for (int i = 0; i < itemList.size(); i++) {
                                    itemList.get(i).setSelect(false);
                                    shopSelectList.remove(itemList.get(i));
                                }
                                shopCartAdapter.removeSelectedList(itemList);
                             }
                            updateBottomView(shopSelectList);
                        } else {
                            item.setSelect(true);
                            if (itemList.size() > 0) {
                                for (int i = 0; i < itemList.size(); i++) {
                                    itemList.get(i).setSelect(true);
                                    shopSelectList.add(itemList.get(i));
                                }
                                shopCartAdapter.addSelectedList(itemList);
                            }
                            updateBottomView(shopSelectList);
                        }
                        shopCartAdapter.notifyDataSetChanged();
                        break;
                 }
             }
        });

        shopCartAdapter.setUpdateBottomClickListener(new ShopCartAdapter.UpdateBottomListener() {
            @Override
            public void OnBottomListener(Set<ShopCartListItemDto> selectList) {
                shopSelectList.clear();
                shopSelectList.addAll(selectList);
                updateBottomView(selectList);
            }
        });

        shopCartAdapter.setRefreshListsListener(new ShopCartAdapter.RefreshListsListener() {
            @Override
            public void OnRefreshListener(boolean isFresh) {
                if(isFresh){
                    findShoppingCartList(strMallType);
                }
            }
        });

        bindClickEvent(cb_shop_cart_all_sel, () -> {
            setAllCheckBoxStatus();
        }, 500);

        bindClickEvent(tv_shop_cart_submit, () -> {
            if(shopSelectList.size() > 0) {
                if(tv_shop_cart_submit.getText().toString().equals("去结算")) {
                    Iterator<ShopCartListItemDto> items = shopSelectList.iterator();
                    while (items.hasNext()) {
                        ShopCartListItemDto shopCartListItemDto = items.next();
                        rowList.add(shopCartListItemDto.getRowId());
                    }
                    if (defaultAddress != null) {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList(ConfirmOrderActivity.ROW_STR, rowList);
                        bundle.putString(ConfirmOrderActivity.MALL_TYPE, strMallType);
                        bundle.putSerializable(ConfirmOrderActivity.ADDRESS_DETAIL, defaultAddress);
                        gotoActivity(ConfirmOrderActivity.class, false, bundle);
                    } else {
                        gotoActivity(AddShippingAddressActivity.class);
                    }
                }else if(tv_shop_cart_submit.getText().toString().equals("删除")){

                }
            }else{
                ToastUtil.showToast("请先选择商品");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 获取购物车列表
     */
    private void findShoppingCartList(String mall_type) {
        //showLoadDialog();
        DataManager.getInstance().findShoppingCartList(new DefaultSingleObserver<HttpResult<ShopCartInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<ShopCartInfoDto> result) {
                //dissLoadDialog();
                if(result != null) {
                    if(result.getData() != null) {
                        ShopCartInfoDto shopCartInfoDto = result.getData();
                        if (shopCartInfoDto != null) {
                            if (shopCartInfoDto.getShops() != null && shopCartInfoDto.getShops().size()>0) {
                                shopCartAdapter.setNewData(shopCartInfoDto.getShops());
                            } else {
                                shopCartAdapter.setNewData(shopCartInfoDto.getShops());
                                EmptyView emptyView = new EmptyView(ShopCartActivity.this);
                                emptyView.setTvEmptyTip("购物车暂无数据");
                                shopCartAdapter.setEmptyView(emptyView);
                            }
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, mall_type);
    }
    /**
     * 再来一单
     */
    private void again() {
        showLoadDialog();
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> product_id=bundle.getStringArrayList("product_id");
        ArrayList<String> qty = bundle.getStringArrayList("qty");
        ArrayList<String> stock_id = bundle.getStringArrayList("stock_id");
        HashMap<String,Object> map = new HashMap<>();
        map.put("qty",qty);
        if(product_id!= null && product_id.size()>0){
            for(int i =0;i< product_id.size();i++){
                if(!TextUtils.isEmpty(product_id.get(i))){
                    map.put("product_id["+i+"]",product_id.get(i)) ;
                }
            }
        }
        if(stock_id!= null && stock_id.size()>0){
            for(int i =0;i< stock_id.size();i++){
                if(!TextUtils.isEmpty(stock_id.get(i))){
                    map.put("stock_id["+i+"]",stock_id.get(i)) ;
                }
            }
        }
        DataManager.getInstance().addShoppingCart(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                dissLoadDialog();
                findShoppingCartList(strMallType);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                findShoppingCartList(strMallType);
            }
        }, strMallType, map);
    }

    private void setAllCheckBoxStatus() {
        if (!cb_shop_cart_all_sel.isChecked()) {
            cb_shop_cart_all_sel.setChecked(false);
            shopSelectList.clear();
        } else {
            cb_shop_cart_all_sel.setChecked(true);
        }
        if (shopCartAdapter.getData() != null && !shopCartAdapter.getData().isEmpty()) {
            for (int i = 0; i < shopCartAdapter.getData().size(); i++) {
                shopCartAdapter.getData().get(i).setSelect(cb_shop_cart_all_sel.isChecked());
                if (cb_shop_cart_all_sel.isChecked()) {//全选按钮选中时添加到selectList
                    ShopCartListDto item = shopCartAdapter.getData().get(i);
                    item.setSelect(true);
                    for(int j=0; j<item.getProducts().size(); j++) {
                        item.getProducts().get(j).setSelect(true);
                        shopSelectList.add(item.getProducts().get(j));
                    }
                }else{
                    ShopCartListDto item = shopCartAdapter.getData().get(i);
                    item.setSelect(false);
                    for(int j=0; j<item.getProducts().size(); j++) {
                        item.getProducts().get(j).setSelect(false);
                    }
                }
            }
        }
        updateBottomView(shopSelectList);
        shopCartAdapter.notifyDataSetChanged();
    }

    /**
     * 更新底部View状态
     * @param object 选中的集合
     */
    private void updateBottomView(Set<ShopCartListItemDto> object) {
        double price = 0;
        if (object != null && !object.isEmpty()) {
            Iterator<ShopCartListItemDto> items = object.iterator();
            while (items.hasNext()) {
                ShopCartListItemDto item = items.next();
                price += Double.valueOf(item.getPrice()) * Double.valueOf(item.getQty());;
            }
        }
        cb_shop_cart_price.setText(price + "");
        //全选状态
        if (shopCartAdapter.getData() != null && !shopCartAdapter.getData().isEmpty()) {
            int totalNum = 0;
            for(int m=0; m<shopCartAdapter.getData().size(); m++){
              totalNum = totalNum + shopCartAdapter.getData().get(m).getProducts().size();
            }
            if (totalNum == object.size()) {
                cb_shop_cart_all_sel.setChecked(true);
            } else {
                cb_shop_cart_all_sel.setChecked(false);
            }
        }
    }

    /**
     * 获取收货地址
     */
    private void getAddressListData() {
        //showLoadDialog();
        DataManager.getInstance().getAddressesList(new DefaultSingleObserver<List<AddressDto>>() {
            @Override
            public void onSuccess(List<AddressDto> addressDtos) {
                //dissLoadDialog();
                mAddressDatas.clear();
                mAddressDatas.addAll(addressDtos);
                if(mAddressDatas.size() > 0) {
                    for (int i = 0; i < mAddressDatas.size(); i++) {
                        if (mAddressDatas.get(i).getIs_default().equals("1")) {
                            defaultAddress = mAddressDatas.get(i);
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
}
