package com.hqmy.market.rong.message.redpacketchat;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * 自定义积分红包消息
 * Created by Lenovo on 2019/3/4.
 * 注解名：MessageTag ；属性：value ，flag； value 即 ObjectName 是消息的唯一标识不可以重复，
 * 开发者命名时不能以 RC 开头，避免和融云内置消息冲突；flag 是用来定义消息的可操作状态。
 * 如下面代码段，自定义消息名称 CustomizeMessage ，vaule 是 app:custom ，
 * flag 是 MessageTag.ISCOUNTED | MessageTag.ISPERSISTED 表示消息计数且存库。
 * app:ReadMessage: 这是自定义消息类型的名称，测试的时候用"app:ReadMessage"；
 */
@MessageTag(value = "app:IntegralRedPacke", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class RedPackageMessage extends MessageContent {
    private static final String TAG = RedPackageMessage.class.getSimpleName();
    //自定义的属性
    //private String title;
    private String cash_coupon_id;
    private String extra;
    //private String conversationType;

    /*
     * 实现 encode() 方法，该方法的功能是将消息属性封装成 json 串，
     * 再将 json 串转成 byte 数组，该方法会在发消息时调用，如下面示例代码：
     * */
    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            //jsonObj.put("title", this.getTitle());
            jsonObj.put("cash_coupon_id", this.getCash_coupon_id());
            jsonObj.put("extra", this.getExtra());
            //jsonObj.put("conversationType", this.getConversationType());
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected RedPackageMessage() {
    }


    public static RedPackageMessage obtain(String title, String cash_coupon_id, String conversationType, String extra) {
        RedPackageMessage model = new RedPackageMessage();
        //model.setTitle(title);
        model.setCash_coupon_id(cash_coupon_id);
        model.setExtra(extra);
        //model.setConversationType(conversationType);
        return model;
    }

    /*
    * 覆盖父类的 MessageContent(byte[] data) 构造方法，该方法将对收到的消息进行解析，
    * 先由 byte 转成 json 字符串，再将 json 中内容取出赋值给消息属性。
    * */
    public RedPackageMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            //if (jsonObj.has("title")){
            //    setTitle(jsonObj.optString("title"));
            //}
            if (jsonObj.has("cash_coupon_id")){
                setCash_coupon_id(jsonObj.optString("cash_coupon_id"));
            }

            if (jsonObj.has("extra")){
                setCash_coupon_id(jsonObj.optString("cash_coupon_id"));
            }
            //if (jsonObj.has("conversationType")){
            //    setConversationType(jsonObj.optString("conversationType"));
            //}
        } catch (JSONException e) {
            Log.d("JSONException", e.getMessage());
        }
    }

    //给消息赋值。
    public RedPackageMessage(Parcel in) {
        //这里可继续增加你消息的属性
    //    setTitle(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setCash_coupon_id(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
    //    setConversationType(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setExtra(ParcelUtils.readFromParcel(in));
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<RedPackageMessage> CREATOR = new Creator<RedPackageMessage>() {

        @Override
        public RedPackageMessage createFromParcel(Parcel source) {
            return new RedPackageMessage(source);
        }

        @Override
        public RedPackageMessage[] newArray(int size) {
            return new RedPackageMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 将类的数据写入外部提供的 Parcel 中。
     *
     * @param dest  对象被写入的 Parcel。
     * @param flags 对象如何被写入的附加标志。
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
   //     ParcelUtils.writeToParcel(dest, getTitle());
        ParcelUtils.writeToParcel(dest, getCash_coupon_id());
   //     ParcelUtils.writeToParcel(dest, getConversationType());
        ParcelUtils.writeToParcel(dest, getExtra());
    }

   // public String getTitle() {
   //     return title;
   // }

   // public void setTitle(String title) {
   //     this.title = title;
   // }

   // public String getConversationType() {
   //     return conversationType;
   // }

   // public void setConversationType(String conversationType) {
   //     this.conversationType = conversationType;
   // }


    public String getCash_coupon_id() {
        return cash_coupon_id;
    }

    public void setCash_coupon_id(String cash_coupon_id) {
        this.cash_coupon_id = cash_coupon_id;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}