package com.hqmy.market.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CourseBean;
import com.hqmy.market.bean.VideoBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.mainfragment.learn.CourseWarehouseAudioFragment;
import com.hqmy.market.view.mainfragment.learn.CourseWarehouseGraphicFragment;
import com.hqmy.market.view.mainfragment.learn.CourseWarehouseIntroductionFragment;
import com.hqmy.market.view.mainfragment.learn.CourseWarehousePPTFragment;
import com.hqmy.market.view.mainfragment.learn.LearnViewPagerAdapter;
import com.hqmy.market.view.widgets.dialog.BaseDialog;
import com.hqmy.market.view.widgets.dialog.FeeTipDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 课件视频详情
 */
public class CourseWarehouseDetailActivity extends BaseActivity {
    @BindView(R.id.stl_learn_indicator)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.vp_learn_container)
    ViewPager mViewPager;
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.iv_paid_viewing)
    ImageView ivPaidViewing;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.rl_watch)
    RelativeLayout rlWatch;

    private String id;
    private CourseBean courseBean;
    private VideoBean videoBean;

    @Override
    public void initListener() {
        //播放视频
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (courseBean == null || !courseBean.getPaid()) {
                    FeeTipDialog dialog = new FeeTipDialog(CourseWarehouseDetailActivity.this);
                    dialog.setCancelable(false);
                    dialog.setYesOnclickListener("去支付", new BaseDialog.OnYesClickListener() {
                        @Override
                        public void onYesClick() {
                            dialog.dismiss();
                            Intent intent = new Intent(CourseWarehouseDetailActivity.this, VideoPayMethodActivity.class);
                            intent.putExtra("product_id", videoBean.getId());
                            intent.putExtra("author", courseBean.getAuthor() + "  " + videoBean.getCreated_at());
                            intent.putExtra("title", videoBean.getTitle());
                            intent.putExtra("money", videoBean.getScore());
                            intent.putExtra("cover", videoBean.getCover());
                            startActivity(intent);
                        }
                    });
                    dialog.setCancleClickListener("再想想", new BaseDialog.OnCloseClickListener() {

                        @Override
                        public void onCloseClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return;
                }
                if (courseBean.getVideo() != null) {
                    Intent intent = new Intent(CourseWarehouseDetailActivity.this, PLVideoViewActivity.class);
                    intent.putExtra("videoPath", Constants.WEB_IMG_URL_UPLOADS + courseBean.getVideo());
                    intent.putExtra("liveStreaming", 0);//点播
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.course_warehouse_detail_layout;
    }

    @Override
    public void initView() {
        id = getIntent().getStringExtra("id");
        mTitleText.setText(getIntent().getStringExtra("name"));
    }

    @Override
    public void initData() {
        getCourseProductsDetail();
    }

    private void getCourseProductsDetail() {
        showLoadDialog();
        DataManager.getInstance().getCourseProductsDetail(new DefaultSingleObserver<HttpResult<VideoBean>>() {
            @Override
            public void onSuccess(HttpResult<VideoBean> result) {
                dissLoadDialog();
                if (result != null && result.getData() != null) {
                    videoBean = result.getData();
                    GlideUtils.getInstances().loadNormalImg(CourseWarehouseDetailActivity.this, ivIcon, Constants.WEB_IMG_URL_UPLOADS + result.getData().getCover());
                    if (result.getData().getCourse_info() != null && result.getData().getCourse_info().getData() != null) {
                        courseBean = result.getData().getCourse_info().getData();
                        if (courseBean.getPaid()) {
                            //已付费
                            ivPaidViewing.setVisibility(View.GONE);
                            rlWatch.setVisibility(View.VISIBLE);
                        } else {
                            //未付费
                            ivPaidViewing.setVisibility(View.VISIBLE);
                            rlWatch.setVisibility(View.GONE);
                        }
                    }
                    initFragment(result.getData());
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, id);
    }

    private void initFragment(VideoBean videoBean) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(CourseWarehouseIntroductionFragment.newInstance(videoBean));
        fragments.add(CourseWarehousePPTFragment.newInstance(videoBean));
        fragments.add(CourseWarehouseGraphicFragment.newInstance(videoBean));
        fragments.add(CourseWarehouseAudioFragment.newInstance(videoBean));
        mViewPager.setAdapter(new LearnViewPagerAdapter(getSupportFragmentManager(), fragments));
        String[] titles = {"介绍", "PPT", "图文", "音频"};
        mSlidingTabLayout.setViewPager(mViewPager, titles);
    }

    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }

    }
}
