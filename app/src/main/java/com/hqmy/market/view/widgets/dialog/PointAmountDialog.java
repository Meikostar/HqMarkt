package com.hqmy.market.view.widgets.dialog;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.view.adapter.PointAmountAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 打赏金额Dialog
 * Created by Administrator on 2017/2/15.
 */

public class PointAmountDialog extends BaseDialog {
    private RecyclerView recyclerView;
    private EditText etPointAmount;
    private OnYesClickListener1 onYesClickListener1;
    private PointAmountAdapter mAdapter;


    public PointAmountDialog(Activity context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_point_amount_layout;
    }

    /**
     * 初始化界面控件
     */
    @Override
    protected void initView() {
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (display.getWidth());
        Window window = getWindow();
        window.setAttributes(p);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomAnimStyle);

        btn_sure = (Button) findViewById(R.id.yes);
        btn_cancel = (Button) findViewById(R.id.no);
        etPointAmount = findViewById(R.id.et_point_amount);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        List<String> strings = new ArrayList<>();
        strings.add("100.00");
        strings.add("200.00");
        strings.add("300.00");
        strings.add("500.00");
        strings.add("1000.00");
        strings.add("2000.00");
        mAdapter = new PointAmountAdapter(strings);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.setCurrentPosition(position);
            }
        });
    }


    /**
     * 初始化界面控件的显示数据
     */
    @Override
    protected void initData() {
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
            if (onYesClickListener1 != null) {
                onYesClickListener1.onYesClick(mAdapter.getItem(mAdapter.getCurrentPosition()),etPointAmount.getText().toString());
            }
        });
        bindClickEvent(btn_cancel, () -> {
            dismiss();
            if (onNoClickListener != null) {
                onNoClickListener.onNoClick();
            }

        });
    }
    public void setYesOnclickListener1(String str, OnYesClickListener1 onYesClickListener) {
        if (!TextUtils.isEmpty(str)) {
            this.yesStr = str;
        }
        this.onYesClickListener1 = onYesClickListener;
    }
    public interface OnYesClickListener1 {
        void onYesClick(String amount,String inputAmount);
    }
}
