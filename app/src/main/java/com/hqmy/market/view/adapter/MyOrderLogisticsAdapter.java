package com.hqmy.market.view.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.bean.ShipmentBean;
import com.hqmy.market.bean.TrackBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看物流
 */
public class MyOrderLogisticsAdapter extends BaseQuickAdapter<MyOrderItemDto, BaseViewHolder> {
    private List<ShipmentBean> shipmentBeanList = new ArrayList<>();

    public MyOrderLogisticsAdapter() {
        super(R.layout.adapter_myorder_logistics_layout, null);
    }

    public void setShipmentBeanList(List<ShipmentBean> mShipmentBeanList) {
        this.shipmentBeanList.clear();
        if (mShipmentBeanList != null && mShipmentBeanList.size() > 0) {
            this.shipmentBeanList.addAll(mShipmentBeanList);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderItemDto item) {
        helper.setText(R.id.tv_goods_price, "￥" + item.getPrice());
        if (item.getProduct() != null && item.getProduct().getData() != null) {
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.img_logistics_logo), Constants.WEB_IMG_URL_UPLOADS + item.getProduct().getData().getCover());
            if (item.getProduct().getData().isFlag2()) {
                helper.setText(R.id.tv_goods_price, item.getScore() + "积分");
            }
        }
        StringBuilder options = new StringBuilder();
        if (item.getOptions() != null && item.getOptions().get("尺码") != null) {
            options.append(item.getOptions().get("尺码") + ",");
        }
        if (item.getOptions() != null && item.getOptions().get("颜色") != null) {
            options.append(item.getOptions().get("颜色"));
        }
        helper.setText(R.id.tv_goods_color, options.toString());
        helper.setText(R.id.tv_goods_name, item.getTitle())
                .setText(R.id.tv_goods_num, "x" + item.getQty());
        helper.setText(R.id.tv_express_name, "");
        helper.setText(R.id.tv_express_no, "");
        helper.setGone(R.id.recy_logistics, false);
        if (shipmentBeanList != null && shipmentBeanList.size() > 0 && helper.getPosition() < shipmentBeanList.size()) {
            if (shipmentBeanList.get(helper.getPosition()).getInfo() != null) {
                helper.setText(R.id.tv_express_name, shipmentBeanList.get(helper.getPosition()).getInfo().getName());
            }
            helper.setText(R.id.tv_express_no, shipmentBeanList.get(helper.getPosition()).getExpress_no());
            if (shipmentBeanList.get(helper.getPosition()) != null && shipmentBeanList.get(helper.getPosition()).getTrack() != null && shipmentBeanList.get(helper.getPosition()).getTrack().getData() != null && shipmentBeanList.get(helper.getPosition()).getTrack().getData().size() > 0) {
                helper.setVisible(R.id.recy_logistics, true);
                setLogisticsListData(helper.getView(R.id.recy_logistics), shipmentBeanList.get(helper.getPosition()).getTrack().getData());
            }
        }

    }

    private void setLogisticsListData(RecyclerView recyclerView, List<TrackBean> items) {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MyOrderLogisticsItemAdapter(items));
    }
}
