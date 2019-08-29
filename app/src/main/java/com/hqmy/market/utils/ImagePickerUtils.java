package com.hqmy.market.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.data.ImagePickerCropParams;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;

import java.util.List;


/**
 * 自定义图片选择裁剪工具
 * Created by Administrator on 2017/12/13.
 */

public class ImagePickerUtils {

    public static final int REQUEST_CODE = 111;
    private static final String TAG = "ImagePickerUtils";

    private volatile static ImagePickerUtils imagePickerUtils;
    private OnUploadOkListener onUploadOkListener;

    private byte[] firstBase64;
    private byte[] secondBase64;
    private byte[] thirdBase64;
    private byte[] bitmapByte;
    private String domain;
    private String uptoken;
    private String prefix;

    public static ImagePickerUtils getInstances() {
        if (imagePickerUtils == null) {
            imagePickerUtils = new ImagePickerUtils();
            initUpload();
        }
        return imagePickerUtils;
    }

    private static void initUpload() {
    }

    public byte[] onActivityResult(int requestCode, int resultCode, Intent data, final Activity activity) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            List<ImageBean> resultList = data.getParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
            String imagePath = "";
            for (ImageBean imageBean : resultList) {
                imagePath = imageBean.getImagePath();
            }
            LogUtil.i(TAG, imagePath);
            byte[] imageByte = SaveImageTools.getImageByte("", imagePath);
            if (imageByte != null) {
                return imageByte;
            }
        }
        return null;
    }

    /**
     * 应用于个人信息
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @param activity
     * @param imageView   显示本地图片
     */
    public void onActivityResultUserIcon(int requestCode, int resultCode, Intent data, final Activity activity, ImageView imageView) {

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            List<ImageBean> resultList = data.getParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
            String imagePath = "";
            for (ImageBean imageBean : resultList) {
                imagePath = imageBean.getImagePath();
            }

            LogUtil.i(TAG, imagePath);

            byte[] imageByte = SaveImageTools.getImageByte("", imagePath);

            if (imageByte == null) {
                ToastUtil.toast("图片不存在");
                return;
            }

            GlideUtils.getInstances().loadRoundCornerImgUnCache(activity, imageView, 4, imageByte);
            imageView.setVisibility(View.VISIBLE);

            //putImageMethod(imageByte, activity, null, null, null, 0);
            putImageToQiniu(imageByte, activity);
        }
    }

    /**
     * 应用于实名认证
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @param activity
     * @param iv_report
     * @param rl_loading_container
     * @param tv_show_modify
     * @param tv_show_again
     * @param selectItem
     */
    public void onActivityForResult(int requestCode, int resultCode, Intent data, final Activity activity,
                                    final ImageView iv_report, final RelativeLayout rl_loading_container,
                                    final TextView tv_show_modify, final RelativeLayout tv_show_again, final int selectItem) {

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            List<ImageBean> resultList = data.getParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
            String imagePath = "";
            for (ImageBean imageBean : resultList) {
                imagePath = imageBean.getImagePath();
            }
            LogUtil.i(TAG, imagePath);
            byte[] bitmapByte = SaveImageTools.getImageByte("", imagePath);

            if (bitmapByte == null) {
                ToastUtil.toast("图片不存在");
                return;
            }
            setHeadLoad(bitmapByte, activity, iv_report, rl_loading_container, tv_show_modify, tv_show_again, selectItem);
        }
    }

    /**
     * 设置图片内容
     *
     * @param bitmapByte
     * @param activity
     * @param iv_report
     * @param rl_loading_container
     * @param tv_show_modify
     * @param tv_show_again
     * @param selectItem
     */
    public void setHeadLoad(byte[] bitmapByte, Activity activity, ImageView iv_report, RelativeLayout rl_loading_container, TextView tv_show_modify,
                            RelativeLayout tv_show_again, int selectItem) {
        iv_report.setVisibility(View.VISIBLE);
        GlideUtils.getInstances().loadRoundCornerImgUnCache(activity, iv_report, 2.5f, bitmapByte);
        putImageMethod(bitmapByte, activity, rl_loading_container, tv_show_modify, tv_show_again, selectItem);
        if (selectItem == 1) {
            firstBase64 = bitmapByte;
        } else if (selectItem == 2) {
            secondBase64 = bitmapByte;
        } else if (selectItem == 3) {
            thirdBase64 = bitmapByte;
        }
    }

    /**
     * 上传失败重新上传
     *
     * @param activity
     * @param rl_loading_container
     * @param tv_show_modify
     * @param tv_show_again
     * @param selectItem
     */
    public void putAgainUpload(Activity activity, RelativeLayout rl_loading_container, TextView tv_show_modify,
                               RelativeLayout tv_show_again, int selectItem) {
        if (selectItem == 1) {
            if (firstBase64 != null)
                putImageMethod(firstBase64, activity, rl_loading_container, tv_show_modify, tv_show_again, selectItem);
        } else if (selectItem == 2) {
            if (secondBase64 != null)
                putImageMethod(secondBase64, activity, rl_loading_container, tv_show_modify, tv_show_again, selectItem);
        } else if (selectItem == 3) {
            if (thirdBase64 != null)
                putImageMethod(thirdBase64, activity, rl_loading_container, tv_show_modify, tv_show_again, selectItem);
        }
    }

    /**
     * @param activity      上下文
     * @param imagePickType ImagePickType.SINGLE单选图片/ImagePickType.ONLY_CAMERA相机
     * @param isShowCamera  是否需要在界面中显示相机入口
     * @param asXString     图片宽度比例9/1
     * @param asYString     图片高度比例5/1
     * @param opXString     图片宽度720/720
     * @param opYString     图片高度400/720
     */
    public void startSelectPhoto(Activity activity, ImagePickType imagePickType, boolean isShowCamera, int asXString, int asYString, int opXString, int opYString) {
        new ImagePicker()
                .pickType(imagePickType)//设置选取类型(拍照、单选、多选)
                .maxNum(1)//设置最大选择数量(拍照和单选都是1，修改后也无效)
                .needCamera(isShowCamera)//是否需要在界面中显示相机入口(类似微信)
                .cachePath(Constants.IMG_BASE_PATH)//自定义缓存路径
                .doCrop(getCropParams(asXString, asYString, opXString, opYString))//裁剪功能需要调用这个方法，多选模式下无效
                .displayer(new GlideImagePickerDisplayer())//自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                .start(activity, REQUEST_CODE);
    }

    private ImagePickerCropParams getCropParams(int asXString, int asYString, int opXString, int opYString) {

        return new ImagePickerCropParams(asXString, asYString, opXString, opYString);
    }

    public interface OnUploadOkListener {
        void singleImageId(int selectItem, String imageId);

        void putOnFail();
    }

    public void setOnUploadOkListener(OnUploadOkListener onUploadOkListener) {
        this.onUploadOkListener = onUploadOkListener;
    }

    private void putImageMethod(byte[] bitmapByte, Activity activity, final RelativeLayout rl_loading_container, final TextView tv_show_modify,
                                final RelativeLayout tv_show_again, final int selectItem) {
        String fileBase64 = Base64.encodeToString(bitmapByte, Base64.NO_WRAP);
        if (rl_loading_container != null)
            rl_loading_container.setVisibility(View.VISIBLE);
        if (tv_show_modify != null)
            tv_show_modify.setVisibility(View.GONE);
        if (tv_show_again != null)
            tv_show_again.setVisibility(View.GONE);

    }


    private void putImageToQiniu(byte[] bitmapByte, Activity activity) {
        this.bitmapByte = bitmapByte;
        if (TextUtils.isEmpty(domain) && TextUtils.isEmpty(uptoken)) {
        } else {
        }
    }

    // 初始化、执行上传
    private volatile boolean isCancelled = false;

    // 点击取消按钮，让UpCancellationSignal##isCancelled()方法返回true，以停止上传
    public void cancel() {
        isCancelled = true;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public Bitmap base64ToBitmap(String base64Data) {
        Bitmap bitmap = null;
        try {
            String base64DataStr[] = base64Data.split(",");
            byte[] bitmapArray;
            if (base64DataStr != null && base64DataStr.length > 1) {
                bitmapArray = Base64.decode(base64DataStr[1], Base64.DEFAULT);
            } else {
                bitmapArray = Base64.decode(base64Data, Base64.DEFAULT);
            }
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
