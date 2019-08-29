package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.UserInfoDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.view.mainfragment.chat.UserDetailActivity;

import java.util.List;

/**
 * 模糊查询用户列表适配器
 * Created by rzb on 2019/4/20
 */
public class AddFriendAdapter extends BaseQuickAdapter<UserInfoDto, BaseViewHolder> {
    private Context mContext;

    public AddFriendAdapter(Context mContext, List<UserInfoDto> data) {
        super(R.layout.item_add_friend_layout, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfoDto item) {
        if (item != null) {
            helper.setText(R.id.tv_user_name, item.getName());
            helper.setText(R.id.tv_user_phone, item.getPhone());
            GlideUtils.getInstances().loadRoundCornerImg(mContext, helper.getView(R.id.item_user_icon), mContext.getResources().getDimension(R.dimen.bj_10dp), Constants.WEB_IMG_URL_UPLOADS + item.getAvatar());
            helper.getView(R.id.user_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(UserDetailActivity.ITEM_USER, item);
                    gotoActivity(UserDetailActivity.class, bundle);
                }
            });
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