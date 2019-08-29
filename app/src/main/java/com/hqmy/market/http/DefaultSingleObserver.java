package com.hqmy.market.http;



import com.hqmy.market.common.utils.LogUtil;

import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by uqduiba on 10/28/2017.
 */

public class DefaultSingleObserver<T> extends DisposableSingleObserver<T> {


    public DefaultSingleObserver() {
        super();
    }

    @Override
    public void onSuccess(T t) {
        LogUtil.i("RxLog-Thread: onSuccess()", Long.toString(Thread.currentThread().getId()));
    }

    @Override
    public void onError(Throwable throwable) {
        LogUtil.i("RxLog-Thread: onError()", Long.toString(Thread.currentThread().getId()));
    }

}
