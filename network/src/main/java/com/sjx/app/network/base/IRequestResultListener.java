package com.sjx.app.network.base;

public interface IRequestResultListener<T> {
    void onResultSuccess(T t);

    void onResultFailure(String msg);
}
