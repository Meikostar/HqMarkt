package com.hqmy.market.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rzb on 2019/04/18.
 */

public class RetrofitHelper {

    private static final String         TAG             = RetrofitHelper.class.getSimpleName();
    private static       RetrofitHelper instance        = null;
    private static final int            DEFAULT_TIMEOUT = 5000;
    private              Retrofit       mRetrofit       = null;
    public Gson gson;

    public static RetrofitHelper getInstance() {
        if (instance == null) {
            instance = new RetrofitHelper();
        }
        return instance;
    }

    private RetrofitHelper() {
        init();
    }

    private void init() {
        resetApp();
    }

    private void resetApp() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(new LoggingInterceptor());// 打印日志
        builder.hostnameVerifier((hostname, session) -> true);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        try {
            SSLSocketUtils.ignoreClientCertificate(builder);
        } catch (Exception e) {
            LogUtil.i(TAG, e.toString());
        }
        gson = new GsonBuilder().create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))//请求的结果转为实体类
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    public RetrofitService getServer() {
        return mRetrofit.create(RetrofitService.class);
    }
}
