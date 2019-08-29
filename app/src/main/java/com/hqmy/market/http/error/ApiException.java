package com.hqmy.market.http.error;


import android.text.TextUtils;

import com.hqmy.market.http.request.BaseRequestModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;


/**
 * Created by uqduiba on 17/11/03.
 */
public class ApiException extends RuntimeException {

    public static final int WRONG_0 = 0; //成功
    public static final int WRONG_200 = 200; //成功
    public static final int WRONG_500 = 500; //程序出现错误

    public static final int WRONG_201 = 201; //成功创建
    public static final int WRONG_204 = 204; //执行成功，没有返回内容

    public static final int WRONG_401 = 401; //没有登陆
    public static final int WRONG_403 = 403; //没有权限
    public static final int WRONG_404 = 404; //要操作的资源不存在
    public static final int WRONG_405 = 405; //操作方法不允许
    public static final int WRONG_422 = 422; //参数验证错误

    private static int              code;
    private        String           errorMsg;
    private        BaseRequestModel httpRequest;
    private static ApiException instance = null;

    public ApiException(BaseRequestModel httpRequest, int resultCode, String rawErrorMsg) {
        this.httpRequest = httpRequest;
        this.code = resultCode;
        this.errorMsg = rawErrorMsg;
    }

    public ApiException() {
    }

    public static ApiException getInstance() {
        if (instance == null) {
            instance = new ApiException();
        }
        return instance;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccess() {
        if (getCode() == WRONG_0 || getCode() == WRONG_200 || getCode() == WRONG_201 || getCode() == WRONG_204) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 开发者视角的api错误消息内容
     *
     * @return
     */
    public String getErrorMsg() {
        return errorMsg;
//        return getApiExceptionMessage(code);
    }

    public BaseRequestModel getHttpRequest() {
        return httpRequest;
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @return
     */
    private String getApiExceptionMessage(int code) {
        String message;
        switch (code) {
            case WRONG_500:
                message = "程序出现错误";
                break;
            default:
                message = errorMsg;
                break;
        }
        return message;
    }

    public static String getShowToast(Response<ResponseBody> response) {
        String message = "";
        try {
            if (response != null) {
                String jsonStr = new String(response.errorBody().bytes());//把原始数据转为字符串
                JSONObject jsonObject = new JSONObject(jsonStr);
                if (jsonObject.has("err_msg")) {
                    message = jsonObject.getString("err_msg");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (TextUtils.isEmpty(message)) {
                message = "失败";
            }
        }
        return message;
    }

    public static String getHttpExceptionMessage(Throwable throwable) {

        String message = "";
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            int code = httpException.code();
            switch (code) {
                case WRONG_403:
                    message = "没有权限";
                    break;
                case WRONG_405:
                    message = "操作方法不允许";
                    break;
                case WRONG_401:
                case WRONG_500:
                case WRONG_422:
                case WRONG_404:
                    message = ErrorUtil.getInstance().getErrorMessage(throwable);
                    break;

            }
        }
        if (TextUtils.isEmpty(message)) {
            message = throwable.getMessage();
        }
        return message;
    }
}

