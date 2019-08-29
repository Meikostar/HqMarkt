package com.hqmy.market.view.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.hqmy.market.R;
import com.hqmy.market.common.utils.ZXingUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupQrcodeDialog extends Dialog {
    @BindView(R.id.img_qrcode)
    ImageView img_qrcode;
    //    @BindView(R.id.img_close)
//    ImageView img_close;
    Bitmap bitmap;
    private String inviteCode;

    public GroupQrcodeDialog(@NonNull Context context, String inviteCode) {
        super(context, R.style.dialog_with_alpha);
        this.inviteCode = inviteCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_group_qrcode_layout);
        ButterKnife.bind(this);
        bitmap = ZXingUtils.createQRImage(inviteCode, 250, 250);
        img_qrcode.setImageBitmap(bitmap);
    }

    @OnClick(R.id.img_close)
    public void onclick(View view) {
        dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
