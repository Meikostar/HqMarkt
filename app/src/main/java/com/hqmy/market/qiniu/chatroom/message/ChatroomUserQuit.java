
package com.hqmy.market.qiniu.chatroom.message;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

@MessageTag(value = "RC:Chatroom:User:Quit", flag = 3)
public class ChatroomUserQuit extends MessageContent {
    public ChatroomUserQuit() {
    }

    public ChatroomUserQuit(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("id")) {
                id = jsonObj.optString("id");
            }
            if (jsonObj.has("name")) {
                name = jsonObj.optString("name");
            }
            if (jsonObj.has("url")) {
                url = jsonObj.optString("url");
            }
            if (jsonObj.has("extra")) {
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

            jsonObj.put("id", id);

            jsonObj.put("extra", extra);
            jsonObj.put("name", name);
            jsonObj.put("url", url);
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


        ParcelUtils.writeToParcel(dest, id);


        ParcelUtils.writeToParcel(dest, extra);
        ParcelUtils.writeToParcel(dest, name);
        ParcelUtils.writeToParcel(dest, url);

    }

    protected ChatroomUserQuit(Parcel in) {


        id = ParcelUtils.readFromParcel(in);


        extra = ParcelUtils.readFromParcel(in);
        name = ParcelUtils.readFromParcel(in);
        url = ParcelUtils.readFromParcel(in);

    }

    public static final Parcelable.Creator<ChatroomUserQuit> CREATOR = new Parcelable.Creator<ChatroomUserQuit>() {
        @Override
        public ChatroomUserQuit createFromParcel(Parcel source) {
            return new ChatroomUserQuit(source);
        }

        @Override
        public ChatroomUserQuit[] newArray(int size) {
            return new ChatroomUserQuit[size];
        }
    };

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    private String extra;

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getExtra() {
        return extra;
    }

    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
