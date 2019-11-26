package com.hqmy.market.bean;

import java.io.Serializable;

/**
 * Created by rzb on 2019/7/10.
 */
public class NoticeDetailDto implements Serializable {
    private String send_user_id;
    public String content;
    public String title;
    private String opt_type;
    private String msg_type;
    public String type;
    public String subject;
    public String id;
    public String img;

    public NoticeDetailDto data;

    public String getSend_user_id() {
        return send_user_id;
    }

    public void setSend_user_id(String send_user_id) {
        this.send_user_id = send_user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOpt_type() {
        return opt_type;
    }

    public void setOpt_type(String opt_type) {
        this.opt_type = opt_type;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }
}
