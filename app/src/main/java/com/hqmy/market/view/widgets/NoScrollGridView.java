package com.hqmy.market.view.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义的GridView，重写onMeasure方法，用于镶嵌在listView的item中
 * Created by lmc123456 on 2/6/2017.
 */
public class NoScrollGridView extends GridView {
    public NoScrollGridView(Context context) {
        super(context.getApplicationContext());

    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context.getApplicationContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

    /**
     * 固定返回false：这样的话，此控件放在scrollView中就不会自动滑动到本控件的位置
     */
    @Override
    public boolean isFocused() {
        return false;
    }

    /**
     * 固定返回false：这样的话，此控件放在scrollView中就不会自动滑动到本控件的位置
     */
    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return false;
    }
}
