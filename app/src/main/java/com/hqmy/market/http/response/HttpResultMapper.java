package com.hqmy.market.http.response;

import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.request.BaseRequestModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

/**
 * Created by uqduiba on 11/4/2017.
 */

public final class HttpResultMapper {

    private static final String TAG = HttpResultMapper.class.getSimpleName();

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    public final static class HttpResultData<T> implements Function<HttpResult<T>, T> {

        private final BaseRequestModel httpRequest;

        public HttpResultData(BaseRequestModel httpRequest) {
            this.httpRequest = httpRequest;
        }

        @Override
        public T apply(HttpResult<T> httpResult) {
            LogUtil.i(TAG, "httpResult = " + httpResult.getStatus_code());
            if (httpResult.getStatus_code() != 200 && httpResult.getStatus_code() != 201 && httpResult.getStatus_code() != 204 && httpResult.getStatus_code() != 0) {
                throw new ApiException(httpRequest, httpResult.getStatus_code(), httpResult.getMessage());
            }
            //            if (httpResult.getData() == null) {
            //                throw new ApiException(httpRequest, 200, "没有有效的数据");
            //            }
            return httpResult.getData();
        }
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    public final static class HttpResultBaseData<T> implements Function<HttpResult<T>, T> {

        private final BaseRequestModel httpRequest;

        public HttpResultBaseData(BaseRequestModel httpRequest) {
            this.httpRequest = httpRequest;
        }

        @Override
        public T apply(HttpResult<T> httpResult) {
            LogUtil.i(TAG, "httpResult = " + httpResult.getStatus_code());
            if (httpResult.getStatus_code() != 200 && httpResult.getStatus_code() != 201 && httpResult.getStatus_code() != 204 && httpResult.getStatus_code() != 0) {
                throw new ApiException(httpRequest, httpResult.getStatus_code(), httpResult.getMessage());
            }
            //            if (httpResult.getData() == null) {
            //                throw new ApiException(httpRequest, 200, "没有有效的数据");
            //            }
            return httpResult.getData();
        }
    }

    /**
     * 容忍null值并将null转化为empty列表，避免Observable遇到null发出onError事件
     *
     * @param <T> 列表包含数据的类型
     */
    public final static class HttpResultTolerateNullList<T> implements Function<HttpResult<List<?>>, List<T>> {

        private final BaseRequestModel httpRequest;

        public HttpResultTolerateNullList(BaseRequestModel httpRequest) {
            this.httpRequest = httpRequest;
        }

        @Override
        public List<T> apply(HttpResult<List<?>> listHttpResult) throws ClassCastException {
            if (listHttpResult.getStatus_code() != 200 && listHttpResult.getStatus_code() != 201 && listHttpResult.getStatus_code() != 204 && listHttpResult.getStatus_code() != 0) {
                throw new ApiException(httpRequest, listHttpResult.getStatus_code(), listHttpResult.getMessage());
            }
            if (listHttpResult.getData() == null) {
                return new ArrayList<>();
            }
            try {
                return (List<T>) listHttpResult.getData();
            } catch (ClassCastException ex) {
                throw new ApiException(httpRequest, 200, "返回的数据类型与期望的类型不一致");
            }
        }
    }

    /**
     * 接受其他类型数据返回
     *
     * @param <T> 列表包含数据的类型
     */
    public final static class HttpResultOtheData<T> implements Function<T, T> {

        private final BaseRequestModel httpRequest;

        public HttpResultOtheData(BaseRequestModel httpRequest) {
            this.httpRequest = httpRequest;
        }

        @Override
        public T apply(T listHttpResult) throws ClassCastException {
            try {
                return listHttpResult;
            } catch (ClassCastException ex) {
                throw new ApiException(httpRequest, 200, "返回的数据类型与期望的类型不一致");
            }
        }
    }

    /**
     * 容忍null值并将null转化为empty字符串，避免Observable遇到null发出onError事件
     */
    public final static class HttpResultString implements Function<HttpResult<String>, String> {

        private final BaseRequestModel httpRequest;

        public HttpResultString(BaseRequestModel httpRequest) {
            this.httpRequest = httpRequest;
        }

        @Override
        public String apply(HttpResult<String> httpResult) {
            if (httpResult.getStatus_code() != 200 && httpResult.getStatus_code() != 201 && httpResult.getStatus_code() != 204 && httpResult.getStatus_code() != 0) {
                throw new ApiException(httpRequest, httpResult.getStatus_code(), httpResult.getMessage());
            }
            if (httpResult.getData() == null) {
                return "";
            }
            return httpResult.getData();
        }
    }
}
