package com.hqmy.market.view.widgets;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView
 */
public class RecycleViewDivider_PovertyRelief extends RecyclerView.ItemDecoration {
    //horizontal
    private int horizontalSpace;
    private int verticalSpace;

    public RecycleViewDivider_PovertyRelief(int horizontalSpace, int verticalSpace) {
        this.horizontalSpace = horizontalSpace;
        this.verticalSpace = verticalSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = horizontalSpace;
        outRect.bottom = verticalSpace;
        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.left = horizontalSpace;
        }
        if (parent.getChildLayoutPosition(view) < 2) {
            outRect.top = verticalSpace;
        }
    }

}