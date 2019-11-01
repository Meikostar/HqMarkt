package com.hqmy.market.view.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.hqmy.market.R;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.view.adapter.InputPwdAdapter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 输入交易密码对话框
 * Created by dahai on 2018/12/18.
 */
public class InputPwdDialog extends Dialog implements AdapterView.OnItemClickListener {
    private EditText et1, et2, et3, et4, et5, et6;
    private GridView gridView;
    private Button dialog_ok;
    private ImageView dialog_cancel;
    private InputPasswordListener sexSelectListener;
    private Context mContext;
    private int keyNum = 0;

    public InputPwdDialog(Context context, InputPasswordListener listener) {
        super(context, R.style.dialog_with_alpha);
//        setCanceledOnTouchOutside(false);//设置点击外部不可以取消;
        this.mContext = context;
        this.sexSelectListener = listener;
        setContentView(R.layout.dialog_input_password);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (display.getWidth());
        Window window = getWindow();
        window.setAttributes(p);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomAnimStyle);
        gridView = findViewById(R.id.gv_input_pwd_dialog);
        dialog_cancel = findViewById(R.id.iv_input_pwd_dialog_cancel);
        et1 = findViewById(R.id.tv_input_pwd_dialog_1);
        et2 = findViewById(R.id.tv_input_pwd_dialog_2);
        et3 = findViewById(R.id.tv_input_pwd_dialog_3);
        et4 = findViewById(R.id.tv_input_pwd_dialog_4);
        et5 = findViewById(R.id.tv_input_pwd_dialog_5);
        et6 = findViewById(R.id.tv_input_pwd_dialog_6);
        dialog_ok = findViewById(R.id.but_input_pwd_dialog_ok_icon);
    }

    private void initListener() {
        gridView.setOnItemClickListener(this);
        bindClickEvent(dialog_ok, () -> {
            if (keyNum < 6) {
                ToastUtil.showToast("请输入完整的密码");
            } else {
                if (null != this.sexSelectListener) {
                    String pwd1 = et1.getText().toString();
                    String pwd2 = et2.getText().toString();
                    String pwd3 = et3.getText().toString();
                    String pwd4 = et4.getText().toString();
                    String pwd5 = et5.getText().toString();
                    String pwd6 = et6.getText().toString();
                    String pwd = pwd1 + pwd2 + pwd3 + pwd4 + pwd5 + pwd6;
                    sexSelectListener.callbackPassword(pwd);
                }
                this.cancel();
            }
        });
        bindClickEvent(dialog_cancel, () -> {
            this.cancel();
        });
    }

    private void initData() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add(i);
        }
        InputPwdAdapter adapter = new InputPwdAdapter(mContext, list);
        gridView.setAdapter(adapter);
    }

    /**
     * 基本点击事件跳转默认节流1000毫秒
     *
     * @param view   绑定的view
     * @param action 跳转的Acticvity
     */
    protected void bindClickEvent(View view, final Action action) {
        bindClickEvent(view, action, 1000);
    }

    protected void bindClickEvent(View view, final Action action, long frequency) {
        Observable<Object> observable = RxView.clicks(view);
        if (frequency > 0) {
            observable.throttleFirst(frequency, TimeUnit.MILLISECONDS);
        }
        observable.subscribe(trigger -> action.run());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int inputNum = position + 1;
        if (inputNum == 11) {
            inputNum = 0;
        }
        if (inputNum != 10 && inputNum != 12 && keyNum < 6) {
            ++keyNum;
            switch (keyNum) {
                case 1:
                    et1.setText(inputNum + "");
//                    et1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et1.setTransformationMethod(new MyTransformationMethod());
                    break;
                case 2:
                    et2.setText(inputNum + "");
//                    et2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et2.setTransformationMethod(new MyTransformationMethod());
                    break;
                case 3:
                    et3.setText(inputNum + "");
//                    et3.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et3.setTransformationMethod(new MyTransformationMethod());
                    break;
                case 4:
                    et4.setText(inputNum + "");
//                    et4.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et4.setTransformationMethod(new MyTransformationMethod());
                    break;
                case 5:
                    et5.setText(inputNum + "");
//                    et5.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et5.setTransformationMethod(new MyTransformationMethod());
                    break;
                case 6:
                    et6.setText(inputNum + "");
//                    et6.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et6.setTransformationMethod(new MyTransformationMethod());
                    break;
            }
        }
        if (inputNum == 12 && inputNum != 10 && keyNum > 0) {
            switch (keyNum) {
                case 1:
                    et1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    break;
                case 2:
                    et2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    break;
                case 3:
                    et3.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    break;
                case 4:
                    et4.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    break;
                case 5:
                    et5.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    break;
                case 6:
                    et6.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    break;
            }
            --keyNum;
        }
    }

    public interface InputPasswordListener {
        void callbackPassword(String password);
    }


    private class MyTransformationMethod implements TransformationMethod {

        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        @Override
        public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

        }
    }

    /**
     * 将密码转换成*显示
     */
    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }

        public char charAt(int index) {
            return '*';
        }

        public int length() {
            return mSource.length();
        }

        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }

}
