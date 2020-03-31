package com.sjx.app.base.loadsir;


import com.kingja.loadsir.callback.Callback;
import com.sjx.app.base.R;

public class ErrorCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_error;
    }
}
