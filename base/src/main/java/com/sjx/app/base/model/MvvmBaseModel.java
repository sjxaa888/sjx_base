package com.sjx.app.base.model;

import android.text.TextUtils;
import android.util.Log;

import com.sjx.app.base.BaseApplication;
import com.sjx.app.base.room.Entity.NetworkCache;
import com.sjx.app.base.utils.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import androidx.annotation.CallSuper;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class MvvmBaseModel {
    private CompositeDisposable compositeDisposable;

    @CallSuper
    public void cancel() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void addDisposable(Disposable d) {
        if (d == null) {
            return;
        }

        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(d);
    }

    public void removeDisposable(Disposable d) {
        if (d == null) {
            return;
        }

        if (!d.isDisposed()){
            d.dispose();
        }

        if (compositeDisposable == null) {
            return;
        }

        compositeDisposable.remove(d);
    }

    /**
     * @param data     保存的数据对象
     * @param cacheKey 如果使用本地保存数据,传入非空string,否则传入key
     * @param <T>      泛型
     */
    protected <T> void saveDataToRoom(T data, String cacheKey) {
        if (!TextUtils.isEmpty(cacheKey)) {
            BaseCachedData mData = new BaseCachedData<T>();
            mData.data = data;
            mData.updateTimeInMills = System.currentTimeMillis();
            String dataStr = GsonUtils.toJson(mData);

            NetworkCache networkCache = new NetworkCache();
            networkCache.key = cacheKey;
            networkCache.data = dataStr;

            BaseApplication.getAppBaseDatabase().networkCacheDao().insertAll(networkCache)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            Log.d("MvvmBaseModel", cacheKey + " 数据插入成功");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("MvvmBaseModel", cacheKey + " 数据插入失败:" + (e.getCause() == null ? "" : e.getCause().getMessage()));
                        }
                    });
        }
    }

    public <T> void getCachedData(Type typeOfT, String cacheKey, ICacheDataListener iCacheDataListener) {
        getCachedData(typeOfT, cacheKey, null, iCacheDataListener);
    }

    /**
     * @param typeOfT            保存的数据对象class
     * @param cacheKey           如果使用本地缓存,传入非空string,否则传入key
     * @param apkPredefinedData  如果使用apk预制数据,传入非空string,否则传入json
     * @param iCacheDataListener 返回结果监听
     * @param <T>                保存的数据对象泛型
     */
    public <T> void getCachedData(Type typeOfT, String cacheKey, String apkPredefinedData, ICacheDataListener iCacheDataListener) {
        if (!TextUtils.isEmpty(cacheKey)) {
            BaseApplication.getAppBaseDatabase().networkCacheDao()
                    .findByCacheKey(cacheKey)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<NetworkCache>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(NetworkCache entities) {
                            if (entities != null && !TextUtils.isEmpty(entities.data)) {
                                if (parseData(entities.data)) return;
                            }

                            if (!TextUtils.isEmpty(apkPredefinedData)) {
                                if (parseData(apkPredefinedData)) return;
                            }
                            if (iCacheDataListener != null) {
                                iCacheDataListener.onError(null);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (!TextUtils.isEmpty(apkPredefinedData)) {
                                if (parseData(apkPredefinedData)) return;
                            }
                            if (iCacheDataListener != null) {
                                iCacheDataListener.onError(null);
                            }
                        }

                        /**
                         * 解析数据,如果有正确的数据就返回
                         * @param data 缓存数据
                         * @return 是否解析出非空的正确数据
                         */
                        private boolean parseData(String data) {
                            try {
                                T savedData = GsonUtils.fromLocalJson(new JSONObject(data).getString("data"), typeOfT);
                                if (savedData != null) {
                                    if (iCacheDataListener != null) {
                                        Log.d("MvvmBaseModel", cacheKey + " 使用缓存");
                                        iCacheDataListener.onSuccess(savedData);
                                    }
                                    return true;
                                }
                            } catch (JSONException e) {
//                                    e.printStackTrace();
                            }
                            return false;
                        }
                    });
        } else {
            if (iCacheDataListener != null) {
                iCacheDataListener.onError(null);
            }
        }
    }

    /**
     * 是否更新数据，可以在这里设计策略，可以是一天一次，一月一次等等，
     * 默认是每次请求都更新
     */
    protected boolean isNeedToUpdate() {
        return true;
    }
}
