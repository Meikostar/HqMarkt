package com.hqmy.market.view.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lwkandroid.imagepicker.data.ImageDataModel;
import com.lwkandroid.imagepicker.ui.pager.adapter.ImagePagerAdapter;
import com.lwkandroid.imagepicker.utils.ImagePickerComUtils;
import com.lwkandroid.imagepicker.widget.photoview.OnPhotoTapListener;
import com.lwkandroid.imagepicker.widget.photoview.PhotoView;

import java.util.ArrayList;


public class ImagePreviewAdapter extends PagerAdapter {
    private int mScreenWidth;
    private int mScreenHeight;
    private ArrayList<String> mAllmageList = new ArrayList<>();
    private Activity                                mActivity;
    public ImagePagerAdapter.PhotoViewClickListener mListener;

    public ImagePreviewAdapter(Activity activity, ArrayList<String> images) {
        this.mActivity = activity;
        this.mAllmageList.addAll(images);

        mScreenWidth = ImagePickerComUtils.getScreenWidth(activity);
        mScreenHeight = ImagePickerComUtils.getScreenHeight(activity);
    }

    public void setData(ArrayList<String> images) {
        mAllmageList.clear();
        this.mAllmageList.addAll(images);
    }

    public void setPhotoViewClickListener(ImagePagerAdapter.PhotoViewClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(mActivity);
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        photoView.setEnabled(true);
//        String imagePath = Constants.WEB_IMG_URL_STORAGE + mAllmageList.get(position);
        ImageDataModel.getInstance().getDisplayer().display(mActivity, mAllmageList.get(position), photoView, mScreenWidth, mScreenHeight);
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                if (mListener != null)
                    mListener.OnPhotoTapListener(view, x, y);
            }
        });
        container.addView(photoView);
        return photoView;
    }

    @Override
    public int getCount() {
        return mAllmageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public interface PhotoViewClickListener {
        void OnPhotoTapListener(View view, float v, float v1);
    }
}