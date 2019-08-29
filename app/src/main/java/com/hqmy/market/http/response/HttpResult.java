package com.hqmy.market.http.response;


import com.hqmy.market.bean.MetaDto;

/**
 * 通用请求返回结果外层实体
 * Created by liukun on 16/3/5.
 */
public class HttpResult<T> {
    private String message;
    private Object errors;
    private int status_code;
    private Object debug;
    private T data;
    private T live_apply;
    private T live_video;
    MetaDto meta;



    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getLive_video() {
        return live_video;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public Object getDebug() {
        return debug;
    }

    public void setDebug(Object debug) {
        this.debug = debug;
    }

    public MetaDto getMeta() {
        return meta;
    }

    public void setMeta(MetaDto meta) {
        this.meta = meta;
    }

    public T getLive_apply() {
        return live_apply;
    }
}
