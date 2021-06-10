package com.sjx.app.network.observer;


import com.sjx.app.network.errorhandler.ExceptionHandler;

import io.reactivex.observers.DisposableObserver;

public abstract class BaseObserver<T> extends DisposableObserver<T> {


    @Override
    public void onNext(T t) {
        onSuccess(t);
        onComplete();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ExceptionHandler.ResponeThrowable) {
            onFailure((ExceptionHandler.ResponeThrowable) e);
        }
        onComplete();
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(ExceptionHandler.ResponeThrowable e);
}
