package com.sjx.app.network.errorhandler;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class HttpErrorHandler<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        return Observable.error(ExceptionHandler.handleException(throwable));
    }
}
