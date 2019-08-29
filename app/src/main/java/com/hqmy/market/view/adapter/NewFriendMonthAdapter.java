package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.FriendListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import java.util.List;

/**
 * Created by rzb on 2019/6/18.
 */
public class NewFriendMonthAdapter extends BaseQuickAdapter<FriendListItemDto, BaseViewHolder> {
    private Context mContext;

    public NewFriendMonthAdapter(List<FriendListItemDto> data, Context mContext) {
        super(R.layout.item_new_friend, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendListItemDto item) {
        if (item != null) {
               helper.setText(R.id.tv_new_friend_user_name, item.getRemark_name());
               GlideUtils.getInstances().loadUserRoundImg(mContext, helper.getView(R.id.iv_new_friend_user_icon), Constants.WEB_IMG_URL_UPLOADS + item.getFriend_user().getData().getAvatar());
               if(item.getRela().equals("1")){
                   helper.getView(R.id.tv_new_friend_user_status).setVisibility(View.VISIBLE);
                   helper.setText(R.id.tv_new_friend_user_status, "已添加");
                   helper.getView(R.id.but_new_friend_user_status).setVisibility(View.GONE);
               }else{
                   helper.getView(R.id.tv_new_friend_user_status).setVisibility(View.GONE);
                   helper.getView(R.id.but_new_friend_user_status).setVisibility(View.VISIBLE);
               }
               helper.addOnClickListener(R.id.but_new_friend_user_status);
        }
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}