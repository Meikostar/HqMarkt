package com.hqmy.market.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hqmy.market.R;

public class DialogHelper {
    /**
     * 取消收藏
     */
    public static void showCancelCollectDialog(Context context, OnDialogStateListener listener){
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.update_version_dialog, null);
        TextView title = view.findViewById(R.id.update_title_dialog);
        title.setText("移除收藏夹");

        TextView content = (TextView)view.findViewById(R.id.update_content_dialog);
        content.setText("是否移除收藏夹？"); //

        Button confirm = (Button)view.findViewById(R.id.btn_confirm_dialog);
        Button cancel = (Button)view.findViewById(R.id.btn_cancel_dialog);
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dialog.dismiss();
                if(listener!=null){
                    listener.onAffirm();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dialog.dismiss();
                if(listener!=null){
                    listener.onCancel();
                }
            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager wm = ((Activity)(context)).getWindowManager();
        Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()*0.8) ; // 宽度设置为屏幕的0.6
        dialogWindow.setAttributes(p);

        dialog.show();
    }

    public interface OnDialogStateListener{
        void onAffirm();
        void onCancel();
    }
}
