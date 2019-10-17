
package com.hqmy.market.qiniu.chatroom.message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

@MessageTag(value = "RC:Chatroom:End ", flag = 3)
public class ChatroomEnd extends MessageContent {
  public ChatroomEnd() {
  }
  public ChatroomEnd(byte[] data) {
    String jsonStr = null;
    try {
        jsonStr = new String(data, "UTF-8");
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    try {
        JSONObject jsonObj = new JSONObject(jsonStr);
        
          if (jsonObj.has("duration")){
            duration = jsonObj.optInt("duration");
          }
        
          if (jsonObj.has("extra")){
            extra = jsonObj.optString("extra");
          }
        if (jsonObj.has("name")) {
            name = jsonObj.optString("name");
        }
        if (jsonObj.has("url")) {
            url = jsonObj.optString("url");
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
  }
  @Override
  public byte[] encode() {
    JSONObject jsonObj = new JSONObject();
    try {
        
            jsonObj.put("duration", duration);
        
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
    
      
         ParcelUtils.writeToParcel(dest, duration);
      
    
      
         ParcelUtils.writeToParcel(dest, extra);
      ParcelUtils.writeToParcel(dest, name);
      ParcelUtils.writeToParcel(dest, url);
    
  }
  protected ChatroomEnd(Parcel in) {
    
      
        
          duration = ParcelUtils.readIntFromParcel(in);
        
      
    
      
        extra = ParcelUtils.readFromParcel(in);

      name = ParcelUtils.readFromParcel(in);
      url = ParcelUtils.readFromParcel(in);
  }
  public static final Creator<ChatroomEnd> CREATOR = new Creator<ChatroomEnd>() {
    @Override
    public ChatroomEnd createFromParcel(Parcel source) {
        return new ChatroomEnd(source);
    }
    @Override
    public ChatroomEnd[] newArray(int size) {
        return new ChatroomEnd[size];
    }
  };
  
    private int duration;
    public void setDuration( int    duration) {
        this.duration = duration;
    }
    public  int getDuration() {
      return duration;
    }
  
    private String extra;
    public void setExtra(   String  extra) {
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
