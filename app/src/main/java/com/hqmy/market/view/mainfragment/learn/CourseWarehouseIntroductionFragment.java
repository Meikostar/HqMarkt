package com.hqmy.market.view.mainfragment.learn;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.VideoBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.activity.VideoPayMethodActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class CourseWarehouseIntroductionFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_watch_count)
    TextView tvWatchCount;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    @BindView(R.id.tv_to_pay)
    TextView tvToPay;

    VideoBean videoBean;

    public static CourseWarehouseIntroductionFragment newInstance(VideoBean videoBean) {
        CourseWarehouseIntroductionFragment fragment = new CourseWarehouseIntroductionFragment();
        fragment.videoBean = videoBean;
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.course_warehouse_introduction_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        if (videoBean != null){
            tvTitle.setText(videoBean.getTitle());
            tvWatchCount.setText(videoBean.getSales_count()+"人观看");
            if (videoBean.getCourse_info() != null && videoBean.getCourse_info().getData() != null){
                tvAuthor.setText(videoBean.getCourse_info().getData().getAuthor()+"  "+videoBean.getCreated_at());
                tvIntroduce.setText(Html.fromHtml(videoBean.getCourse_info().getData().getIntroduce()));
                if (videoBean.getCourse_info().getData().getPaid()){
                    tvToPay.setVisibility(View.GONE);
                }else {
                    tvToPay.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    @Override
    protected void initListener() {

    }
    @OnClick({R.id.tv_to_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_to_pay:
                if (TextUtils.isEmpty(ShareUtil.getInstance().get(Constants.USER_TOKEN))) {
                    gotoActivity(LoginActivity.class);
                    return;
                }
                if (videoBean ==null || videoBean.getCourse_info() == null ||videoBean.getCourse_info().getData() == null ) return;
                Intent intent = new Intent(getActivity(),VideoPayMethodActivity.class);
                intent.putExtra("product_id",videoBean.getId());
                intent.putExtra("author",videoBean.getCourse_info().getData().getAuthor()+"  "+videoBean.getCreated_at());
                intent.putExtra("title",videoBean.getTitle());
                intent.putExtra("money",videoBean.getScore());
                intent.putExtra("cover",videoBean.getCover());
                startActivity(intent);
                break;
        }

    }
}
