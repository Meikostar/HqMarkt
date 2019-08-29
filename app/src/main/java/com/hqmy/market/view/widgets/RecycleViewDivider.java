package com.hqmy.market.view.widgets;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecycleViewDivider extends RecyclerView.ItemDecoration {
    //horizontal
    private int horizontalSpace;
    private int verticalSpace;

    public RecycleViewDivider(int horizontalSpace, int verticalSpace) {
        this.horizontalSpace = horizontalSpace;
        this.verticalSpace = verticalSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildLayoutPosition(view) >0){
            //包含一个headview
            outRect.left = horizontalSpace;
            outRect.bottom = verticalSpace;
            if (parent.getChildLayoutPosition(view) %2 == 0) {
                outRect.right = horizontalSpace;
            }
        }
    }
}