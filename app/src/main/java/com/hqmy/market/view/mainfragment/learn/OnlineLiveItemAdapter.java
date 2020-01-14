package com.hqmy.market.view.mainfragment.learn;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.VideoLiveBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.activity.LiveVideoViewActivity;

public class OnlineLiveItemAdapter extends BaseQuickAdapter<VideoLiveBean, BaseViewHolder> {
    public OnlineLiveItemAdapter() {
        super(R.layout.item_online_live, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoLiveBean item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_icon), Constants.WEB_IMG_URL_UPLOADS + item.getImages());
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_chatter_total, item.getChatter_total()+"人观看");
        RelativeLayout rlbg=helper.getView(R.id.rl_bg);
        rlbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLiveVideoActivity(item);
            }
        });
    }


    private void startLiveVideoActivity(VideoLiveBean videoLiveBean) {
        Intent intent = new Intent(mContext, LiveVideoViewActivity.class);

        if (videoLiveBean.getRoom() != null && videoLiveBean.getRoom().getData() != null) {
            intent.putExtra("roomId", videoLiveBean.getRoom().getData().getId());
        }
        if (videoLiveBean.apply != null && videoLiveBean.apply.getData() != null&&TextUtil.isEmpty(videoLiveBean.end_at)) {
            intent.putExtra("videoPath", videoLiveBean.apply.getData().rtmp_play_url);
        }else {
            if(TextUtil.isNotEmpty(videoLiveBean.end_at)&&TextUtil.isNotEmpty(videoLiveBean.play_url)){
                intent.putExtra("videoPath", "http://pili-vod.app.b-market.shop/"+videoLiveBean.play_url);
            }
        }
        intent.putExtra("videoId", videoLiveBean.getId());
        intent.putExtra("liveStreaming", 1);
        mContext.startActivity(intent);
    }
}
