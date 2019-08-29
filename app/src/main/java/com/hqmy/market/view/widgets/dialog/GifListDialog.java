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
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.bean.GiftBean;
import com.hqmy.market.view.adapter.GifListAdapter;

import java.util.List;

/**
 * 打赏礼物Dialog
 * Created by Administrator on 2017/2/15.
 */

public class GifListDialog extends BaseDialog {
    private RecyclerView recyclerView;
    private TextView tvPayAmount;
    private GifListAdapter mAdapter;
    private OnYesClickListener1 onYesClickListener1;
    private List<GiftBean> giftBeanList;

    public GifListDialog(Activity context, List<GiftBean> giftBeanList) {
        super(context);
        this.giftBeanList = giftBeanList;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_gif_list;
    }

    /**
     * 初始化界面控件
     */
    @Override
    protected void initView() {
        //按空白处不能取消动画
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (display.getWidth());
        Window window = getWindow();
        window.setAttributes(p);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomAnimStyle);

        btn_sure = (Button) findViewById(R.id.yes);
        tvPayAmount = findViewById(R.id.tv_pay_amount);
        recyclerView = findViewById(R.id.recyclerView);
        if (giftBeanList != null && giftBeanList.size() > 0) {
            tvPayAmount.setText(giftBeanList.get(0).getPrice());
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mAdapter = new GifListAdapter(giftBeanList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.setCurrentPosition(position);
                tvPayAmount.setText(mAdapter.getItem(position).getPrice());
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
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    @Override
    protected void initEvent() {
        bindClickEvent(btn_sure, () -> {
            if (onYesClickListener1 != null && mAdapter.getItemCount() > 0) {
                onYesClickListener1.onYesClick(mAdapter.getItem(mAdapter.getCurrentPosition()).getId());
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
        void onYesClick(String amount);
    }
}
