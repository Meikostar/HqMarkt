package com.hqmy.market.view.widgets.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.hqmy.market.R;


/**
 * 通知的弹出框
 */

public class PopDialog extends Dialog {
    private int layoutResID;
    private Window dialogWindow;

    public PopDialog(Context context, int layoutResID) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);         //去掉dialog标题
        dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);
        dialogWindow.setWindowAnimations(R.style.AnimBottom);
        this.layoutResID = layoutResID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layoutResID);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();       //布局
        WindowManager windowManager = dialogWindow.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        lp.width = display.getWidth();
        dialogWindow.setAttributes(lp);

    }
}