package com.sjx.app.network.base;

import com.sjx.app.network.commoninterceptor.CommonRequestInterceptor;
import com.sjx.app.network.commoninterceptor.CommonResponseInterceptor;
import com.sjx.app.network.environment.EnvironmentActivity;
import com.sjx.app.network.errorhandler.HttpErrorHandler;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class NetworkApi {
    protected static INetworkRequiredInfo iNetworkRequiredInfo;
    private static HashMap<String, Retrofit> retrofitHashMap = new HashMap<>();
    protected String mBaseUrl;
    private OkHttpClient mOkHttpClient;
    private static String mEnvironment = "";
    private static final String TEST_URL_TYPE = "test";
    private static final String RELEASE_URL_TYPE = "release";

    public NetworkApi() {
        switch (mEnvironment) {
            case TEST_URL_TYPE:
                mBaseUrl = iNetworkRequiredInfo.getBaseTestUrl();
                break;
            case RELEASE_URL_TYPE:
            default:
                mBaseUrl = iNetworkRequiredInfo.getBaseUrl();
                break;
        }
    }

    public static void init(INetworkRequiredInfo networkRequiredInfo) {
        iNetworkRequiredInfo = networkRequiredInfo;
        if (iNetworkRequiredInfo.isDebug()) {
            mEnvironment = EnvironmentActivity.isOfficialEnvironment(networkRequiredInfo.getApplicationContext());
        } else {
            mEnvironment = iNetworkRequiredInfo.getEnvironment();
        }
    }

    protected Retrofit getRetrofit(Class service) {
        return getRetrofit(mBaseUrl, service);
    }

    protected Retrofit getRetrofit(String httpUrl, Class service) {
        if (retrofitHashMap.get(httpUrl + service.getName()) != null) {
            return retrofitHashMap.get(httpUrl + service.getName());
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitHashMap.put(httpUrl + service.getName(), retrofit);
        return retrofit;
    }

    private OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(iNetworkRequiredInfo.isDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (getInterceptor() != null) {
                //每个环境特有的拦截器
                builder.addInterceptor(getInterceptor());
            }
            mOkHttpClient = builder
                    .addInterceptor(interceptor)    //日志
                    //链接超时
                    .connectTimeout(getTimeout(), TimeUnit.SECONDS)
                    //读取超时
                    .readTimeout(getTimeout(), TimeUnit.SECONDS)
                    //缓存
//                .cache(new Cache(context.getExternalFilesDir("http_cache"), 10 << 20))
                    //添加请求拦截器
                    .addInterceptor(new CommonRequestInterceptor(iNetworkRequiredInfo))
                    .addInterceptor(new CommonResponseInterceptor())
                    //添加Cookie拦截器
//                .addInterceptor(new SaveCookieInterceptor())
//                .addInterceptor(new LoadCookieInterceptor())
                    //添加缓存拦截器
//                .addInterceptor(new RequestInterceptor())//无网
//                .addNetworkInterceptor(new CacheInterceptor())//有网
                    //设置dns域名解析
//                .dns(new TestDns())
                    .build();
        }
        return mOkHttpClient;
    }

    public <T> ObservableTransformer<T, T> applySchedulers(final Observer<T> observer) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                Observable<T> observable = upstream.subscribeOn(Schedulers.io());
                observable.observeOn(AndroidSchedulers.mainThread())
                        .map(getAppErrorHandler())
                        .onErrorResumeNext(new HttpErrorHandler<T>())
                        .subscribe(observer);
                return observable;
            }
        };
    }

    protected abstract Interceptor getInterceptor();

    protected abstract <T> Function<T, T> getAppErrorHandler();

    protected int getTimeout() {
        return 5;
    }
}
