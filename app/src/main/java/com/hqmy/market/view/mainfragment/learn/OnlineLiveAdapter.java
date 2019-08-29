package com.hqmy.market.view.mainfragment.learn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.bean.LiveCatesBean;
import com.hqmy.market.view.activity.LiveVideoViewActivity;
import com.hqmy.market.view.activity.PovertyReliefActivity;

import java.util.List;

/**
 * 在线直播首页
 */
public class OnlineLiveAdapter extends BaseAdapter {
    List<LiveCatesBean> dataList;
    Context mContext;

    public OnlineLiveAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<LiveCatesBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<LiveCatesBean> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Override
    public LiveCatesBean getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.head_online_live_poverty, null);
        }
        LiveCatesBean item = getItem(position);
        ((TextView) view.findViewById(R.id.tv_online_live_poverty)).setText(item.getCat_name());
        RelativeLayout relativeLayout = view.findViewById(R.id.rl_online_live_poverty);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPovertyReliefActivity(item.getId(), item.getCat_name());
            }
        });
        RecyclerView mRecyclerView = view.findViewById(R.id.rcl_online_live_poverty);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        OnlineLiveItemAdapter onlineLiveItemAdapter = new OnlineLiveItemAdapter();
        if (item.getVideos() != null) {
            onlineLiveItemAdapter.setNewData(item.getVideos().getData());
        }
        mRecyclerView.setAdapter(onlineLiveItemAdapter);
        onlineLiveItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, LiveVideoViewActivity.class);
                intent.putExtra("videoPath", onlineLiveItemAdapter.getItem(position).getRtmp_play_url());
                if (onlineLiveItemAdapter.getItem(position).getRoom() != null && onlineLiveItemAdapter.getItem(position).getRoom().getData() != null) {
                    intent.putExtra("roomId", onlineLiveItemAdapter.getItem(position).getRoom().getData().getId());
                }
                intent.putExtra("videoId", onlineLiveItemAdapter.getItem(position).getId());
                intent.putExtra("liveStreaming", 1);
                mContext.startActivity(intent);
            }
        });
        return view;
    }


    private void gotoPovertyReliefActivity(String id, String name) {
        Intent intent = new Intent(mContext, PovertyReliefActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        mContext.startActivity(intent);
    }


}
