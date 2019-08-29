package com.hqmy.market.view.widgets.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.hqmy.market.R;
import com.hqmy.market.common.Constants;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.activity.MyQRcodeActivity;
import com.hqmy.market.view.mainfragment.consume.ShopCartActivity;

public class HomeMorePopWindow extends PopupWindow {
    private FragmentActivity mContext;

    @SuppressLint("InflateParams")
    public HomeMorePopWindow(final FragmentActivity mContext ) {
        this.mContext = mContext;
        LayoutInflater inflater = (LayoutInflater) mContext
                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.popupwindow_add_home, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(content);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);

        RelativeLayout re_scan = (RelativeLayout) content.findViewById(R.id.re_scan);
        RelativeLayout re_cart = (RelativeLayout) content.findViewById(R.id.re_cart);
        RelativeLayout re_code = (RelativeLayout) content.findViewById(R.id.re_code);
        RelativeLayout re_entrance = (RelativeLayout) content.findViewById(R.id.re_entrance);
        re_scan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
                HomeMorePopWindow.this.dismiss();
            }
        });

        re_cart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(ShopCartActivity.MALL_TYPE, "gc");
                Intent intent = new Intent(new Intent(mContext, ShopCartActivity.class));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                HomeMorePopWindow.this.dismiss();
            }
        });
        re_code.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!TextUtils.isEmpty(ShareUtil.getInstance().getString(Constants.USER_TOKEN, ""))){
                   Intent intent = new Intent(new Intent(mContext, MyQRcodeActivity.class));
                   mContext.startActivity(intent);
               }else{
                   Intent intent = new Intent(new Intent(mContext, LoginActivity.class));
                   mContext.startActivity(intent);
               }
                HomeMorePopWindow.this.dismiss();
            }
        });
    }

    /**
     * 显示popupWindow
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }

    /**
     * 启动扫一扫
     */
    private void startScan() {
        Intent intent = new Intent(mContext,  CaptureActivity.class);
        ZxingConfig config = new ZxingConfig();
        config.setReactColor(R.color.my_color_009AFF);//设置扫描框四个角的颜色 默认为白色
        config.setScanLineColor(R.color.my_color_009AFF);//设置扫描线的颜色 默认白色
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        config.setShowbottomLayout(false);
        mContext.startActivityForResult(intent, Constants.HOME_REQUEST_CODE_SCAN);
    }
}
