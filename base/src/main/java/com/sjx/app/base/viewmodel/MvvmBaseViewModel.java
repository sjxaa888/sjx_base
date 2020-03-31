package com.sjx.app.base.viewmodel;

import androidx.annotation.CallSuper;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class MvvmBaseViewModel extends ViewModel implements LifecycleObserver {
    private CompositeDisposable compositeDisposable;
    public final MutableLiveData<ViewStatus> viewStatusLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public final MutableLiveData<String> skipActivityLiveData = new MutableLiveData<>();
    public boolean isAllGrantedPermission = true;
    //请求失败计数
    protected int requestFailureCount = 0;

    public MvvmBaseViewModel() {
        errorMessage.setValue("");
        viewStatusLiveData.setValue(ViewStatus.LOADING);
        initModel();
    }

    protected abstract void initModel();

    protected abstract void modelCleared();

    public void tryToRefresh() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
        viewStatusLiveData.postValue(viewStatusLiveData.getValue());
    }

    /**
     * 显示请求成功ui的提示
     */
    protected void showResultSuccessUIHint() {
        //ui显示,提示相关
        errorMessage.setValue("");
        viewStatusLiveData.setValue(ViewStatus.SHOW_CONTENT);
    }

    /**
     * 显示请求失败ui的提示
     */
    protected void showResultFailureUIHint(String msg) {
        //ui显示,提示相关
        errorMessage.setValue(msg);
        viewStatusLiveData.setValue(ViewStatus.ERROR);
    }

    @CallSuper
    private void cancel() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    protected void addDisposable(Disposable d) {
        if (d == null) {
            return;
        }

        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(d);
    }

    protected void removeDisposable(Disposable d) {
        if (d == null) {
            return;
        }

        if (!d.isDisposed()) {
            d.dispose();
        }

        if (compositeDisposable == null) {
            return;
        }

        compositeDisposable.remove(d);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        cancel();
        modelCleared();
    }
}
