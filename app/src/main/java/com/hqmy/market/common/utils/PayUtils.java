package com.hqmy.market.common.utils;


import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.hqmy.market.bean.WEIXINREQ;
import com.hqmy.market.bean.ZfbPayResult;
import com.hqmy.market.view.fragments.PayResultListener;
import com.hqmy.market.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.Map;

/**
 * 支付工具
 * Created by Administrator on 2017/3/6.
 */

public class PayUtils {

    private static final String   TAG = PayUtils.class.getSimpleName();
    private static       PayUtils payUtils;
    private static       IWXAPI   api;

    private PayUtils() {
    }

    public static PayUtils getInstances() {
        if (payUtils == null) {
            payUtils = new PayUtils();
        }
        return payUtils;
    }

    public void WXPay(Activity context, WEIXINREQ orderNoDto) {
        Intent intent = new Intent(context, WXPayEntryActivity.class);
        intent.putExtra("weixinreq", orderNoDto);
        context.startActivityForResult(intent, 12);
        //
        //        if (!InstallWeChatOrAliPay.getInstance().isWeixinAvilible(context)) {
        //            ToastUtil.showToast("请先安装微信");
        //            return;
        //        }
        //        api = WXAPIFactory.createWXAPI(context, null);
        //        api.registerApp(orderNoDto.appid);
        //        PayReq req = new PayReq();
        //        //req.appId = "wxff73fc06e1d70439";  // 测试用appId
        //        req.appId = orderNoDto.appid;
        //        req.partnerId = orderNoDto.partnerid;
        //        req.prepayId = orderNoDto.prepayid;
        //        req.nonceStr = orderNoDto.noncestr;
        //        req.timeStamp = orderNoDto.timestamp;
        //        req.packageValue = "Sign=WXPay";
        ////        req.signType = orderNoDto.getSignType();
        //        req.sign = orderNoDto.sign;
        //        req.extData = "app data"; // optional
        ////         String trade_sn = json.getString("out_trade_no");
        ////         在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        //        boolean checkArgs = req.checkArgs();
        //        boolean sendReq = api.sendReq(req);
        //        LogUtil.i(TAG, sendReq + "-----" + checkArgs + "--------");


    }

    public void zfbPaySync(final Activity context, final String signStr, PayResultListener payResultListener) {

        new RxAsyncTask<String, Integer, ZfbPayResult>() {

            @Override
            protected ZfbPayResult call(String... strings) {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(context);
                // 调用支付接口，获取支付结果
                final Map<String, String> stringStringMap = alipay.payV2(signStr, true);
                //LogUtil.i(TAG, "payInfo---:" + signStr);
                return new ZfbPayResult(stringStringMap);
            }

            @Override
            protected void onResult(ZfbPayResult zfbPayResult) {
                super.onResult(zfbPayResult);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = zfbPayResult.getResult();// 同步返回需要验证的信息
                String resultStatus = zfbPayResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    //ToastUtil.toast("支付成功");
                    if (payResultListener != null) {
                        payResultListener.zfbPayOk(true);
                    }
                } else {
                    payResultListener.zfbPayOk(false);
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    //                    ToastUtil.toast("支付失败");
                }
            }
        }.execute();
    }



}
