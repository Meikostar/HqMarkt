package com.hqmy.market.view.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.hqmy.market.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 分享方式
 */
public class ShareModeDialog extends Dialog {
    public static final int SHARE_FRIEND = 1;
    public static final int SHARE_PYQ = 2;

    @BindView(R.id.tv_dialog_title)
    TextView tvDialogTitle;

    private String title;
    private DialogListener dialogListener;

    public ShareModeDialog(Context context, DialogListener dialogListener) {
        super(context, R.style.dialog_with_alpha);
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_mode);
        ButterKnife.bind(this);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) getContext().getResources().getDisplayMetrics().widthPixels; // 宽度
        dialogWindow.setAttributes(lp);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        if (!TextUtils.isEmpty(title)) {
            tvDialogTitle.setText(title);
        }
    }

    @OnClick(R.id.cancel)
    public void cancelOnclick() {
        dismiss();
    }

    @OnClick({R.id.btn_wechat, R.id.btn_pyq})
    public void sureOnclick(View view) {
        int position = 0;
        switch (view.getId()) {
            case R.id.btn_wechat:
                position = SHARE_FRIEND;
                break;
            case R.id.btn_pyq:
                position = SHARE_PYQ;
                break;
        }
        if (dialogListener != null) {
            dialogListener.sureItem(position);
        }
        dismiss();
    }

    public interface DialogListener {
        void sureItem(int position);
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
