
package com.hqmy.market.qiniu.chatroom.message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

@MessageTag(value = "RC:Chatroom:Gift", flag = 3)
public class ChatRoomGift extends MessageContent {
    public ChatRoomGift() {
    }

    public ChatRoomGift(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("type")) {
                type = jsonObj.optInt("type");
            }

            if (jsonObj.has("content")) {
                content = jsonObj.optString("content");
            }
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
            jsonObj.put("type", type);
            jsonObj.put("content", content);
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
        ParcelUtils.writeToParcel(dest, type);
        ParcelUtils.writeToParcel(dest, content);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    protected ChatRoomGift(Parcel in) {
        type = ParcelUtils.readIntFromParcel(in);
        content = ParcelUtils.readFromParcel(in);
        this.setUserInfo((UserInfo) ParcelUtils.readFromParcel(in, UserInfo.class));

    }

    public static final Creator<ChatRoomGift> CREATOR = new Creator<ChatRoomGift>() {
        @Override
        public ChatRoomGift createFromParcel(Parcel source) {
            return new ChatRoomGift(source);
        }

        @Override
        public ChatRoomGift[] newArray(int size) {
            return new ChatRoomGift[size];
        }
    };
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
