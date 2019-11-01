package com.hqmy.market.qiniu.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hqmy.market.R;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.view.widgets.autoview.CircleTransform;
import com.orzangleli.xdanmuku.XAdapter;

import java.util.Random;

public class DanmuAdapter extends XAdapter<DanmuEntity> {

    Random random;


    private Context context;

    public DanmuAdapter(Context c) {
        super();
        context = c;
        random = new Random();
    }

    @Override
    public View getView(DanmuEntity danmuEntity, View convertView) {

        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_danmu, null);
            holder = new ViewHolder();
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_usernickname);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Object content;
        Object imgUrl=danmuEntity.getUrl();

        if(imgUrl!=null){
            if(imgUrl instanceof String){
                if(((String) imgUrl).contains("http")){
                    content= imgUrl;
                }else{
                    content=Constants.WEB_IMG_URL_UPLOADS+(String)imgUrl;
                }
            }else {
                content= imgUrl;
            }
        }else {
            content= imgUrl;
        }
                Glide.with(context)
                        .asBitmap()
                        .load(content)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.moren_ren)
                                .error(R.drawable.moren_ren)
        //                         .diskCacheStrategy(DiskCacheStrategy.NONE)// 缓存策略
                                .transforms(new CenterCrop(), new CircleTransform()))
                        .into(holder.image);
//        GlideUtils.getInstances().loadUserRoundImg(context,holder.image,danmuEntity.getUrl());
        holder.content.setText(danmuEntity.getContent());
        // holder.content.setTextColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        holder.tvName.setText(danmuEntity.getName());

        return convertView;
    }

    @Override
    public int[] getViewTypeArray() {
        int type[] = {0, 1};
        return type;
    }

    @Override
    public int getSingleLineHeight() {
        //将所有类型弹幕的布局拿出来，找到高度最大值，作为弹道高度
        View view = LayoutInflater.from(context).inflate(R.layout.item_danmu, null);
        //指定行高
        view.measure(0, 0);
        return view.getMeasuredHeight();

    }


    class ViewHolder {
        public TextView content;
        public TextView tvName;
        public ImageView image;
    }


}
