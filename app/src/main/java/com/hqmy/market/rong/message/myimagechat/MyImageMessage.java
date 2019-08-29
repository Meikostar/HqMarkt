package com.hqmy.market.rong.message.myimagechat;

/**
 * 自定义图片消息
 * Created by rzb on 2019/4/28.
 */
import android.net.Uri;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.DestructionTag;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.UserInfo;
import io.rong.message.MediaMessageContent;

@MessageTag(
        value = "app:MyImageMessage",
        flag = 3,
        messageHandler = MyImageMessageHandler.class
)
@DestructionTag(
        destructionFlag = 0
)
public class MyImageMessage extends MediaMessageContent {
    private Uri mThumUri;
    private boolean mUpLoadExp = false;
    private String mBase64;
    boolean mIsFull;
    public static final Creator<MyImageMessage> CREATOR = new Creator<MyImageMessage>() {
        public MyImageMessage createFromParcel(Parcel source) {
            return new MyImageMessage(source);
        }

        public MyImageMessage[] newArray(int size) {
            return new MyImageMessage[size];
        }
    };

    public MyImageMessage(byte[] data) {
        String jsonStr = new String(data);

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if(jsonObj.has("imageUri")) {
                String uri = jsonObj.optString("imageUri");
                if(!TextUtils.isEmpty(uri)) {
                    this.setRemoteUri(Uri.parse(uri));
                }
            }

            if(jsonObj.has("localPath")) {
                this.setLocalPath(Uri.parse(jsonObj.optString("localPath")));
            }

            if(jsonObj.has("content")) {
                this.setBase64(jsonObj.optString("content"));
            }

            if(jsonObj.has("extra")) {
                this.setExtra(jsonObj.optString("extra"));
            }

            if(jsonObj.has("exp")) {
                this.setUpLoadExp(true);
            }

            if(jsonObj.has("isFull")) {
                this.setIsFull(jsonObj.optBoolean("isFull"));
            } else if(jsonObj.has("full")) {
                this.setIsFull(jsonObj.optBoolean("full"));
            }

            if(jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }

            if(jsonObj.has("isBurnAfterRead")) {
                this.setDestruct(jsonObj.getBoolean("isBurnAfterRead"));
            }

            if(jsonObj.has("burnDuration")) {
                this.setDestructTime(jsonObj.getLong("burnDuration"));
            }
        } catch (JSONException var5) {
            Log.e("JSONException", var5.getMessage());
        }

    }

    public MyImageMessage() {
    }

    private MyImageMessage(Uri thumbUri, Uri localUri) {
        this.mThumUri = thumbUri;
        this.setLocalPath(localUri);
    }

    private MyImageMessage(Uri thumbUri, Uri localUri, boolean original) {
        this.mThumUri = thumbUri;
        this.setLocalPath(localUri);
        this.mIsFull = original;
    }

    public static MyImageMessage obtain(Uri thumUri, Uri localUri) {
        return new MyImageMessage(thumUri, localUri);
    }

    /**
     * @param thumUri
     * @param localUri
     * @param isFull 是否发送原图
     * @return
     */
    public static MyImageMessage obtain(Uri thumUri, Uri localUri, boolean isFull) {
        return new MyImageMessage(thumUri, localUri, isFull);
    }

    public static MyImageMessage obtain() {
        return new MyImageMessage();
    }

    public Uri getThumUri() {
        return this.mThumUri;
    }

    public boolean isFull() {
        return this.mIsFull;
    }

    public void setIsFull(boolean isFull) {
        this.mIsFull = isFull;
    }

    public void setThumUri(Uri thumUri) {
        this.mThumUri = thumUri;
    }

    public Uri getLocalUri() {
        return this.getLocalPath();
    }

    public void setLocalUri(Uri localUri) {
        this.setLocalPath(localUri);
    }

    public Uri getRemoteUri() {
        return this.getMediaUrl();
    }

    public void setRemoteUri(Uri remoteUri) {
        this.setMediaUrl(remoteUri);
    }

    public void setBase64(String base64) {
        this.mBase64 = base64;
    }

    public String getBase64() {
        return this.mBase64;
    }

    public boolean isUpLoadExp() {
        return this.mUpLoadExp;
    }

    public void setUpLoadExp(boolean upLoadExp) {
        this.mUpLoadExp = upLoadExp;
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            if(!TextUtils.isEmpty(this.mBase64)) {
                jsonObj.put("content", this.mBase64);
            } else {
                RLog.d("MyImageMessage", "缩略图为空，请检查构造图片消息的地址");
            }

            if(this.getMediaUrl() != null) {
                jsonObj.put("imageUri", this.getMediaUrl().toString());
            }

            if(this.getLocalUri() != null) {
                jsonObj.put("localPath", this.getLocalUri().toString());
            }

            if(this.mUpLoadExp) {
                jsonObj.put("exp", true);
            }

            jsonObj.put("isFull", this.mIsFull);
            if(!TextUtils.isEmpty(this.getExtra())) {
                jsonObj.put("extra", this.getExtra());
            }

            if(this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }

            jsonObj.put("isBurnAfterRead", this.isDestruct());
            jsonObj.put("burnDuration", this.getDestructTime());
        } catch (JSONException var3) {
            RLog.e("JSONException", var3.getMessage());
        }

        this.mBase64 = null;
        return jsonObj.toString().getBytes();
    }

    public int describeContents() {
        return 0;
    }

    public MyImageMessage(Parcel in) {
        this.setExtra(ParcelUtils.readFromParcel(in));
        this.setLocalPath((Uri) ParcelUtils.readFromParcel(in, Uri.class));
        this.setMediaUrl((Uri) ParcelUtils.readFromParcel(in, Uri.class));
        this.mThumUri = (Uri) ParcelUtils.readFromParcel(in, Uri.class);
        this.setUserInfo((UserInfo) ParcelUtils.readFromParcel(in, UserInfo.class));
        this.mIsFull = ParcelUtils.readIntFromParcel(in).intValue() == 1;
        this.setDestruct(ParcelUtils.readIntFromParcel(in).intValue() == 1);
        this.setDestructTime(ParcelUtils.readLongFromParcel(in).longValue());
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getExtra());
        ParcelUtils.writeToParcel(dest, this.getLocalPath());
        ParcelUtils.writeToParcel(dest, this.getMediaUrl());
        ParcelUtils.writeToParcel(dest, this.mThumUri);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
        ParcelUtils.writeToParcel(dest, Integer.valueOf(this.mIsFull?1:0));
        ParcelUtils.writeToParcel(dest, Integer.valueOf(this.isDestruct()?1:0));
        ParcelUtils.writeToParcel(dest, Long.valueOf(this.getDestructTime()));
    }
}
