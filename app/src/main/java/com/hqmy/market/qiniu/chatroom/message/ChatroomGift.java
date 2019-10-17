
package com.hqmy.market.qiniu.chatroom.message;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

@MessageTag(value = "RC:Chatroom:Gift", flag = 3)
public class ChatroomGift extends MessageContent {
    public ChatroomGift() {
    }

    public ChatroomGift(byte[] data) {
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

            if (jsonObj.has("number")) {
                number = jsonObj.optInt("number");
            }

            if (jsonObj.has("type")) {
                type = jsonObj.optInt("type");
            }

            if (jsonObj.has("total")) {
                total = jsonObj.optInt("total");
            }

            if (jsonObj.has("extra")) {
                extra = jsonObj.optString("extra");
            }
            if (jsonObj.has("content")) {
                content = jsonObj.optString("content");
            }
            if (jsonObj.has("name")) {
                name = jsonObj.optString("name");
            }
            if (jsonObj.has("url")) {
                url = jsonObj.optString("url");
            }
            if (jsonObj.has("gift_name")) {
                gift_name = jsonObj.optString("gift_name");
            }
            if (jsonObj.has("giftURL")) {
                giftURL = jsonObj.optString("giftURL");
            }
            if (jsonObj.has("g_id")) {
                g_id = jsonObj.optString("g_id");
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
            jsonObj.put("number", number);
            jsonObj.put("type", type);
            jsonObj.put("total", total);
            jsonObj.put("extra", extra);
            jsonObj.put("content", content);
            jsonObj.put("name", name);
            jsonObj.put("url", url);
            jsonObj.put("gift_name", gift_name);
            jsonObj.put("giftURL", giftURL);
            jsonObj.put("g_id", g_id);
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
        ParcelUtils.writeToParcel(dest, number);
        ParcelUtils.writeToParcel(dest, type);
        ParcelUtils.writeToParcel(dest, total);
        ParcelUtils.writeToParcel(dest, extra);
        ParcelUtils.writeToParcel(dest, content);
        ParcelUtils.writeToParcel(dest, name);
        ParcelUtils.writeToParcel(dest, url);
        ParcelUtils.writeToParcel(dest, gift_name);
        ParcelUtils.writeToParcel(dest, giftURL);
        ParcelUtils.writeToParcel(dest, g_id);
    }

    protected ChatroomGift(Parcel in) {
        id = ParcelUtils.readFromParcel(in);
        number = ParcelUtils.readIntFromParcel(in);
        total = ParcelUtils.readIntFromParcel(in);
        extra = ParcelUtils.readFromParcel(in);
        content = ParcelUtils.readFromParcel(in);
        name = ParcelUtils.readFromParcel(in);
        url = ParcelUtils.readFromParcel(in);
        gift_name = ParcelUtils.readFromParcel(in);
        g_id = ParcelUtils.readFromParcel(in);
        giftURL = ParcelUtils.readFromParcel(in);
    }

    public static final Parcelable.Creator<ChatroomGift> CREATOR = new Parcelable.Creator<ChatroomGift>() {
        @Override
        public ChatroomGift createFromParcel(Parcel source) {
            return new ChatroomGift(source);
        }

        @Override
        public ChatroomGift[] newArray(int size) {
            return new ChatroomGift[size];
        }
    };

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    private int number;

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    private int total;

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    private String extra;

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getExtra() {
        return extra;
    }

    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    private String gift_name;
    private String giftURL;
    private String g_id;

    public String getGift_name() {
        return gift_name;
    }

    public String getGiftURL() {
        return giftURL;
    }

    public String getG_id() {
        return g_id;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public void setGiftURL(String giftURL) {
        this.giftURL = giftURL;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }
}
