package com.hqmy.market.rong.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.lwkandroid.imagepicker.utils.BroadcastManager;
import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.IPluginRequestPermissionResultCallback;
import io.rong.imkit.plugin.image.PictureSelectorActivity;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.model.Conversation;

/**
 * 自定义图片插件
 * Created by rzb on 2019/4/27.
 */

public class MyImagePlugin implements IPluginModule, IPluginRequestPermissionResultCallback {
    Conversation.ConversationType conversationType;
    String                        targetId;
    private Context mContext;
    private static final String READ_PLUGIN_MESSAGE_SEND_MY_IMAGE = "read_plugin_message_send_my_image";//发送自定义图片

    public MyImagePlugin() {
    }

    public Drawable obtainDrawable(Context context) {
        this.mContext = context;
        return ContextCompat.getDrawable(context, io.rong.imkit.R.drawable.rc_ext_plugin_image_selector);
    }

    public String obtainTitle(Context context) {
        return context.getString(io.rong.imkit.R.string.rc_plugin_image);
    }

    public void onClick(Fragment currentFragment, RongExtension extension) {
        this.conversationType = extension.getConversationType();
        this.targetId = extension.getTargetId();
        String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};
        if(PermissionCheckUtil.checkPermissions(currentFragment.getContext(), permissions)) {
            Intent intent = new Intent(currentFragment.getActivity(), PictureSelectorActivity.class);
            extension.startActivityForPluginResult(intent, 23, this);
        } else {
            extension.requestPermissionForPluginResult(permissions, 255, this);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if(resultCode == -1){
            String strList =  data.getStringExtra("android.intent.extra.RETURN_RESULT");
            if(strList != null) {
//                List<String> items = new ArrayList<>();
//                for (ImageBean imageBean : lists) {
//                    items.add(imageBean.getImagePath());
//                }
                BroadcastManager.getInstance(mContext).sendBroadcast(READ_PLUGIN_MESSAGE_SEND_MY_IMAGE, strList);
//                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
//                Map<String, Integer> retMap2 = gson.fromJson(strList, new TypeToken<Map<String, Integer>>() {
//                }.getType());
//                for (String key : retMap2.keySet()) {
//                    conversationFragmentEx.sendMyImageMessage(targetId, mConversationType, key);
//                 }
              }
          }
    }

    public boolean onRequestPermissionResult(Fragment fragment, RongExtension extension, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(PermissionCheckUtil.checkPermissions(fragment.getActivity(), permissions)) {
            Intent intent = new Intent(fragment.getActivity(), PictureSelectorActivity.class);
            extension.startActivityForPluginResult(intent, 23, this);
        } else {
            extension.showRequestPermissionFailedAlter(PermissionCheckUtil.getNotGrantedPermissionMsg(fragment.getActivity(), permissions, grantResults));
        }
        return true;
    }
}