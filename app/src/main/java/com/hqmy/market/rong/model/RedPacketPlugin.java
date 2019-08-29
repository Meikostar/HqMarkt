package com.hqmy.market.rong.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import com.hqmy.market.view.mainfragment.chat.SendRedPacketActivity;
import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * 积分红包插件
 * Created by rzb on 2019/6/28.
 */
public class RedPacketPlugin implements IPluginModule {
    Conversation.ConversationType conversationType;
    String  targetId;

    @Override
    public Drawable obtainDrawable(Context context) {
        //设置插件 Plugin 图标
        return ContextCompat.getDrawable(context, io.rong.imkit.R.drawable.rc_ext_plugin_red_packet);
    }

    @Override
    public String obtainTitle(Context context) {
        //设置插件 Plugin 展示文字
        return "积分红包";
    }

    @Override
    public void onClick(final Fragment currentFragment, RongExtension extension) {
        //示例获取 会话类型、targetId、Context,此处可根据产品需求自定义逻辑，如:开启新的 Activity 等。
        conversationType = extension.getConversationType();
        targetId = extension.getTargetId();
        String msgType = null;
//                NONE(0, "none"),
//                PRIVATE(1, "private"),
//                DISCUSSION(2, "discussion"),
//                GROUP(3, "group"),
//                CHATROOM(4, "chatroom"),
//                CUSTOMER_SERVICE(5, "customer_service"),
//                SYSTEM(6, "system"),
//                APP_PUBLIC_SERVICE(7, "app_public_service"),
//                PUBLIC_SERVICE(8, "public_service"),
//                PUSH_SERVICE(9, "push_service"),
//                ENCRYPTED(11, "encrypted");
        switch (conversationType) {
            default:
                msgType = "private";
                Intent intent = new Intent(currentFragment.getActivity(), SendRedPacketActivity.class);
                intent.putExtra(SendRedPacketActivity.USER_ID, targetId);
                extension.startActivityForPluginResult(intent, 230, this);
                break;
            case GROUP:
                msgType = "group";
                break;
        }
    }

     @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
     }
}