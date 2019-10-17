
package com.hqmy.market.qiniu.chatroom.message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

@MessageTag(value = "RC:closeuser", flag = 3)
public class ChatroomUser extends MessageContent {
    public ChatroomUser() {
    }

    public ChatroomUser(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("count")) {
                count = jsonObj.optInt("count");
            }
            if (jsonObj.has("extra")){
                extra = jsonObj.optString("extra");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("count", count);
            jsonObj.put("extra", extra);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, count);
        ParcelUtils.writeToParcel(dest, extra);
    }

    protected ChatroomUser(Parcel in) {
        count = ParcelUtils.readIntFromParcel(in);
        extra = ParcelUtils.readFromParcel(in);
    }

    public static final Creator<ChatroomUser> CREATOR = new Creator<ChatroomUser>() {
        @Override
        public ChatroomUser createFromParcel(Parcel source) {
            return new ChatroomUser(source);
        }

        @Override
        public ChatroomUser[] newArray(int size) {
            return new ChatroomUser[size];
        }
    };

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private String extra;
    public void setExtra(   String  extra) {
        this.extra = extra;
    }
    public String getExtra() {
        return extra;
    }
}
