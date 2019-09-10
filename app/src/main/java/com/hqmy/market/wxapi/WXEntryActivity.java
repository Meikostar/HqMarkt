package com.hqmy.market.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.hqmy.market.R;
import com.hqmy.market.common.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 微信回调
 * Created by Administrator on 2018/10/20.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private final String TAG = WXEntryActivity.class.getName();
    private IWXAPI api;
    private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; //分享

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
        api.registerApp(Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int result = 0;
        int type = baseResp.getType(); //类型：分享还是登录
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    String code = ((SendAuth.Resp) baseResp).code;

                    loginRequest(code);
                    //这里拿到了这个code，去做2次网络请求获取access_token和用户个人信息
//                    getAccessToken(code);
                } else {
                    result = R.string.errcode_success;
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();
    }


    private void loginRequest(String code) {

        Intent intent = new Intent(Constants.WX_LOGIN_BROADCAST);
        intent.putExtra(Constants.INTENT_CODE, code);
        //发送一个广播
        sendBroadcast(intent);
        finish();
    }

}
