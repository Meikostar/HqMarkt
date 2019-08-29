package com.hqmy.market.view.mainfragment.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.GroupInfoDto;
import com.hqmy.market.bean.GroupUserItemInfoDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.rong.utils.OperationRong;
import com.hqmy.market.view.widgets.dialog.BaseDialog;
import com.hqmy.market.view.widgets.dialog.ConfirmDialog;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class GroupDetailActivity extends BaseActivity {
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.layout_user_one)
    LinearLayout layout_user_one;
    @BindView(R.id.img_user_header_one)
    ImageView img_user_header_one;
    @BindView(R.id.tv_user_header_one)
    TextView  tv_user_header_one;
    @BindView(R.id.layout_user_two)
    LinearLayout layout_user_two;
    @BindView(R.id.img_user_header_two)
    ImageView img_user_header_two;
    @BindView(R.id.tv_user_header_two)
    TextView tv_user_header_two;
    @BindView(R.id.layout_user_three)
    LinearLayout layout_user_three;
    @BindView(R.id.img_user_header_three)
    ImageView img_user_header_three;
    @BindView(R.id.tv_user_header_three)
    TextView tv_user_header_three;
    @BindView(R.id.btn_add)
    TextView btn_add;
    @BindView(R.id.btn_del)
    TextView btn_del;
    @BindView(R.id.tv_more)
    TextView tv_more;
    @BindView(R.id.tv_group_name)
    TextView tv_group_name;
    @BindView(R.id.tv_group_notice)
    TextView tv_group_notice;
    @BindView(R.id.tv_group_my_nick)
    TextView tv_group_my_nick;
    @BindView(R.id.img_top_chat)
    ImageView img_top_chat;
    @BindView(R.id.ll_group_clean_data)
    TextView ll_group_clean_data;
    @BindView(R.id.btn_logout)
    TextView btn_logout;
    private String groupId;
    private GroupInfoDto mGroupInfoDto;

    @Override
    public int getLayoutId() {
        return R.layout.ui_group_detail_layout;
    }

    @Override
    public void initView() {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    if (conversation == null) {
                        return;
                    }
                    if (conversation.isTop()) {
                        img_top_chat.setSelected(true);
                    } else {
                        img_top_chat.setSelected(false);
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    @Override
    public void initListener() {
        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(btn_add, () -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.TYPE, ModifyGroupActivity.GROUP_ADD);
            bundle.putString(Constants.GROUP_ID, groupId);
            gotoActivity(ModifyGroupActivity.class, false, bundle,ModifyGroupActivity.GROUP_ADD);
        });

        bindClickEvent(btn_del, () -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.TYPE, ModifyGroupActivity.GROUP_DEL);
            bundle.putString(Constants.GROUP_ID, groupId);
            gotoActivity(ModifyGroupActivity.class, false, bundle, ModifyGroupActivity.GROUP_DEL);
        });

        bindClickEvent(img_top_chat, () -> {
            if (!img_top_chat.isSelected()) {
                img_top_chat.setSelected(true);
                OperationRong.setConversationTop(GroupDetailActivity.this, Conversation.ConversationType.GROUP, groupId, true);
            } else {
                img_top_chat.setSelected(false);
                OperationRong.setConversationTop(GroupDetailActivity.this, Conversation.ConversationType.GROUP, groupId, false);
            }
        });

        bindClickEvent(ll_group_clean_data, () -> {
            ConfirmDialog dialog = new ConfirmDialog(GroupDetailActivity.this);
            dialog.setMessage("确定清空聊天记录");
            dialog.setTitle("温馨提示");
            dialog.setYesOnclickListener("确定", new BaseDialog.OnYesClickListener() {
                @Override
                public void onYesClick() {
                    dialog.dismiss();
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                ToastUtil.showToast("清除成功");
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                ToastUtil.showToast("清除失败");
                                ;
                            }
                        });
                        RongIMClient.getInstance().cleanRemoteHistoryMessages(Conversation.ConversationType.GROUP, groupId, System.currentTimeMillis(), null);
                    }
                }
            });
            dialog.show();
        });

        bindClickEvent(btn_logout, () -> {
            if (mGroupInfoDto == null) {
                return;
            }
            ConfirmDialog dialog = new ConfirmDialog((GroupDetailActivity.this));
            //if ("Y".equals(GroupDetailActivity.getIsOwner())) {
            //    dialog.setMessage("是否解散该群组");
            //} else {
            //    dialog.setMessage("是否退出该群组");
            //}

            dialog.setTitle("温馨提示");
            dialog.setYesOnclickListener("确定", new BaseDialog.OnYesClickListener() {
                @Override
                public void onYesClick() {
                    dialog.dismiss();
                    //if ("Y".equals(groupInfoBean.getIsOwner())) {
                    //    delGroup();
                    //} else {
                    //    quitGroup();
                    //}
                }
            });
            dialog.show();
        });
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            groupId = bundle.getString(Constants.GROUP_ID);
        }
        groupInfo();
    }

    private void groupInfo() {
          showLoadDialog();
          DataManager.getInstance().getGroupInfo(new DefaultSingleObserver<HttpResult<GroupInfoDto>>() {
             @Override
             public void onSuccess(HttpResult<GroupInfoDto> result) {
                dissLoadDialog();
                if(result.getData() != null){
                    mGroupInfoDto = result.getData();
                    initDataView();
                }
             }

             @Override
             public void onError(Throwable throwable) {
                 dissLoadDialog();
                }
            }, groupId);
    }

    private void initDataView() {
        if (mGroupInfoDto != null) {
           tv_group_name.setText(mGroupInfoDto.getGroup_name());
           if(mGroupInfoDto.getNotice() != null) {
               tv_group_notice.setText(mGroupInfoDto.getNotice());
           }
           if(mGroupInfoDto.getSelf_group_user() != null){
               if(mGroupInfoDto.getSelf_group_user().getRemark() != null) {
                   tv_group_my_nick.setText(mGroupInfoDto.getSelf_group_user().getGroup_nickname());
               }else{
                   tv_group_my_nick.setText(mGroupInfoDto.getSelf_group_user().getRemark());
               }
           }
           if(mGroupInfoDto.getGroup_users().getData() != null){
               List<GroupUserItemInfoDto>  guiDtoLists = mGroupInfoDto.getGroup_users().getData();
               if(guiDtoLists.size() != 0){
                   if(guiDtoLists.size() == 1){
                       layout_user_one.setVisibility(View.VISIBLE);
                       layout_user_two.setVisibility(View.GONE);
                       layout_user_three.setVisibility(View.GONE);
                       tv_user_header_one.setText(guiDtoLists.get(0).getGroup_nickname());
                       GlideUtils.getInstances().loadRoundCornerImg(GroupDetailActivity.this,
                               img_user_header_one, 4, Constants.WEB_IMG_URL_UPLOADS + guiDtoLists.get(0).getUser().getData().getAvatar());
                   }else if(guiDtoLists.size() == 2){
                       layout_user_one.setVisibility(View.VISIBLE);
                       layout_user_two.setVisibility(View.VISIBLE);
                       layout_user_three.setVisibility(View.GONE);

                       tv_user_header_one.setText(guiDtoLists.get(0).getGroup_nickname());
                       GlideUtils.getInstances().loadRoundCornerImg(GroupDetailActivity.this,
                               img_user_header_one, 4, Constants.WEB_IMG_URL_UPLOADS + guiDtoLists.get(0).getUser().getData().getAvatar());

                       tv_user_header_two.setText(guiDtoLists.get(1).getGroup_nickname());
                       GlideUtils.getInstances().loadRoundCornerImg(GroupDetailActivity.this,
                               img_user_header_two, 4, Constants.WEB_IMG_URL_UPLOADS + guiDtoLists.get(1).getUser().getData().getAvatar());
                   }else if(guiDtoLists.size() == 3){
                       layout_user_one.setVisibility(View.VISIBLE);
                       layout_user_two.setVisibility(View.VISIBLE);
                       layout_user_three.setVisibility(View.VISIBLE);


                       tv_user_header_one.setText(guiDtoLists.get(0).getGroup_nickname());
                       GlideUtils.getInstances().loadRoundCornerImg(GroupDetailActivity.this,
                               img_user_header_one, 2.5f, Constants.WEB_IMG_URL_UPLOADS + guiDtoLists.get(0).getUser().getData().getAvatar());

                       tv_user_header_two.setText(guiDtoLists.get(1).getGroup_nickname());
                       GlideUtils.getInstances().loadRoundCornerImg(GroupDetailActivity.this,
                               img_user_header_two, 2.5f, Constants.WEB_IMG_URL_UPLOADS + guiDtoLists.get(1).getUser().getData().getAvatar());

                       tv_user_header_three.setText(guiDtoLists.get(2).getGroup_nickname());
                       GlideUtils.getInstances().loadRoundCornerImg(GroupDetailActivity.this,
                               img_user_header_three, 2.5f, Constants.WEB_IMG_URL_UPLOADS + guiDtoLists.get(2).getUser().getData().getAvatar());

                   }
               }else{
                   layout_user_one.setVisibility(View.GONE);
                   layout_user_two.setVisibility(View.GONE);
                   layout_user_three.setVisibility(View.GONE);
               }
           }else{
               layout_user_one.setVisibility(View.GONE);
               layout_user_two.setVisibility(View.GONE);
               layout_user_three.setVisibility(View.GONE);
           }
        }
    }

    private void modifyGroup(HashMap<String, String> map, String url) {
//        showLoadDialog();
//        DataManager.getInstance().modifyGroup(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@Nullable Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                dissLoadDialog();
//                if (response.code() == 200) {
//                    ToastUtil.showToast("修改成功");
//                    String groupHead = map.get("groupHeader");
//                    Group group = new Group(groupId, mGroupInfoBean.getGroupName(), Uri.parse(groupHead));
//                    RongIM.getInstance().refreshGroupInfoCache(group);
//                    GlideUtils.getInstances().loadRoundImg(GroupDetailActivity.this, img_group_header, url, R.mipmap.icon_group_default);
//                } else {
//                    ToastUtil.showToast(ApiException.getShowToast(response));
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
//                dissLoadDialog();
//                ToastUtil.showToast("失败");
//            }
//        }, map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != Activity.RESULT_OK || data == null) {
//            return;
//        }
//        if (requestCode == Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//            List<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
//            if (resultList != null && resultList.size() > 0) {
//                String imageUrl = resultList.get(0).getImagePath();
//                uploadImg(imageUrl);
//            }
//            return;
//        }
        //if (requestCode == ModifyGroupActivity.GROUP_DEL) {
        //            int number = data.getIntExtra(Constants.NUMBER, 0);
        //            if (number < 3) {
        //                setResult(Activity.RESULT_OK);
        //                RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, groupId, null);
        //                Intent intent = new Intent(Constants.BROADCAST_CHAT);
        //                发送本地广播
        //                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        //                setResult(Activity.RESULT_OK);
        //                getActivity().onBackPressed();
        //                finish();
        //            } else {
        //                Bundle bundle = new Bundle();
        //                bundle.putString(Constants.GROUP_ID, data.getStringExtra(Constants.GROUP_ID));
        //                gotoActivity(GroupDetailActivity.class, true, bundle);
        //        }
        //
        //            return;
        //        } else if (requestCode == ModifyGroupActivity.GROUP_CHANGE || requestCode == ModifyGroupActivity.GROUP_ADD) {
        //            Bundle bundle = new Bundle();
        //            bundle.putString(Constants.GROUP_ID, data.getStringExtra(Constants.GROUP_ID));
        //            gotoActivity(GroupDetailActivity.class, true, bundle);
        //        }
        //
        //        if (requestCode == UserNameActivity.TYPE_GROUP_NAME) {
        //            if (mGroupInfoBean != null) {
        //                RongIM.getInstance().refreshGroupInfoCache(new Group(groupId, data.getStringExtra(Constants.GROUP_NAME), Uri.parse(mGroupInfoBean.getGroupHeader())));
        //            }
        //            tv_group_name.setText(data.getStringExtra(Constants.GROUP_NAME));
        //        } else if (requestCode == UserNameActivity.TYPE_GROUP_NICK) {
        //            tv_group_nick.setText(data.getStringExtra(Constants.GROUP_NICK));
        //        }
        //
        //        if(requestCode == GroupRemarkActivity.MODIFY_GROUP_MARK){
        //            String markStr = data.getStringExtra(Constants.GROUP_REMARK);
        //            BroadcastManager.getInstance(this).sendBroadcast(MODIFY_REMARK_TEXT,markStr);
        //        }
        //    }
    }
}