package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.AreaDto;
import com.hqmy.market.bean.UserInfoDto;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.view.activity.ConturyDetialActivity;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class Liveadapter extends BaseAdapter {
    private Context        context;
    private LayoutInflater inflater;
    private List<UserInfoDto>  list;
    private int            type = 0;//0 表示默认使用list数据
    private String         types;


    private int[] imgs;


    private String[] names;

    public Liveadapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<UserInfoDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
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
            view = inflater.inflate(R.layout.live_item, null);
            holder.txt_name = view.findViewById(R.id.txt_name);
            holder.img_icon = view.findViewById(R.id.img_icon);
            holder.img_type = view.findViewById(R.id.img_type);
            holder.ll_bg = view.findViewById(R.id.ll_bg);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        UserInfoDto bannerDto = list.get(i);

        if(!TextUtils.isEmpty(bannerDto.getName())){
            holder.txt_name.setText(bannerDto.getName());
        }
        if(i<3){

            holder.img_type.setVisibility(View.VISIBLE);
            if(i==0){
                holder.img_type.setBackgroundResource(R.drawable.circle_1);
                holder.img_type.setText("1");
            }else if(i==1){
                holder.img_type.setBackgroundResource(R.drawable.circle_2);
                holder.img_type.setText("2");
            }else if(i==2){
                holder.img_type.setBackgroundResource(R.drawable.circle_3);
                holder.img_type.setText("3");
            }

        }else {
            holder.img_type.setVisibility(View.GONE);
        }
        holder.ll_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, ConturyDetialActivity.class);
//                intent.putExtra("id",bannerDto.id+"");
//                intent.putExtra("title",bannerDto.title+"");
//                context.startActivity(intent);
            }
        });

        GlideUtils.getInstances().loadRoundImg(context,holder.img_icon,bannerDto.getAvatar(),R.drawable.moren_ren);
        // PROFILE_ITEM item = list.get(i);
        return view;
    }



    class ViewHolder {
        TextView     txt_name;
        ImageView    img_icon;
        TextView    img_type;
        LinearLayout ll_bg;

    }
}
