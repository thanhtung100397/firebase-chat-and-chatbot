package com.ttt.chat_module.services.google_api;

import com.ttt.chat_module.exception.NoInternetConnectionException;

import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by TranThanhTung on 18/03/2018.
 */

public abstract class ResponseObserver<T> extends DisposableObserver<T> {

    public abstract void onSuccess(T response);

    public abstract void onServerError(String message);

    public abstract void onNetworkConnectionError();

    public void onTimeOut(String message) {
        onServerError(message);
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        if(e instanceof NoInternetConnectionException) {
            onNetworkConnectionError();
        } else if(e instanceof SocketTimeoutException){
            onTimeOut(e.getMessage());
        } else {
            onServerError(e.getMessage());
        }
    }

    @Override
    public void onComplete() {

    }
}
