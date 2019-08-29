package com.hqmy.market.view.widgets.dialog;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import com.hqmy.market.R;

/**
 * Created by Administrator on 2017/2/15.
 */

public class FeeTipDialog extends BaseDialog {
    private TextView messageTv;//消息提示文本


    public FeeTipDialog(Activity context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fee_tip_layout;
    }

    /**
     * 初始化界面控件
     */
    @Override
    protected void initView() {
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        btn_sure = (Button) findViewById(R.id.yes);
        btn_cancel = (Button) findViewById(R.id.no);
        messageTv = (TextView) findViewById(R.id.message);
    }

    public void setMessageLocation(int location) {
        if (messageTv != null)
            messageTv.setGravity(location);
    }

    /**
     * 初始化界面控件的显示数据
     */
    @Override
    protected void initData() {
        //如果用户自定了title和message
        if (messageStr != null) {
            messageTv.setText(messageStr);
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            btn_sure.setText(yesStr);
        }
        if (noStr != null) {
            btn_cancel.setText(noStr);
        }
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    @Override
    protected void initEvent() {
        bindClickEvent(btn_sure, () -> {
            if (onYesClickListener != null) {
                onYesClickListener.onYesClick();
            }
        });
        bindClickEvent(btn_cancel, () -> {
            dismiss();
            if (onNoClickListener != null) {
                onNoClickListener.onNoClick();
            }

        });
    }

}
