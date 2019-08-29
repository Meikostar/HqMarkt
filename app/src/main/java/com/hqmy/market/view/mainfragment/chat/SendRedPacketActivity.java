package com.hqmy.market.view.mainfragment.chat;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.RedPacketDto;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ErrorUtil;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.rong.message.redpacketchat.RedPackageMessage;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * 发红包
 */
public class SendRedPacketActivity extends BaseActivity {
    @BindView(R.id.iv_redpacket_back)
    ImageView iv_redpacket_back;
    @BindView(R.id.et_integral_num)
    EditText  et_integral_num;
    @BindView(R.id.but_send_submit)
    Button    but_send_submit;
    @BindView(R.id.et_integral_packet_content)
    EditText  et_integral_packet_content;
    @BindView(R.id.tv_integral_num)
    TextView  tv_integral_num;
    public static final String CONVERSATION_TYPE = "conversationType";//调用者传递的名字
    public static final String USER_ID = "user_id";//调用者传递的名字
    private Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
    private String user_id;


    @Override
    public int getLayoutId() {
        return R.layout.ui_send_red_packet_layout;
    }

    @Override
    public void initView() {
        user_id = getIntent().getStringExtra(USER_ID);
    }


    @Override
    public void initData() {
    }


    @Override
    public void initListener() {
        bindClickEvent(iv_redpacket_back, () -> {
             finish();
        });

        bindClickEvent(but_send_submit, () -> {
            if(TextUtils.isEmpty(et_integral_num.getText().toString())){
                ToastUtil.showToast("请输入积分");
                return;
            }
            hasPayPassword();
        });

        et_integral_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_integral_num.setText(s.toString());
            }
        });
    }




    private void sendSubmit(String userId, Conversation.ConversationType type, String remark, String redPacketId) {
        String conversationType = "1";
        Message message = Message.obtain(userId, type, RedPackageMessage.obtain(remark, redPacketId, conversationType, remark));
        RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
            }
            @Override
            public void onSuccess(Message message) {
                onBackPressed();
            }
            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    /**
     *发送红包
     */
    private void sendRedPacket( String pay_password, String total, String ar_user_id, String say) {
        showLoadDialog();
        DataManager.getInstance().sendRedPacket(new DefaultSingleObserver<HttpResult<RedPacketDto>>() {
            @Override
            public void onSuccess(HttpResult<RedPacketDto> result) {
                RedPacketDto redPacketDto = result.getData();
                sendSubmit(user_id, conversationType, say, String.valueOf(redPacketDto.getId()));
                dissLoadDialog();
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ErrorUtil.getInstance().getErrorMessage(throwable));
                dissLoadDialog();
            }
        }, pay_password, total, ar_user_id, say);
    }

    /**
     * 是否设置支付密码
     */
    private void hasPayPassword() {
        sendRedPacket("123456", et_integral_num.getText().toString(), user_id, et_integral_packet_content.getText().toString());
    }
}
