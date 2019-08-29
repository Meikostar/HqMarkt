package com.hqmy.market.view.widgets.autoview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {

    private OnScrollChangedListener onScrollChangedListener;
    private OnTouchEventListener onTouchEventListener;

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(t, oldt, l, oldl);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    public void setOnTouchEventListener(OnTouchEventListener onTouchEventListener) {
        this.onTouchEventListener = onTouchEventListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (onTouchEventListener != null) onTouchEventListener.onTouch(ev);
        return super.onTouchEvent(ev);
    }

    public abstract interface OnScrollChangedListener {
        public abstract void onScrollChanged(int top, int oldTop, int l, int oldl);
    }

    public abstract interface OnTouchEventListener {
        public abstract void onTouch(MotionEvent event);
    }
}
