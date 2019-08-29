package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.NoticeDto;

import java.util.List;


/**
 * Created by rzb on 2019/06/18
 */
public class NewFriendNoticeAdapter extends BaseQuickAdapter<NoticeDto, BaseViewHolder> {
    private Context mContext;

    public NewFriendNoticeAdapter(List<NoticeDto> data, Context mContext) {
        super(R.layout.item_new_friend, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeDto item) {
        if (item != null) {
//         helper.setText(R.id.tv_new_friend_content, item.getData().getContent());
//         GlideUtils.getInstances().loadUserRoundImg(mContext, helper.getView(R.id.iv_new_friend_user_icon), item.getUserHeader());
//              switch (item.getStatus()){
//                case 0:
//                    helper.getView(R.id.tv_new_friend_user_status).setVisibility(View.VISIBLE);
//                    helper.setText(R.id.tv_new_friend_user_status, "已删除");
//                    helper.getView(R.id.but_new_friend_user_status).setVisibility(View.GONE);
//                    break;
//                case 1:
//                    helper.getView(R.id.tv_new_friend_user_status).setVisibility(View.VISIBLE);
//                    helper.setText(R.id.tv_new_friend_user_status, "已添加");
//                    helper.getView(R.id.but_new_friend_user_status).setVisibility(View.GONE);
//                    break;
//                case 2:
//                    helper.getView(R.id.tv_new_friend_user_status).setVisibility(View.GONE);
//                    helper.getView(R.id.but_new_friend_user_status).setVisibility(View.VISIBLE);
//                    break;
//                case 3:
//                    helper.getView(R.id.tv_new_friend_user_status).setVisibility(View.VISIBLE);
//                    helper.setText(R.id.tv_new_friend_user_status, "已过期");
//                    helper.getView(R.id.but_new_friend_user_status).setVisibility(View.GONE);
//                    break;
//            }
//            helper.addOnClickListener(R.id.but_new_friend_user_status);
//            helper.addOnClickListener(R.id.right_item_new_friend);
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