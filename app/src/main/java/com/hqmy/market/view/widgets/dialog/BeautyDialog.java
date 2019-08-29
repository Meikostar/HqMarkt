package com.hqmy.market.view.widgets.dialog;

import android.app.Activity;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.hqmy.market.R;

/**
 * 美白、磨皮
 * Created by Administrator on 2017/2/15.
 */

public class BeautyDialog extends BaseDialog {
    private SeekBar mopiLevel_seekBar;
    private SeekBar meibaiLevel_seekBar;
    private SeekBarChangeListener listener;


    public BeautyDialog(Activity context,SeekBarChangeListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_beauty;
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

        mopiLevel_seekBar = findViewById(R.id.mopiLevel_seekBar);
        meibaiLevel_seekBar = findViewById(R.id.meibaiLevel_seekBar);
        mopiLevel_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (listener != null){
                    listener.onMoPiSeekBarChange(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        meibaiLevel_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (listener != null){
                    listener.onSeekBarChange(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    /**
     * 初始化界面控件的显示数据
     */
    @Override
    protected void initData() {

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    @Override
    protected void initEvent() {
    }

   public interface SeekBarChangeListener{
       void onSeekBarChange(int progress);
       void onMoPiSeekBarChange(int progress);
   }
}
