package com.sjx.app.base.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sjx.app.base.BaseApplication;

public class PreUtils {
    private static String pre_name = "app_pre_data";

    /**
     * 存
     */
    public static boolean put(String key, Object value) {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor et = sp.edit();
        if (value instanceof String) {
            et.putString(key, (String) value);
        } else if (value instanceof Integer) {
            et.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            et.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            et.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            et.putLong(key, (Long) value);
        }
        return et.commit();
    }

    /**
     * 取
     */
    public static String get(String key, String defaultValue) {
        SharedPreferences sp = getSharedPreferences();
        return sp.getString(key, defaultValue);
    }

    public static Integer get(String key, Integer defaultValue) {
        SharedPreferences sp = getSharedPreferences();
        return sp.getInt(key, defaultValue);
    }

    public static Boolean get(String key, Boolean defaultValue) {
        SharedPreferences sp = getSharedPreferences();
        return sp.getBoolean(key, defaultValue);
    }

    public static Float get(String key, Float defaultValue) {
        SharedPreferences sp = getSharedPreferences();
        return sp.getFloat(key, defaultValue);
    }

    public static Long get(String key, Long defaultValue) {
        SharedPreferences sp = getSharedPreferences();
        return sp.getLong(key, defaultValue);
    }

    /**
     * 移除指定key
     */
    public static boolean remove(String remove_key) {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor et = sp.edit();
        et.remove(remove_key);
        return et.commit();
    }

    /**
     * 清空数据
     */
    public static boolean clearAll() {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor et = sp.edit();
        et.clear();
        return et.commit();
    }

    private static SharedPreferences getSharedPreferences() {
        return BaseApplication.sApplication.getSharedPreferences(pre_name, Context.MODE_PRIVATE);
    }

}
