package com.sjx.app.base.utils;

import android.widget.Toast;

import com.sjx.app.base.BaseApplication;

/**
 * 单例Toast
 */
public class ToastUtils {

    private static Toast toast = null;

    public static void show(String s) {
        if (toast == null)
            toast = Toast.makeText(BaseApplication.sApplication, s, Toast.LENGTH_SHORT);
        toast.setText(s);
        toast.show();
    }

    public static void show(int resId) {
        show(BaseApplication.sApplication.getString(resId));
    }

}
