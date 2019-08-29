package com.hqmy.market.view.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.hqmy.market.base.BaseApplication;

public class RecommendViewGroup extends ViewGroup {
    private final static String TAG = "MyViewGroup";

    private final static int VIEW_MARGIN_VERTICAL = dip2px(5);
    private final static int VIEW_MARGIN_HORIZONTAL = dip2px(10);
    private final static int VIEW_MARGIN_SPACE = dip2px(5);
    private int view_hight = 0;
    int row;

    public RecommendViewGroup(Context context) {
        super(context);
    }

    public RecommendViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int childCount = getChildCount();
        Log.d("childCount", childCount + "");
        int x = 0;
        int y = 0;
        int row = 0;

        for (int index = 0; index < childCount; index++) {
            final View child = getChildAt(index);
            if (child.getVisibility() != View.GONE) {
                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                // 此处增加onlayout中的换行判断，用于计算所需的高度
                int width = child.getMeasuredWidth() + VIEW_MARGIN_HORIZONTAL;
                int height = child.getMeasuredHeight();
                x += width;
                if ((x + VIEW_MARGIN_HORIZONTAL) > maxWidth) {
                    x = width;
//					if (row >= 1)
//						break;
                    row++;
                }
                if (row > 0) {
                    y = row * height + height;
                } else {
                    y = 2 * height;
                }
            }
        }

        // 设置容器所需的宽度和高度
        setMeasuredDimension(maxWidth, y + (row * VIEW_MARGIN_SPACE) + (row * VIEW_MARGIN_VERTICAL) + (row * VIEW_MARGIN_HORIZONTAL) + getPaddingBottom());

    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        Log.d(TAG, "changed = " + arg0 + " left = " + arg1 + " top = " + arg2
                + " right = " + arg3 + " botom = " + arg4);
        arg2 = 0;
        final int count = getChildCount();
        int row = 0;// which row lay you view relative to parent
        int lengthX = arg1; // right position of child relative to parent
        int lengthY = arg2; // bottom position of child relative to parent
        for (int i = 0; i < count; i++) {
            final View child = this.getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            lengthX += width + VIEW_MARGIN_HORIZONTAL;
            lengthY = row * (height + VIEW_MARGIN_VERTICAL)
                    + VIEW_MARGIN_VERTICAL + height + arg2;
            if (lengthX > arg3) {
                lengthX = width + VIEW_MARGIN_HORIZONTAL + arg1;
                row++;
                lengthY = row * (height + VIEW_MARGIN_VERTICAL)
                        + VIEW_MARGIN_VERTICAL + height + arg2;
            }
            child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
        }
        view_hight = lengthY;
        measure(0, view_hight);
    }

    public static int dip2px(float dpValue) {
        final float scale = BaseApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
