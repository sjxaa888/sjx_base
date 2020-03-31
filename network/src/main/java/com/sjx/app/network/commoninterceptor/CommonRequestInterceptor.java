package com.sjx.app.network.commoninterceptor;

import com.sjx.app.network.base.INetworkRequiredInfo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CommonRequestInterceptor implements Interceptor {
    private INetworkRequiredInfo requiredInfo;

    public CommonRequestInterceptor(INetworkRequiredInfo requiredInfo) {
        this.requiredInfo = requiredInfo;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder
                .addHeader("os", "android")
                .addHeader("appVersion", requiredInfo.getAppVersionCode())
        ;
        return chain.proceed(builder.build());
    }
}
