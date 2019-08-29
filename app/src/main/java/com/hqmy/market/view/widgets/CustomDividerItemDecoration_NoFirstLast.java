package com.hqmy.market.view.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 给RecyclerView设置item的分割线(自定义分割线)
 * @author bowen
 *
 */
public class CustomDividerItemDecoration_NoFirstLast extends RecyclerView.ItemDecoration {

	public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

	public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

	private Drawable mDivider;

	private int mOrientation;
	private int mOffsetLeft =0;    //分割线偏离左边界
	private int mOffsetRight = 0;   //分割线偏离右边界


    public int getOffsetLeft() {
        return mOffsetLeft;
    }

    public void setOffsetLeft(int offsetLeft) {
        mOffsetLeft = offsetLeft;
    }

    public int getOffsetRight() {
        return mOffsetRight;
    }

    public void setOffsetRight(int offsetRight) {
        mOffsetRight = offsetRight;
    }

    public CustomDividerItemDecoration_NoFirstLast(Context context, int orientation, int resId) {
		mDivider = context.getResources().getDrawable(resId);
		setOrientation(orientation);
	}

	public void setOrientation(int orientation) {
		if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
			throw new IllegalArgumentException("invalid orientation");
		}
		mOrientation = orientation;
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent) {
		//Log.v("recyclerview - itemdecoration", "onDraw()");

		if (mOrientation == VERTICAL_LIST) {
			drawVertical(c, parent);
		} else {
			drawHorizontal(c, parent);
		}

	}

	public void drawVertical(Canvas c, RecyclerView parent) {
		/*final int left = parent.getPaddingLeft();
		final int right = parent.getWidth() - parent.getPaddingRight();

		final int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			RecyclerView v = new RecyclerView(parent.getContext());
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			final int top = child.getBottom() + params.bottomMargin;
			final int bottom = top + mDivider.getIntrinsicHeight();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
*/

		final int left = parent.getPaddingLeft();
		final int right = parent.getWidth() - parent.getPaddingRight();

		final int childCount = parent.getChildCount();
		for (int i = 1; i < childCount-1; i++) {
			final View child = parent.getChildAt(i);
			RecyclerView v = new RecyclerView(parent.getContext());
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			final int top = child.getBottom() + params.bottomMargin;
			final int bottom = top + mDivider.getIntrinsicHeight();

			if (mDivider != null) {
				mDivider.setBounds(left + mOffsetLeft, top, right - mOffsetRight, bottom);
				mDivider.draw(c);
			}
		}

		/*final int left = parent.getPaddingLeft();
		final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
		final int childSize = parent.getChildCount();
		for (int i = 0; i < childSize; i++) {
			final View child = parent.getChildAt(i);
			RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
			final int top = child.getBottom() + layoutParams.bottomMargin;
			final int bottom = top + mDivider.getIntrinsicHeight();
			if (mDivider != null) {
				mDivider.setBounds(left + mOffsetLeft, top, right - mOffsetRight, bottom);
				mDivider.draw(c);
			}
			if (mPaint != null) {
				canvas.drawRect(left + mOffsetLeft, top, right - mOffsetRight, bottom, mPaint);
			}
		}*/
	}

	public void drawHorizontal(Canvas c, RecyclerView parent) {
		final int top = parent.getPaddingTop();
		final int bottom = parent.getHeight() - parent.getPaddingBottom();

		final int childCount = parent.getChildCount();
		for (int i = 1; i < childCount-1; i++) {
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			final int left = child.getRight() + params.rightMargin;
			final int right = left + mDivider.getIntrinsicHeight();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}

	@Override
	public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
		if (mOrientation == VERTICAL_LIST) {
			outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
		} else {
			outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
		}
	}
}
