package com.hqmy.market.view.mainfragment.consume;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.base.BaseApplication;
import com.hqmy.market.bean.CategoryListdto;
import com.hqmy.market.bean.UploadFilesDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.Compressor;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.UploadPicAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 发布宝贝
 * Created by rzb on 2019/5/13.
 */
public class BabyPublishActivity extends BaseActivity {
    public static final String PRODUCT_TYPE = "product_type";
    @BindView(R.id.layout_top)
    RelativeLayout layout_top;
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title_text)
    TextView  mTitleText;
    @BindView(R.id.tv_title_right)
    TextView  mTitleRight;
    @BindView(R.id.tv_product_type)
    TextView  tv_product_type;
    @BindView(R.id.recy_view)
    RecyclerView recyPic;
    @BindView(R.id.et_product_price)
    EditText et_product_price;
    @BindView(R.id.et_product_title)
    EditText et_product_title;
    @BindView(R.id.ed_content)
    EditText  ed_content;
    @BindView(R.id.layout_product_type)
    RelativeLayout layout_product_type;
    @BindView(R.id.tv_publish)
    TextView tv_publish;

    private int upPicPosition;
    private UploadPicAdapter mAdapter;
    private List<String> imgArr = new ArrayList<>();
    private List<ImageBean> list = new ArrayList<>();
    private CategoryListdto categoryListdto;

    @Override
    public int getLayoutId() {
        return R.layout.activity_baby_publish;
    }

    @Override
    public void initView() {
        layout_top.setBackgroundColor(getResources().getColor(R.color.my_color_white));
        mTitleText.setText("发布宝贝");
        Bundle bundle = getIntent().getExtras();
        initAdapter(list);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(layout_product_type, () -> {
            gotoActivity(ProductTypeActivity.class, false, null, ProductTypeActivity.CHOICE_PRODUCT_TYPE);
        });

        bindClickEvent(tv_publish, () -> {
            if(mAdapter.getData().size() == 0){
                ToastUtil.showToast("请添加宝贝图片...");
                return;
            }
            if(TextUtils.isEmpty(tv_product_type.getText().toString())){
                ToastUtil.showToast("请选择宝贝类型...");
                return;
            }
            if(TextUtils.isEmpty(et_product_price.getText().toString())){
                ToastUtil.showToast("请输入宝贝价格...");
                return;
            }

            if(TextUtils.isEmpty(et_product_title.getText().toString())){
                ToastUtil.showToast("请输入宝贝标题...");
                return;
            }

            if (TextUtils.isEmpty(ed_content.getText().toString().trim())) {
                ToastUtil.showToast("请输入宝贝详情...");
                return;
            }
            initUploadData();
        });

    }

    private void initAdapter(List<ImageBean> list) {
        mAdapter = new UploadPicAdapter(list, 9, "evaluate_pic");
        mAdapter.refresh();
        LinearLayoutManager layout = new LinearLayoutManager(BabyPublishActivity.this);
        layout.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        recyPic.setLayoutManager(layout);
        recyPic.setHasFixedSize(true);
        recyPic.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (Constants.IMAGEITEM_DEFAULT_ADD.equals(mAdapter.getItem(position).getImagePath())) {
                    //打开选择
                    new RxPermissions(BabyPublishActivity.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                                            .start(BabyPublishActivity.this, Constants.INTENT_REQUESTCODE_VERIFIED_IMG1);
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
                        mAdapter.remove(position);
                        mAdapter.refresh();
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG1 && data != null) {
            ArrayList<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null && resultList.size() > 0) {
                if (mAdapter.getData().size() > 0) {
                    List<ImageBean> newList = mAdapter.getData();
                    newList.addAll(mAdapter.getData().size() - 1,resultList);
                    mAdapter.setNewData(newList);
                } else {
                    mAdapter.addData(0, resultList);
                }
                mAdapter.refresh();
            }
        }

        if (resultCode == RESULT_OK && requestCode == ProductTypeActivity.CHOICE_PRODUCT_TYPE && data != null) {
            categoryListdto = (CategoryListdto) data.getSerializableExtra("catedto");
            tv_product_type.setText(categoryListdto.getTitle());
        }
    }

    private void initUploadData() {
        showLoadDialog();
        upPicPosition = 0;
        imgArr.clear();
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
            String title = et_product_title.getText().toString();
            String content = ed_content.getText().toString().trim();
            String price = et_product_price.getText().toString();
            publishBaby(title, content, imgArr, price, "广东省", "深圳市", "南山区");
        }
    }


    //发布宝贝
    private void publishBaby(String title, String content, List<String> imgArr, String score, String province, String city, String district) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("category_id",categoryListdto.getId());
        if (imgArr.size() > 0) {
            for (int i = 0; i < imgArr.size(); i++) {
                map.put("imgs[" + i + "]", imgArr.get(i));
            }
        }
        map.put("score", score);
        map.put("content", content);
        map.put("ext[province]", province);
        map.put("ext[city]", city);
        map.put("ext[district]", district);
        DataManager.getInstance().publishBaby(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                dissLoadDialog();
                ToastUtil.showToast("宝贝发布成功");
                finish();
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if(ApiException.getInstance().isSuccess()){
                    ToastUtil.showToast("宝贝发布成功");
                }else{
//                    ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

                setResult(Activity.RESULT_OK);
                finish();
            }
        }, map);
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
            ToastUtil.showToast("上传宝贝图片失败");
            return;
        }
        //showLoadDialog();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        DataManager.getInstance().uploadFiles(new DefaultSingleObserver<UploadFilesDto>() {
            @Override
            public void onSuccess(UploadFilesDto object) {
                if (object != null) {
                    imgArr.add(String.valueOf(object.getPath()));
                    dealUpload();
                } else {
                    dissLoadDialog();
                    ToastUtil.showToast("上传宝贝图片失败");
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.toast(ApiException.getInstance().getErrorMsg());
            }
        }, "image", part);
    }
}
