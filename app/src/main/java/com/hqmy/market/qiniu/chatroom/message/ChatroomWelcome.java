
package com.hqmy.market.qiniu.chatroom.message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

@MessageTag(value = "RC:InfoNtf", flag = 3)
public class ChatroomWelcome extends MessageContent {
    public ChatroomWelcome() {
    }

    public ChatroomWelcome(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
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
        ParcelUtils.writeToParcel(dest, this.getUserInfo());

    }

    protected ChatroomWelcome(Parcel in) {
        this.setUserInfo((UserInfo) ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    public static final Creator<ChatroomWelcome> CREATOR = new Creator<ChatroomWelcome>() {
        @Override
        public ChatroomWelcome createFromParcel(Parcel source) {
            return new ChatroomWelcome(source);
        }

        @Override
        public ChatroomWelcome[] newArray(int size) {
            return new ChatroomWelcome[size];
        }
    };

}
