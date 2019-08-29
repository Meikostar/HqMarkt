package com.hqmy.market.view.mainfragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lwkandroid.imagepicker.data.ImageContants;
import com.lwkandroid.imagepicker.ui.pager.adapter.ImagePagerAdapter;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.view.adapter.ImagePreviewAdapter;
import java.util.ArrayList;
import butterknife.BindView;

public class ImagePreviewActivity extends BaseActivity {
    @BindView(R.id.vp_image_pager)
    ViewPager mViewPager;
    @BindView(R.id.tv_page)
    TextView  tvPage;
    private ImagePreviewAdapter mAdapter;
    private ArrayList<String>   mDataList;
    private int  mCurPosition;

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_image_preview_layout;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        mDataList = bundle.getStringArrayList(ImageContants.INTENT_KEY_DATA);
        mCurPosition = bundle.getInt(ImageContants.INTENT_KEY_START_POSITION, 0);
        mAdapter = new ImagePreviewAdapter(this, mDataList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mViewPager.setCurrentItem(mCurPosition, false);
        mAdapter.setPhotoViewClickListener(new ImagePagerAdapter.PhotoViewClickListener() {
            @Override
            public void OnPhotoTapListener(View view, float v, float v1) {
                finish();
            }
        });
        mViewPager.setCurrentItem(mCurPosition);
        updateActionbarTitle();
    }

    private ViewPager.SimpleOnPageChangeListener mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            mCurPosition = position;
            if (mDataList != null && position < mDataList.size()) {
                updateActionbarTitle();
            }
        }
    };

    //更新Title
    private void updateActionbarTitle() {
        if (tvPage != null) {
            tvPage.setText(getResources().getString(R.string.imagepicker_pager_title_count
                    , String.valueOf(mCurPosition + 1), String.valueOf(mDataList.size())));
        }
    }
}
