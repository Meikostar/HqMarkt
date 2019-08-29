package com.hqmy.market.common.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hqmy.market.base.BaseApplication;


public class ScreenSizeUtil {


    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                BaseApplication.getInstance().getResources().getDisplayMetrics());
    }

    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                BaseApplication.getInstance().getResources().getDisplayMetrics());
    }

    public static int px2dp(int px) {
        float scale = BaseApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int px2dp(float px) {
        float scale = BaseApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        heigth = dm.heightPixels;
        return dm.widthPixels;
    }

    /**
     * 计算ListView总高度
     *
     * @param listView
     * @return
     */
    public static int getListViewTotalHieght(ListView listView) {
        int totalHeight = 0;

        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        return totalHeight;
    }


}
