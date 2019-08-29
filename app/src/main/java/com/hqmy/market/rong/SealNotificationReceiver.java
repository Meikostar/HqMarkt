package com.hqmy.market.rong;

import android.content.Context;
import com.hqmy.market.common.utils.LogUtil;
import io.rong.push.PushType;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;


public class SealNotificationReceiver extends PushMessageReceiver {
    private static final String TAG = SealNotificationReceiver.class.getSimpleName();
    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage message) {
        LogUtil.e(TAG,"-- onNotificationMessageArrived pushType = "+pushType.getName()+" message="+message.getPushId());
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage message) {
        LogUtil.e(TAG,"-- onNotificationMessageClicked pushType = "+pushType.getName()+" message="+message.getPushId());
        return false;
    }

    @Override
    public void onThirdPartyPushState(PushType pushType, String action, long resultCode) {
        super.onThirdPartyPushState(pushType, action, resultCode);
        LogUtil.e(TAG,"-- onThirdPartyPushState pushType = "+pushType.getName()+" action="+action+" resultCode"+resultCode);
        if("com.xiaomi.mipush.CLEAR_NOTIFICATION".equals(action)) {
            LogUtil.e(TAG,"-- onThirdPartyPushState action="+action);
        }
    }
}
