package com.hqmy.market.view.mainfragment;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.common.Constants;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.mainfragment.community.NewsFragment;
import com.hqmy.market.view.mainfragment.community.TopicFragment;

import com.hqmy.market.view.widgets.autoview.AutoViewPager;
import com.hqmy.market.view.widgets.dialog.PhotoSelectDialog;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * 社区
 * Created by rzb on 2019/04/18.
 */
public class CommunityFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener{
    @BindView(R.id.community_header)
    RadioGroup    community_header;
    @BindView(R.id.community_btn_news)
    RadioButton   community_btn_news;
    @BindView(R.id.community_btn_topic)
    RadioButton   community_btn_topic;
    @BindView(R.id.community_viewpager)
    AutoViewPager community_viewpager;
    @BindView(R.id.community_btn_publish)
    ImageView     community_btn_publish;
    private List<Fragment> fragments = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        initViewPager();
    }

    @Override
    protected void initListener() {
        bindClickEvent(community_btn_news, () -> {
            community_viewpager.setCurrentItem(0, false);
        });
        bindClickEvent(community_btn_topic, () -> {
            community_viewpager.setCurrentItem(1, false);
        });
        bindClickEvent(community_btn_publish, () -> {
            if(TextUtils.isEmpty(ShareUtil.getInstance().get(Constants.USER_TOKEN))) {
                gotoActivity(LoginActivity.class);
            }else{
                PhotoSelectDialog dialog = new PhotoSelectDialog(getActivity(), new PhotoSelectDialog.PhotoSelectListener() {
                    @Override
                    public void callbackPhotoSelect(int index) {
                        if (index == 1) {
                            setPhotoSelect(false, Constants.INTENT_REQUESTCODE_PUBLISH);
                        } else if (index == 2) {
                            setPhotoSelect(true, Constants.INTENT_REQUESTCODE_PUBLISH);
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    private void initViewPager() {
        fragments.add(new NewsFragment());
        fragments.add(new TopicFragment());
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        };
        community_viewpager.setAdapter(mAdapter);
        community_viewpager.setScanScroll(false);
        community_viewpager.setOffscreenPageLimit(2);
        community_viewpager.setOnPageChangeListener(this);
    }

    private void setPhotoSelect(boolean isCamera, int requestCode) {
        ImagePicker imagePicker = new ImagePicker();
        new RxPermissions(getActivity()).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (isCamera) {
                            if (permission.granted && Manifest.permission.CAMERA.equals(permission.name)) {
                                // 用户已经同意该权限
                                imagePicker.pickType(ImagePickType.ONLY_CAMERA);//设置选取类型(拍照、单选、多选)
                                imagePicker.start(getActivity(), requestCode);
                            }
                        } else if (permission.granted && Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission.name)) {
                            imagePicker.pickType(ImagePickType.MULTI);//设置选取类型(拍照、单选、多选)
                            imagePicker.maxNum(6);//设置最大选择数量(拍照和单选都是1，修改后也无效)
                            imagePicker.needCamera(false);//是否需要在界面中显示相机入口(类似微信)
                            imagePicker.displayer(new GlideImagePickerDisplayer());//自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                            imagePicker.start(getActivity(), requestCode);
                        }
                    }
                });
        }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
