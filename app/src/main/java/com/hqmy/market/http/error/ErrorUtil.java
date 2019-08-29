package com.hqmy.market.http.error;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class ErrorUtil {

    /*private static ErrorUtil INSTANCE;

    public static ErrorUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ErrorUtil();
        }

        return INSTANCE;
    }

    public String getErrorMessage(Throwable throwable){
       String message = null;
        if(throwable instanceof HttpException){
            ResponseBody body = ((HttpException) throwable).response().errorBody();
            if(null != body){
                try {
                    String json = body.string();
                    JSONObject jsonObject = JSON.parseObject(json);
                    message = jsonObject.getString("err_msg");
                    String err_code = jsonObject.getString("err_code");
                    Object request_id = jsonObject.getString("request_id");
                    String err_type = jsonObject.getString("err_type");
                } catch (IOException e) {
                    e.printStackTrace();
                    message = e.getMessage();
                }catch (JSONException e){
                    e.printStackTrace();
                    message = e.getMessage();
                }
            }
        }else {
            message = throwable.getMessage();
        }
       return message;
    }

    public String getErroCode(Throwable throwable){
        String err_code = null;
        if(throwable instanceof HttpException){
            ResponseBody body = ((HttpException) throwable).response().errorBody();
            if(null != body){
                try {
                    String json = body.string();
                    JSONObject jsonObject = JSON.parseObject(json);
                    String message = jsonObject.getString("err_msg");
                    err_code = jsonObject.getString("err_code");
                    Object request_id = jsonObject.getString("request_id");
                    String err_type = jsonObject.getString("err_type");
                } catch (IOException e) {
                    e.printStackTrace();
                    err_code = e.getMessage();
                }catch (JsonIOException e){
                    e.printStackTrace();
                    err_code = e.getMessage();
                }
            }
        }else {
            err_code = throwable.getMessage();
        }
        return err_code;
    }
*/

    private static ErrorUtil INSTANCE;

    public static ErrorUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ErrorUtil();
        }

        return INSTANCE;
    }

    public String getErrorMessage(Throwable throwable){
        String message = null;
        if(throwable instanceof HttpException){
            ResponseBody body = ((HttpException) throwable).response().errorBody();
            if(null != body){
                try {
                    String json = body.string();
                    JSONObject jsonObject = JSON.parseObject(json);
                    if(null != jsonObject.getString("errors")){
                        JSONObject errorsObject= JSON.parseObject(jsonObject.getString("errors"));
                        String[] messageString = analyzeJsonToArray(errorsObject,"value");
                        if(null != messageString){
                            if(messageString[0].contains("]") ){
                                message = messageString[0].replace("]","");
                            }else{
                                message = messageString[0];
                            }
                            if(message.contains("[")){
                                message = message.replace("[","");
                            }
                        }
                    }else{
                        message = (String) jsonObject.get("message");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    message = e.getMessage();
                }
            }
        }else {
            message = throwable.getMessage();
        }
        return message;
    }


    /**
     * 将json键值对分别解析到数组中
     *
     * @param jsonject
     *            需要解析的json对象
     * @param type
     *            决定返回值的内容：键或值
     * @return type="key"：返回json对象中"键"的字符串， type="key""value":返回json对象中"值"的字符串
     */
    private static String[] analyzeJsonToArray(JSONObject jsonject, String type) {

        String string = jsonject.toString();
        string = string.replace("}", "");
        string = string.replace("{", "");
        string = string.replace("\"", "");
        String[] strings = string.split(",");

        if (type.equals("key")) {
            String[] stringsNum = new String[strings.length];
            for (int i = 0; i < strings.length; i++) {
                stringsNum[i] = strings[i].split(":")[0];
            }
            return stringsNum;
        } else if (type.equals("value")) {
            String[] stringsName = new String[strings.length];
            for (int i = 0; i < strings.length; i++) {
                stringsName[i] = strings[i].split(":")[1];
            }
            return stringsName;
        } else {
            return null;
        }
    }


}
