package com.hqmy.market.view.widgets.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.common.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查看兑换券
 */
public class CouponCodeDialog extends Dialog {
    @BindView(R.id.tv_coupon_code)
    TextView tvCouponCode;

    Context mContext;

    String couponCode;
    public CouponCodeDialog(@NonNull Context context,String couponCode) {
        super(context, R.style.dialog_with_alpha);
        this.mContext = context;
        this.couponCode = couponCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_coupon_detail);
        ButterKnife.bind(this);
        tvCouponCode.setText(couponCode);
    }
    @OnClick({R.id.btn_cancel,R.id.btn_sure})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_sure:
                copy(couponCode);
                ToastUtil.showToast("复制成功");
                dismiss();
                break;
        }

    }
    /**
     * 复制内容到剪切板
     *
     * @param copyStr
     * @return
     */
    private boolean copy(String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
