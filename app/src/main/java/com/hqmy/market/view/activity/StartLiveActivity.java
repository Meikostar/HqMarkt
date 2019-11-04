package com.hqmy.market.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.base.BaseApplication;
import com.hqmy.market.bean.LiveCatesBean;
import com.hqmy.market.bean.LiveVideoInfo;
import com.hqmy.market.bean.UploadFilesDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.Compressor;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.eventbus.LiveEvent;
import com.hqmy.market.eventbus.LogoutEvent;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.qiniu.AVStreamingActivity;
import com.hqmy.market.qiniu.StreamingBaseActivity;
import com.hqmy.market.view.mainfragment.learn.StartLiveCategoryAdapter;
import com.hqmy.market.view.widgets.NoScrollGridView;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 开播(准备直播)
 */
public class StartLiveActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView  mTitleText;
    @BindView(R.id.tv_title_right)
    TextView  mRightText;
    @BindView(R.id.img_add)
    ImageView img_add;
    @BindView(R.id.et_des)
    EditText  et_des;
    /**
     * 直播类别
     */
    @BindView(R.id.gv_start_live_category)
    GridView  mLiveCategory;

    StartLiveCategoryAdapter startLiveCategoryAdapter;
    UploadFilesDto           uploadFilesDto;
    @BindView(R.id.iv_title_back)
    ImageView        ivTitleBack;
    @BindView(R.id.layout_top)
    RelativeLayout   layoutTop;
    @BindView(R.id.tv_contury)
    TextView         tvContury;
    @BindView(R.id.rl_contury)
    RelativeLayout   rlContury;
    @BindView(R.id.tv_brand)
    TextView         tvBrand;
    @BindView(R.id.rl_brand)
    RelativeLayout   rlBrand;
    @BindView(R.id.tv_categry)
    TextView         tvCategry;
    @BindView(R.id.rl_categry)
    RelativeLayout   rlCategry;
    @BindView(R.id.btn_open_live)
    TextView         btnOpenLive;

    @Override
    public int getLayoutId() {
        return R.layout.activity_start_live;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mTitleText.setText("开播");
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText("主播资料");

    }
    private String conturyId;
    private String brandId;
    private String categryId;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LiveEvent event) {
        if(event.type==1){
            tvContury.setText(event.name);
            conturyId=event.id;
        }else  if(event.type==2){
            tvBrand.setText(event.name);
            brandId=event.id;
        }else  if(event.type==3){
            tvCategry.setText(event.name);
            categryId=event.id;
        }
    }

    @Override
    public void initData() {
        startLiveCategoryAdapter = new StartLiveCategoryAdapter(this, null);
        mLiveCategory.setAdapter(startLiveCategoryAdapter);
        getLiveCates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

            EventBus.getDefault().unregister(this);

    }

    @Override
    public void initListener() {
        rlContury.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartLiveActivity.this, SelectConturyActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);
            }
        });
        rlBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartLiveActivity.this, SelectConturyActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });
        rlCategry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartLiveActivity.this, SelectConturyActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.iv_title_back, R.id.btn_open_live,
            R.id.tv_title_right, R.id.img_add
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                gotoActivity(AnchorInfoActivity.class);
                break;
            case R.id.btn_open_live:
                if (conturyId==null) {
                    ToastUtil.showToast("请选择国家");
                    return;
                }
                if (categryId==null) {
                    ToastUtil.showToast("请选择直播课程分类");
                    return;
                }
                if (brandId==null) {
                    ToastUtil.showToast("请选择直播品牌");
                    return;
                }
                if (TextUtils.isEmpty(et_des.getText().toString().trim())) {
                    ToastUtil.showToast("请输入直播标题");
                    return;
                }
                if (uploadFilesDto == null || TextUtils.isEmpty(uploadFilesDto.getPath())) {
                    ToastUtil.showToast("请添加封面图片");
                    return;
                }
                HashMap<String, String> map = new HashMap<>();
                map.put("cate_id", categryId);
                map.put("mall_brand_id", brandId);
                map.put("new_category_id", conturyId);
                map.put("title", et_des.getText().toString().trim());
                map.put("images", uploadFilesDto.getPath());
                map.put("include", "room,apply");
                openLive(map);
                break;
            case R.id.img_add:
                new RxPermissions(this).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted && Manifest.permission.CAMERA.equals(permission.name)) {
                                    // 用户已经同意该权限
                                    new ImagePicker()
                                            .pickType(ImagePickType.SINGLE)//设置选取类型(拍照、单选、多选)
                                            .maxNum(1)//设置最大选择数量(拍照和单选都是1，修改后也无效)
                                            .needCamera(true)//是否需要在界面中显示相机入口(类似微信)
                                            .displayer(new GlideImagePickerDisplayer())//自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                                            //                                             .doCrop(1, 1, 300, 300)
                                            .start(StartLiveActivity.this, Constants.INTENT_REQUESTCODE_VERIFIED_IMG1);
                                }
                            }
                        });
                break;
        }

    }

    public void getLiveCates() {
        showLoadDialog();
        DataManager.getInstance().getLiveCates(new DefaultSingleObserver<HttpResult<List<LiveCatesBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<LiveCatesBean>> result) {
                dissLoadDialog();
                startLiveCategoryAdapter.setList(result.getData());
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, 0);
    }

    private void uploadImg(String imgPath) {
        File file = new File(imgPath);
        File compressedImage = null;
        if (file.exists()) {
            //压缩文件
            try {
                compressedImage = new Compressor(BaseApplication.getInstance()).compressToFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (compressedImage == null) {
                compressedImage = file;
            }
        } else {
            ToastUtil.showToast("上传封面图片失败");
            return;
        }
        showLoadDialog();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        DataManager.getInstance().uploadFiles(new DefaultSingleObserver<UploadFilesDto>() {
            @Override
            public void onSuccess(UploadFilesDto object) {
                dissLoadDialog();
                if (object != null) {
                    uploadFilesDto = object;
                    GlideUtils.getInstances().loadNormalImg(StartLiveActivity.this, img_add, imgPath);
                } else {
                    ToastUtil.showToast("上传封面图片失败");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                //                ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, "live", part);
    }

    public void openLive(HashMap<String, String> map) {
        showLoadDialog();
        DataManager.getInstance().openLive(new DefaultSingleObserver<HttpResult<LiveVideoInfo>>() {
            @Override
            public void onSuccess(HttpResult<LiveVideoInfo> result) {
                dissLoadDialog();
                if (result != null) {
                    Intent intent = new Intent(StartLiveActivity.this, AVStreamingActivity.class);
                    intent.putExtra(StreamingBaseActivity.PUBLIC_URL, result.getData().apply.getData().rtmp_publish_url);
                    intent.putExtra("id", result.getData().getId());
                    if (result.getData() != null && result.getData().getRoom() != null && result.getData().getRoom().getData() != null) {
                        intent.putExtra("room", result.getData().getRoom().getData());
                    }
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG1 && data != null) {
            List<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null && resultList.size() > 0) {
                String imageUrl = resultList.get(0).getImagePath();
                uploadImg(imageUrl);
            }
        }
    }


}
