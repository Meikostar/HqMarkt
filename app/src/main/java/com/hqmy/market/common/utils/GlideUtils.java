package com.hqmy.market.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.common.Constants;
import com.hqmy.market.utils.ScreenUtils;
import com.hqmy.market.view.widgets.CornerTransform;
import com.hqmy.market.view.widgets.autoview.CircleTransform;
import com.hqmy.market.view.widgets.autoview.RoundCornerTransform;
import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * Created by Administrator on 2017/12/21.
 */

public class GlideUtils {

    private static GlideUtils glideUtils;

    public static GlideUtils getInstances() {
        if (glideUtils == null) {
            glideUtils = new GlideUtils();
        }
        return glideUtils;
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param imageView
     * @param cornerDp
     * @param imgUrl
     */
    public void loadRoundCornerImg(Context context, ImageView imageView, float cornerDp, Object imgUrl) {
        Object content;
        if(imgUrl!=null){
            if(imgUrl instanceof String){
                if(((String) imgUrl).contains("http")){
                    content= imgUrl;
                }else{
                    content=Constants.WEB_IMG_URL_UPLOADS+(String)imgUrl;
                }
            }else {
                content= imgUrl;
            }
        }else {
            content= imgUrl;
        }
        Glide.with(context)
                .asBitmap()
                .load(content)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.img_default_2) // 预加载图片
                        .error(R.mipmap.img_default_2) // 加载失败显示图片
                        .priority(Priority.HIGH) // 优先级
                        // .diskCacheStrategy(DiskCacheStrategy.NONE) // 缓存策略
                        .transforms(new CenterCrop(), new RoundCornerTransform(ScreenSizeUtil.dp2px(cornerDp)))) // 转化为圆角
                .into(imageView);
    }
    /**
     * 加载圆角图片
     *
     * @param context
     * @param imageView
     * @param cornerDp
     * @param imgUrl
     */
    public void loadRoundCornerImg(Context context, ImageView imageView, float cornerDp, Object imgUrl,int moren) {
        Object content;
        if(imgUrl!=null){
            if(imgUrl instanceof String){
                if(((String) imgUrl).contains("http")){
                    content= imgUrl;
                }else{
                    content=Constants.WEB_IMG_URL_UPLOADS+(String)imgUrl;
                }
            }else {
                content= imgUrl;
            }
        }else {
            content= imgUrl;
        }
        Glide.with(context)
                .asBitmap()
                .load(content)
                .apply(new RequestOptions()
                        .placeholder(moren) // 预加载图片
                        .error(moren) // 加载失败显示图片
                        .priority(Priority.HIGH) // 优先级
                        // .diskCacheStrategy(DiskCacheStrategy.NONE) // 缓存策略
                        .transforms(new CenterCrop(), new RoundCornerTransform(ScreenSizeUtil.dp2px(cornerDp)))) // 转化为圆角
                .into(imageView);
    }
    /**
     * 加载圆形图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadRoundImg(Context context, ImageView imageView, Object imgUrl) {
        Object content;
        if(imgUrl!=null){
            if(imgUrl instanceof String){
                if(((String) imgUrl).contains("http")||((String) imgUrl).contains("storage/emulated")){
                    content= imgUrl;
                }else{
                    content=Constants.WEB_IMG_URL_UPLOADS+(String)imgUrl;
                }
            }else {
                content= imgUrl;
            }
        }else {
            content= imgUrl;
        }
        Glide.with(context)
                .load(content).error(R.mipmap.img_default_1).placeholder(R.mipmap.img_default_1).transform( new CircleTransform())
                .centerCrop()
                .into(imageView);
//        Glide.with(context)
//                .asBitmap()
//                .load(content)
//                .apply(new RequestOptions()
//                        .placeholder(R.mipmap.img_default_1)
//                        .error(R.mipmap.img_default_1)
//                        // .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .transforms(new CenterCrop(), new CircleTransform()))
//                .into(imageView);
    }
    /**
     * 加载圆形图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadRoundImg(Context context, ImageView imageView, String imgUrl,int img_default) {

        Glide.with(context)
                .asBitmap()
                .load(TextUtils.isEmpty(imgUrl)?imgUrl:imgUrl.contains("http")?imgUrl:Constants.WEB_IMG_URL_UPLOADS+imgUrl)
                .apply(new RequestOptions()
                        .placeholder(img_default)
                        .error(img_default)
                        // .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .transforms(new CenterCrop(), new CircleTransform()))
                .into(imageView);
//        Glide.with(context)
//                .load(TextUtils.isEmpty(imgUrl)?imgUrl:imgUrl.contains("http")?imgUrl:Constants.WEB_IMG_URL_UPLOADS+imgUrl).error(img_default).placeholder(img_default).transform( new CircleTransform())
//                .centerCrop()
//                .into(imageView);
    }
    /**
     * 加载用户圆形图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadUserRoundImg(Context context, ImageView imageView, Object imgUrl) {
        Object content;
        if(imgUrl!=null){
            if(imgUrl instanceof String){
                if(((String) imgUrl).contains("http")){
                    content= imgUrl;
                }else{
                    content=Constants.WEB_IMG_URL_UPLOADS+(String)imgUrl;
                }
            }else {
                content= imgUrl;
            }
        }else {
            content= imgUrl;
        }
//        Glide.with(context)
//                .asBitmap()
//                .load(content)
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.moren_ren)
//                        .error(R.drawable.moren_ren)
////                         .diskCacheStrategy(DiskCacheStrategy.NONE)// 缓存策略
//                        .transforms(new CenterCrop(), new CircleTransform()))
//                .into(imageView);

        Glide.with(context)
                .load(content).error(R.drawable.moren_ren).placeholder(R.drawable.moren_ren).transform( new CircleTransform())
                .centerCrop()
                .into(imageView);
    }


    /**
     * 加载正常图片居中填充控件
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadNormalCenterCropImg(Context context, ImageView imageView, Object imgUrl) {
        Glide.with(context).asBitmap().load(imgUrl).apply(new RequestOptions()
                .placeholder(R.mipmap.img_default_1) // 预加载图片
                .error(R.mipmap.img_default_1) // 加载失败显示图片
                .priority(Priority.HIGH) // 优先级
                // .diskCacheStrategy(DiskCacheStrategy.NONE) // 缓存策略
                .transforms(new CenterCrop()))
                .into(imageView);
    }

    /**
     * 加载正常图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadProcuctNormalImg(Context context, ImageView imageView, Object imgUrl) {
        Object content;
        if(imgUrl!=null){
            if(imgUrl instanceof String){
                if(((String) imgUrl).contains("http")){
                    content= imgUrl;
                }else{
                    content=Constants.WEB_IMG_URL_UPLOADS+(String)imgUrl;
                }
            }else {
                content= imgUrl;
            }
        }else {
            content= imgUrl;
        }
        Glide.with(context)
                .load(content).error(R.drawable.moren_product).placeholder(R.drawable.moren_product)
                .centerCrop()
                .into(imageView);
//        try {
//            Glide.with(context).asBitmap().load(content).apply(new RequestOptions()
//                    .placeholder(R.drawable.moren_product) // 预加载图片
//                    .error(R.drawable.moren_product) // 加载失败显示图片
//                    .priority(Priority.LOW)) // 优先级
//                    .into(imageView);
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("loadNormalImg", "loadNormalImg--isDestroyed");
//        }

    }

    /**
     * 加载正常图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadNormalImg(Context context, ImageView imageView, Object imgUrl) {
        Object content;
         if(imgUrl!=null){
             if(imgUrl instanceof String){
                 if(((String) imgUrl).contains("http")||((String) imgUrl).contains("storage/emulated")){
                     content= imgUrl;
                 }else{
                     content=Constants.WEB_IMG_URL_UPLOADS+(String)imgUrl;
                 }
             }else {
                 content= imgUrl;
             }
         }else {
             content= imgUrl;
         }

        Glide.with(context)
                .load(content).error(R.drawable.img_default_three).placeholder(R.drawable.img_default_three)
                .centerCrop()
                .into(imageView);
//        try {
//            Glide.with(context).asBitmap().load(content).apply(new RequestOptions()
//                    .placeholder(R.mipmap.img_default_three) // 预加载图片
//                    .error(R.mipmap.img_default_three) // 加载失败显示图片
//                    .priority(Priority.LOW)) // 优先级
//                    .into(imageView);
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("loadNormalImg", "loadNormalImg--isDestroyed");
//        }

    }

    /**
     * 加载正常图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadNormalImg(Context context, ImageView imageView, Object imgUrl, int defaultImgId) {
        Object content;
        if(imgUrl!=null){
            if(imgUrl instanceof String){
                if(((String) imgUrl).contains("http")){
                    content= imgUrl;
                }else{
                    content=Constants.WEB_IMG_URL_UPLOADS+(String)imgUrl;
                }
            }else {
                content= imgUrl;
            }
        }else {
            content= imgUrl;
        }

        Glide.with(context)
                .load(content).error(defaultImgId).placeholder(defaultImgId)
                .centerCrop()
                .into(imageView);
//        try {
//            Glide.with(context).asBitmap().load(content).apply(new RequestOptions()
//                    .placeholder(defaultImgId) // 预加载图片
//                    .error(defaultImgId) // 加载失败显示图片
//                    .priority(Priority.LOW)) // 优先级
//                    .into(imageView);
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.d("loadNormalImg", "loadNormalImg--isDestroyed");
//        }

    }
    /**
     * 加载正常图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadNormalPathImg(Context context, ImageView imageView, Object imgUrl, int defaultImgId) {

        try {
            Glide.with(context).asBitmap().load(imgUrl).apply(new RequestOptions()
                    .placeholder(defaultImgId) // 预加载图片
                    .error(defaultImgId) // 加载失败显示图片
                    .priority(Priority.LOW)) // 优先级
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("loadNormalImg", "loadNormalImg--isDestroyed");
        }

    }
    /**
     * 加载圆角图片无缓存
     *
     * @param context
     * @param imageView
     * @param cornerDp
     * @param imgUrl
     */
    public void loadRoundCornerImgUnCache(Context context, ImageView imageView, float cornerDp, Object imgUrl) {
        Glide.with(context)
                .asBitmap()
                .load(imgUrl)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.img_default_1) // 预加载图片
                        .error(R.mipmap.img_default_1) // 加载失败显示图片
                        .priority(Priority.HIGH) // 优先级
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // 缓存策略
                        .transforms(new CenterCrop(), new RoundCornerTransform(ScreenSizeUtil.dp2px(cornerDp)))) // 转化为圆角
                .into(imageView);
    }


    public void loadResizeImage(Context context, ImageView imageView, Object imgUrl, BaseActivity mBaseActivity) {
        Glide.with(context)
                .asBitmap()
                .load(imgUrl)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.img_default_three) // 预加载图片
                        .error(R.mipmap.img_default_three) // 加载失败显示图片
                        .priority(Priority.LOW) // 优先级
                        .diskCacheStrategy(DiskCacheStrategy.NONE) ) // 缓存策略
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                        //屏幕宽度
                        float width = ScreenUtils.getScreenW(mBaseActivity);
                        //缩放比例
                        float scale = width / resource.getWidth();
                        //缩放后的宽度和高度
                        int afterWidth = (int) (resource.getWidth() * scale);
                        int afterHeight = (int) (resource.getHeight() * scale);
                        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                        lp.width = afterWidth;
                        lp.height = afterHeight;
                        imageView.setLayoutParams(lp);
                    }
                });
    }

    /**
     * 重新加载图片
     *
     * @param context
     */
    public void onResumeLoadImg(Context context) {
        if (context != null) {
            Glide.with(context).resumeRequests();
        }
    }

    /**
     * 暂停加载图片
     *
     * @param context
     */
    public void onPauseLoad(Context context) {
        if (context != null) {
            Glide.with(context).pauseRequests();
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
