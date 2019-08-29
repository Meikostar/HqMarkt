package com.hqmy.market.view.widgets;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView 横向间距
 */
public class RecycleViewDividerHorizontal extends RecyclerView.ItemDecoration {
    //horizontal
    private int horizontalSpace;

    public RecycleViewDividerHorizontal(int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.right = horizontalSpace;
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = horizontalSpace;
        }
    }

}