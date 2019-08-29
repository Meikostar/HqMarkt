package com.hqmy.market.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.ExtDto;
import com.hqmy.market.bean.PublishInfo;
import com.hqmy.market.bean.UploadFilesDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.Compressor;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.view.adapter.ReasonUploadPicAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 编辑宝贝
 */
public class EditGoodsActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.rcly_pics)
    RecyclerView rclyPics;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_detail)
    EditText etDetail;
    @BindView(R.id.iv_is_new)
    ImageView ivIsNew;
    boolean isNew;
    PublishInfo publishInfo;
    String category_id;
    private ReasonUploadPicAdapter mAdapter;
    private List<String> areadUploadImg = new ArrayList<>();
    private int upPicPosition = 0;
    private List<String> allUploadImgs = new ArrayList<>();
    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.edit_goods_layout;
    }

    @Override
    public void initView() {
        tvTitleText.setText("编辑宝贝");
        initPicAdapter();
        publishInfo = (PublishInfo) getIntent().getSerializableExtra("publishInfo");
        if (publishInfo != null) {
            category_id = publishInfo.getCategory_id();
            etTitle.setText(publishInfo.getTitle());
            etPrice.setText(publishInfo.getScore());
            if (publishInfo.getExt() != null) {
                tvLocation.setText(publishInfo.getExt().getProvince() + " " + publishInfo.getExt().getCity() + " " + publishInfo.getExt().getDistrict());
            }
            if (publishInfo.getCategory() != null && publishInfo.getCategory().getData() != null) {
                tvType.setText(publishInfo.getCategory().getData().getTitle());
            }
            etDetail.setText(publishInfo.getContent());
            isNew = publishInfo.isIs_new();
            if (isNew) {
                ivIsNew.setImageResource(R.mipmap.icon_collection_check_sel);
            } else {
                ivIsNew.setImageResource(R.mipmap.icon_collection_check_default);
            }
            if (publishInfo.getImgs() != null && publishInfo.getImgs().size() > 0) {
                allUploadImgs.clear();
                allUploadImgs.addAll(publishInfo.getImgs());
                List<ImageBean> imageBeans = new ArrayList<>();
                for (String url : publishInfo.getImgs()) {
                    ImageBean imageBean = new ImageBean();
                    imageBean.setImagePath(Constants.WEB_IMG_URL_UPLOADS+url);
                    imageBean.setNoAddBaseUrlPath(url);
                    imageBeans.add(imageBean);
                }
                if (imageBeans.size()< 6){
                    ImageBean imageBean = new ImageBean();
                    imageBean.setImagePath(Constants.IMAGEITEM_DEFAULT_ADD);
                    imageBeans.add(imageBean);
                }
                mAdapter.setNewData(imageBeans);
            }
        }
    }

    @Override
    public void initData() {

    }

    private void initPicAdapter() {
        mAdapter = new ReasonUploadPicAdapter(null, 6, "return_upload_pic");
        mAdapter.refresh();
        rclyPics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        rclyPics.setHasFixedSize(true);
        rclyPics.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (Constants.IMAGEITEM_DEFAULT_ADD.equals(mAdapter.getItem(position).getImagePath())) {
                    //打开选择
                    new RxPermissions(EditGoodsActivity.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {
                                    if (!aBoolean) {
                                        return;
                                    }
                                    // 用户已经同意该权限
                                    new ImagePicker()
                                            .pickType(ImagePickType.MULTI)//设置选取类型(拍照、单选、多选)
                                            .maxNum(7 - mAdapter.getItemCount())//设置最大选择数量(拍照和单选都是1，修改后也无效)
                                            .needCamera(true)//是否需要在界面中显示相机入口(类似微信)
                                            .displayer(new GlideImagePickerDisplayer())//自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                                            .start(EditGoodsActivity.this, Constants.INTENT_REQUESTCODE_VERIFIED_IMG1);
                                }
                            });
                }
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_del:
                        if (allUploadImgs.contains(mAdapter.getItem(position).getNoAddBaseUrlPath())){
                            allUploadImgs.remove(mAdapter.getItem(position).getNoAddBaseUrlPath());
                        }
                        mAdapter.remove(position);
                        mAdapter.refresh();
                        break;
                }
            }
        });
    }

    private void initUploadData() {
        showLoadDialog();
        upPicPosition = 0;
        areadUploadImg.clear();
        dealUpload();
    }

    private void dealUpload() {
        List<ImageBean> imageBeanList = mAdapter.getData();
        if (imageBeanList != null && upPicPosition < imageBeanList.size()) {
            String imgPath = imageBeanList.get(upPicPosition).getImagePath();
            upPicPosition = upPicPosition + 1;
            if (!Constants.IMAGEITEM_DEFAULT_ADD.equals(imgPath)) {
                uploadImg(imgPath);
            } else {//添加图片
                dealUpload();
            }

        } else {
            upLoadFinish();
        }

    }

    private void upLoadFinish() {
        editProducts();
    }

    private void uploadImg(String imgPath) {
        File file = new File(imgPath);
        File compressedImage = null;
        if (file.exists()) {
            //压缩文件
            try {
                compressedImage = new Compressor(this).compressToFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (compressedImage == null) {
                compressedImage = file;
            }

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            DataManager.getInstance().uploadFiles(new DefaultSingleObserver<UploadFilesDto>() {
                @Override
                public void onSuccess(UploadFilesDto object) {
                    if (object != null) {
                        Log.d("onSuccess() = ", "UploadFilesDto==" + object.getName());
                        areadUploadImg.add(object.getPath());
                        if (!allUploadImgs.contains(object.getPath())){
                            allUploadImgs.add(object.getPath());
                        }
                    }
                    dealUpload();
                }

                @Override
                public void onError(Throwable throwable) {
                    dissLoadDialog();
                    ToastUtil.toast(ApiException.getInstance().getErrorMsg());

                }
            }, "image", part);
        } else {
            dealUpload();
        }
    }

    private void editProducts() {
        if (publishInfo == null || TextUtils.isEmpty(publishInfo.getId())) {
            ToastUtil.showToast("id不能为空");
            return;
        }
        category_id = publishInfo.getCategory_id();
        if ("家用电器".equals(tvType.getText().toString())) {
            category_id = "8";
        } else if ("图片音像".equals(tvType.getText().toString())) {
            category_id = "9";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("category_id",category_id);
        map.put("content", etDetail.getText().toString());
        map.put("is_new", isNew);
        map.put("score", etPrice.getText().toString());
        map.put("title", etTitle.getText().toString());
        if (!TextUtils.isEmpty(tvLocation.getText().toString())) {
            String[] allNameStr = tvLocation.getText().toString().trim().split(" ");
            ExtDto extDto = new ExtDto();
            if (allNameStr.length > 0) {
                extDto.setProvince(allNameStr[0]);
            }
            if (allNameStr.length > 1) {
                extDto.setCity(allNameStr[1]);
            }
            if (allNameStr.length > 2) {
                extDto.setDistrict(allNameStr[2]);
            }
            map.put("ext", extDto);
        }
        if (allUploadImgs.size() > 0) {
            map.put("imgs", allUploadImgs);
        }
        showLoadDialog();
        DataManager.getInstance().editProducts(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                dissLoadDialog();
                ToastUtil.showToast("发布成功");
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("发布成功");
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    ToastUtil.showToast(ApiException.getInstance().getErrorMsg());
                }
            }
        }, publishInfo.getId(), map);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_edit, R.id.rl_type, R.id.rl_location, R.id.rl_is_new})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_edit:
                initUploadData();
                break;
            case R.id.rl_type:
                //类型选择
                gotoActivity(GoodsTypeActivity.class, false, null, 1);
                break;
            case R.id.rl_location:
                gotoActivity(AreaListActivity.class, false, null, Constants.INTENT_REQUESTCODE_AREA);
                break;
            case R.id.rl_is_new:
                isNew = !isNew;
                if (isNew) {
                    ivIsNew.setImageResource(R.mipmap.icon_collection_check_sel);
                } else {
                    ivIsNew.setImageResource(R.mipmap.icon_collection_check_default);
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    tvType.setText(data.getStringExtra("typeName"));
                    break;
                case Constants.INTENT_REQUESTCODE_AREA:
                    String allName = data.getStringExtra("areaName");
                    tvLocation.setText(allName);
                    break;
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG1 && data != null) {
            List<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null && resultList.size() > 0) {
                mAdapter.addData(0, resultList);
                mAdapter.refresh();
            }
        }
    }
}
