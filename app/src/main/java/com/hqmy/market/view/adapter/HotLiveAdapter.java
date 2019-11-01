package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.bean.UserInfoDto;
import com.hqmy.market.bean.VideoLiveBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ScreenSizeUtil;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.activity.LiveVideoViewActivity;
import com.hqmy.market.view.activity.RequestLivePermissionActivity;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;

import java.util.List;


/**
 * 消费商品推荐适配器
 * Created by rzb on 2019/4/20
 */
public class HotLiveAdapter extends BaseAdapter {
    private Context           context;
    private LayoutInflater    inflater;
    private List<VideoLiveBean> list;
    private int               type = 0;//0 表示默认使用list数据
    private String            types;


    private int[] imgs;


    private String[] names;

    public HotLiveAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<VideoLiveBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_hot_live_layout, null);
            holder.tv_title = view.findViewById(R.id.tv_title);
            holder.img_icon = view.findViewById(R.id.iv_img);
            holder.tv_cout = view.findViewById(R.id.tv_cout);
            holder.ll_bg = view.findViewById(R.id.rl_bg);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        VideoLiveBean bannerDto = list.get(i);

        if (!TextUtils.isEmpty(bannerDto.getTitle())) {
            holder.tv_title.setText(bannerDto.getTitle());
        }
        if (!TextUtils.isEmpty(bannerDto.getChatter_total())) {
            holder.tv_cout.setText(bannerDto.getChatter_total()+"观看");
        }
        holder.ll_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLiveVideoActivity(bannerDto);
            }
        });

        GlideUtils.getInstances().loadRoundImg(context, holder.img_icon, bannerDto.getImages(), R.drawable.moren_sf);
        // PROFILE_ITEM item = list.get(i);
        return view;
    }
    private void startLiveVideoActivity(VideoLiveBean videoLiveBean) {
        Intent intent = new Intent(context, LiveVideoViewActivity.class);
        intent.putExtra("videoPath", videoLiveBean.getRtmp_play_url());
        if (videoLiveBean.getRoom() != null && videoLiveBean.getRoom().getData() != null) {
            intent.putExtra("roomId", videoLiveBean.getRoom().getData().getId());
        }
        if (videoLiveBean.apply != null && videoLiveBean.apply.getData() != null) {
            intent.putExtra("userId", videoLiveBean.apply.getData().getUser_id());
        }

        intent.putExtra("videoId", videoLiveBean.getId());
        intent.putExtra("liveStreaming", 1);
        context.startActivity(intent);
    }

    class ViewHolder {
        TextView       tv_title;
        ImageView      img_icon;
        TextView       tv_cout;
        RelativeLayout ll_bg;

    }

}
