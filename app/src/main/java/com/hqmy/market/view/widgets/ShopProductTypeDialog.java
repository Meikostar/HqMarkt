package com.hqmy.market.view.widgets;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donkingliang.labels.LabelsView;
import com.hqmy.market.bean.BaseDto;
import com.jakewharton.rxbinding2.view.RxView;
import com.hqmy.market.R;
import com.hqmy.market.bean.CommodityDetailAttrDto;
import com.hqmy.market.bean.CommodityDetailAttrItemDto;
import com.hqmy.market.bean.CommodityDetailInfoDto;
import com.hqmy.market.bean.GoodsAttrDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.view.adapter.GoodsAttrAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 产品类型、规格
 * Created by rzb on 2018/6/18.
 */
public class ShopProductTypeDialog extends Dialog implements GoodsAttrAdapter.GoodsSpecListener{
    private static final String TAG = ShopProductTypeDialog.class.getSimpleName();
    private Context mContext;
    @BindView(R.id.iv_dialog_select_commodity_img)
    ImageView iv_dialog_select_commodity_img;
    @BindView(R.id.tv_dialog_select_commodity_price)
    TextView tv_dialog_select_commodity_price;
    @BindView(R.id.tv_dialog_select_commodity_num)
    TextView tv_dialog_select_commodity_num;
    @BindView(R.id.but_dialog_select_commodity_submit)
    Button butSubmit;
    @BindView(R.id.iv_dialog_select_commodity_clean)
    ImageView clean;
    @BindView(R.id.iv_dialog_select_commodity_decrease)
    ImageView iv_dialog_select_commodity_decrease;
    @BindView(R.id.tv_dialog_select_commodity_count)
    TextView tv_dialog_select_commodity_count;
    @BindView(R.id.iv_dialog_select_commodity_increase)
    ImageView iv_dialog_select_commodity_increase;
    @BindView(R.id.tv_commodity_count)
    TextView tv_commodity_count;
    @BindView(R.id.tv_commodity_total_price)
    TextView tv_commodity_total_price;
    @BindView(R.id.rv_dialog_select_commodity)
    RecyclerView recyclerView;
    private int goodscount = 1;//数量
    private boolean isShoppingCart = false; //是否添加到购物车
    private ShopProductTypeListener shopProductTypeListener;
    private GoodsAttrAdapter        goodsAttrAdapter;
    private String stockId = null;
    private String productId = null;
    private List<CommodityDetailAttrItemDto> attrItemList;
    private LinearLayoutManager mLinearLayoutManager;
    private List<String> attrList = new ArrayList<String>();
    private String strPrice = null;
    private CommodityDetailInfoDto cDetailInfo;
    private int minBuy;

    public ShopProductTypeDialog(Context context, CommodityDetailInfoDto commodityDetailInfoDto, CommodityDetailAttrDto object, String imageUrl, String price, String title, ShopProductTypeListener listener, boolean isShoppingCart) {
        super(context, R.style.dialog_with_alpha);
//      setCanceledOnTouchOutside(false);//设置点击外部不可以取消;
        this.mContext = context;
        setContentView(R.layout.dialog_shop_product_type);
        ButterKnife.bind(this);
        this.shopProductTypeListener = listener;
        this.isShoppingCart = isShoppingCart;
        this.cDetailInfo = commodityDetailInfoDto;
        initView();
        initData(object, imageUrl, price, title);
        initListener();
    }

    private void initView() {
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (display.getWidth());
        Window window = getWindow();
        window.setAttributes(p);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomAnimStyle);
    }

    private void initListener() {
        bindClickEvent(iv_dialog_select_commodity_decrease, () -> {
            if (minBuy > 0 && goodscount - 1 < minBuy){
                ToastUtil.showToast("购买数量不能小于"+minBuy);
                return;
            }
            if (goodscount <= 1) {
                goodscount = 1;
            } else {
                --goodscount;
            }
            tv_dialog_select_commodity_count.setText("" + goodscount);
            tv_commodity_count.setText("" + goodscount);
            tv_commodity_total_price.setText("￥"+String.valueOf(Double.valueOf(strPrice) * Double.parseDouble("" + goodscount)));
        });
        bindClickEvent(iv_dialog_select_commodity_increase, () -> {
              ++goodscount;
              tv_dialog_select_commodity_count.setText("" + goodscount);
              tv_commodity_count.setText("" + goodscount);
              tv_commodity_total_price.setText("￥"+String.valueOf(Double.parseDouble(strPrice) * Double.parseDouble("" + goodscount)));
        });
        bindClickEvent(clean, () -> {
            hide();
        });
        bindClickEvent(butSubmit, () -> {
            if (null != shopProductTypeListener) {
                List<GoodsAttrDto> attList = goodsAttrAdapter.getData();
                if(attList != null) {
                    if(attList.size() > 0) {
                        getStockId(attList);
                    }else{
                        productId = cDetailInfo.getId();
                    }
                }else{
                    productId = cDetailInfo.getId();
                }
                if(isShoppingCart){
                    shopProductTypeListener.callbackSelect(stockId, productId, tv_commodity_count.getText().toString());

                }else {

                }
             }
            hide();
        });
    }

    private void initData(CommodityDetailAttrDto object, String imageUrl, String price, String title) {
        if (object == null) {
            return;
        }
        strPrice = price;
        String imgUrl = Constants.WEB_IMG_URL_UPLOADS + imageUrl;
        GlideUtils.getInstances().loadNormalImg(mContext, iv_dialog_select_commodity_img, imgUrl);
        tv_dialog_select_commodity_price.setText("¥" + price);
        tv_dialog_select_commodity_num.setText(title);
        tv_commodity_total_price.setText("¥" + price);
        if (cDetailInfo != null && cDetailInfo.getExt() != null && !TextUtils.isEmpty(cDetailInfo.getExt().getMin_buy())){
            minBuy = Integer.valueOf(cDetailInfo.getExt().getMin_buy());
            goodscount = minBuy;
            tv_commodity_count.setText(cDetailInfo.getExt().getMin_buy());
            tv_dialog_select_commodity_count.setText("" + goodscount);
            tv_commodity_total_price.setText(String.valueOf(Double.valueOf(strPrice) * Double.parseDouble("" + goodscount)));
        }else {
            tv_commodity_count.setText("1");
        }
        if (isShoppingCart) {
            butSubmit.setText("加入购物车");
        }else {
            butSubmit.setText("立即购买");
        }
        List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
        attrItemList = object.getData();
        List<GoodsAttrDto> gadList = null;
        if(attrItemList != null) {
            if(attrItemList.size() > 0) {
                for (int i = 0; i < attrItemList.size(); i++) {
                    CommodityDetailAttrItemDto commodityDetailAttrItemDto = attrItemList.get(i);
                    String attrStr = commodityDetailAttrItemDto.getAttr_str();
                    String[] attrArray = attrStr.split("\\|");
                    HashMap<String, String> attrMp = new HashMap<String, String>();
                    for (int j = 0; j < attrArray.length; j++) {
                        String[] itemAttrArray = attrArray[j].split(":");
                        attrMp.put(itemAttrArray[0], itemAttrArray[1]+","+commodityDetailAttrItemDto.getPrice());
                    }
                    mapList.add(attrMp);
                }
                Set<String> nameSets = mapList.get(0).keySet();
                gadList = new ArrayList<GoodsAttrDto>();
                for (String key : nameSets) {
                    List<BaseDto> valueList = new ArrayList<BaseDto>();
                    for (int k = 0; k < mapList.size(); k++) {
                        BaseDto baseDto = new BaseDto();
                        String con = mapList.get(k).get(key);
                        String[] split = con.split(",");

                        baseDto.name=split[0];
                        baseDto.price=split[1];
                        valueList.add(baseDto);
                    }

                    GoodsAttrDto gaDto = new GoodsAttrDto();
                    gaDto.setKey(key);
                    for (int i = 0; i < valueList.size() - 1; i++) {
                        for (int j = valueList.size() - 1; j > i; j--) {
                            if (valueList.get(j).equals(valueList.get(i))) {
                                valueList.remove(j);
                            }
                        }
                    }
                    gaDto.data=valueList;
                    gadList.add(gaDto);
                }
            }
        }
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setFocusable(false);
        goodsAttrAdapter = new GoodsAttrAdapter(gadList,mContext);
        goodsAttrAdapter.setGoodsSpecListener(new GoodsAttrAdapter.GoodsSpecListener() {
            @Override
            public void callbackGoodsSpec(String attStrs) {
                strPrice = attStrs;
                tv_dialog_select_commodity_price.setText("¥" + attStrs);

                tv_commodity_total_price.setText("¥" + attStrs);
            }
        });
        recyclerView.setAdapter(goodsAttrAdapter);
    }

    private void getStockId(List<GoodsAttrDto> goodsAttrDtoList){
        String strAttr = null;
        for(int i=0; i<goodsAttrDtoList.size(); i++){
            LinearLayout layoutView = (LinearLayout) mLinearLayoutManager.findViewByPosition((i));
            LabelsView  mLabel = (LabelsView)layoutView.getChildAt(2);
            BaseDto strLabel = (BaseDto) mLabel.getSelectLabelDatas().get(0);
            String strItem = goodsAttrDtoList.get(i).getKey() + ":" + strLabel.name;
            attrList.add(strItem);
        }

        for(int j=attrList.size()-1; j>=0; j--){
            if(j==attrList.size()-1){
                strAttr = attrList.get(attrList.size()-1);
            }else{
                strAttr = strAttr + "|" + attrList.get(j);
            }
        }

        for(int k=0; k<attrItemList.size(); k++){
             if(strAttr.equals(attrItemList.get(k).getAttr_str())){
                stockId =  attrItemList.get(k).getId();
                return;
             }
        }
    }

    /**
     * 基本点击事件跳转默认节流1000毫秒
     * @param view   绑定的view
     * @param action 跳转的Acticvity
     */
    protected void bindClickEvent(View view, final Action action) {
        bindClickEvent(view, action, 1000);
    }

    protected void bindClickEvent(View view, final Action action, long frequency) {
        Observable<Object> observable = RxView.clicks(view);
        if (frequency > 0) {
            observable.throttleFirst(frequency, TimeUnit.MILLISECONDS);
        }
        observable.subscribe(trigger -> action.run());
    }

    @Override
    public void callbackGoodsSpec(String attStrs) {
        String[] attArray = attStrs.split(",");
        for(int i=0; i<attrItemList.size(); i++) {
            boolean flag = false;
            for (int j = 0;  j<attArray.length; i++) {
                if (!attrItemList.get(i).getAttr_str().contains(attArray[j])) {
                    continue;
                }
            }
            if(!flag){
              stockId = attrItemList.get(i).getId();
            }
        }
    }

    public interface ShopProductTypeListener {
        /**
         * 回调
         * @param stockId
         */
        void callbackSelect(String stockId, String productId, String countBuy);
    }
}
