package com.hqmy.market.view.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.view.adapter.ReturnReasonAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 退款原因
 */
public class ReturnReasonDialog extends Dialog {
    @BindView(R.id.recy_reason)
    RecyclerView recyclerView;
    @BindView(R.id.tv_dialog_title)
    TextView tvDialogTitle;
    ReturnReasonAdapter mAdapter;

    private Context mContext;
    private DialogListener dialogListener;
    List<String> mReasons = new ArrayList<>();
    public ReturnReasonDialog(Context context, DialogListener dialogListener,List<String> reasons) {
        super(context, R.style.transparent_style_dialog);
        this.dialogListener = dialogListener;
        this.mContext = context;
        this.mReasons.clear();
        this.mReasons.addAll(reasons);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reason_dialog_layout);
        ButterKnife.bind(this);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.bottomAnimStyle);//动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) getContext().getResources().getDisplayMetrics().widthPixels; // 宽度
        dialogWindow.setAttributes(lp);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        initAdapter();

    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ReturnReasonAdapter(mReasons);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                mAdapter.setSelPositon(position);
                dismiss();
                if (dialogListener != null) {
                    dialogListener.sureItem(position, mAdapter.getItem(position));
                }
            }
        });
    }
    public void setTvDialogTitle(String dialogTitleStr){
        tvDialogTitle.setText(dialogTitleStr);
    }
    @OnClick(R.id.btn_close)
    public void cancelOnclick() {
        dismiss();
    }


    public interface DialogListener {
        void sureItem(int position, String name);
    }
}
