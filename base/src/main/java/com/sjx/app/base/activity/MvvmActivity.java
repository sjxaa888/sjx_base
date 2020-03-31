package com.sjx.app.base.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.sjx.app.base.BaseApplication;
import com.sjx.app.base.R;
import com.sjx.app.base.loadsir.EmptyCallback;
import com.sjx.app.base.loadsir.ErrorCallback;
import com.sjx.app.base.loadsir.LoadingCallback;
import com.sjx.app.base.utils.ToastUtils;
import com.sjx.app.base.viewmodel.MvvmBaseViewModel;
import com.sjx.app.base.viewmodel.ViewStatus;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

public abstract class MvvmActivity<V extends ViewDataBinding, VM extends MvvmBaseViewModel> extends AppCompatActivity implements Observer {
    protected Activity mActivity;
    protected V viewDataBinding;
    protected VM viewModel;
    private LoadService mLoadService;
    protected int requestFailureCount = 0;

    protected abstract @LayoutRes
    int getLayoutId();

    protected abstract VM getViewModel();

    public abstract int getBindingVariable();

    public abstract void onRetryBtnClick();

    protected String getActivityTag() {
        return this.getClass().getSimpleName();
    }

    /**
     * 初始化参数
     */
    protected void initParameters() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        BaseApplication.addActivity(this);
        initParameters();
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        viewModel = getViewModel();
        getLifecycle().addObserver(viewModel);
        viewModel.viewStatusLiveData.observe(this, this);
        viewModel.errorMessage.observe(this, this);
        viewModel.skipActivityLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                skipActivity(s);
            }
        });
        initLifecycleAddObserver();
        if (getBindingVariable() > 0) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
            viewDataBinding.executePendingBindings();
        }
        init();
    }

    protected abstract void initLifecycleAddObserver();

    protected abstract void init();

    protected abstract void skipActivity(String s);

    public void setLoadSir(View view) {
        mLoadService = LoadSir.getDefault().register(view, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                onRetryBtnClick();
            }
        });
    }

    @Override
    public void onChanged(Object o) {
        if (o instanceof ViewStatus) {
            switch ((ViewStatus) o) {
                case LOADING:
                    showLoading();
                    break;
                case EMPTY:
                    showEmpty();
                    break;
                case SHOW_CONTENT:
                    showSuccess();
                    break;
                case ERROR:
                case REFRESH_ERROR:
                    showError();
                    if (viewModel.errorMessage.getValue() != null) {
                        ToastUtils.show(viewModel.errorMessage.getValue());
                    }
                    break;
                case NO_MORE_DATA:
                    ToastUtils.show(getString(R.string.no_more_data));
                    break;
                case LOAD_MORE_FAILED:
                    if (viewModel.errorMessage.getValue() != null)
                        ToastUtils.show(viewModel.errorMessage.getValue());
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(getActivityTag(), "Activity:" + this + ": " + "onStop");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getActivityTag(), "Activity:" + this + ": " + "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(getActivityTag(), "Activity:" + this + ": " + "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getActivityTag(), "Activity:" + this + ": " + "onResume");
    }

    @Override
    public void onDestroy() {
        BaseApplication.removeActivity(this);
        super.onDestroy();
        Log.d(getActivityTag(), "Activity:" + this + ": " + "onDestroy");
    }

    protected void showSuccess() {
        if (mLoadService != null) {
            mLoadService.showSuccess();
        }
    }

    protected void showLoading() {
        if (mLoadService != null) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }

    protected void showEmpty() {
        if (mLoadService != null) {
            mLoadService.showCallback(EmptyCallback.class);
        }
    }

    protected void showError() {
        if (mLoadService != null) {
            mLoadService.showCallback(ErrorCallback.class);
        }
    }

    /**
     * 权限检查
     *
     * @param neededPermissions 需要的权限
     * @return 是否全部被允许
     */
    protected boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isAllGranted = true;
        for (int grantResult : grantResults) {
            isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
        }
        if (viewModel != null) viewModel.isAllGrantedPermission = isAllGranted;
        afterRequestPermission(requestCode, isAllGranted);
    }

    /**
     * 请求权限的回调
     *
     * @param requestCode  请求码
     * @param isAllGranted 是否全部被同意
     */
    public void afterRequestPermission(int requestCode, boolean isAllGranted) {
    }
}
