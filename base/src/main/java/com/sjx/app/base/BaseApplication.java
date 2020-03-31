package com.sjx.app.base;

import android.app.Activity;
import android.app.Application;

import com.sjx.app.base.room.database.AppBaseDatabase;
import com.sjx.app.base.utils.LoggerUtils;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class BaseApplication extends Application {
    public static Application sApplication;
    private static List<Activity> sActivityList;
    public static boolean sDebug;
    private static AppBaseDatabase mAppBaseDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        sActivityList = new ArrayList<>();

        LoggerUtils.init();

        mAppBaseDatabase = Room
                .databaseBuilder(getApplicationContext(), AppBaseDatabase.class, "network_cache")
                .allowMainThreadQueries()//允许在主线程查询
                .build();

    }

    public static void setIsDebug(boolean isDebug) {
        sDebug = isDebug;
    }

    public static AppBaseDatabase getAppBaseDatabase() {
        return mAppBaseDatabase;
    }

    public static void addActivity(Activity activity) {
        sActivityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        sActivityList.remove(activity);
    }

    public static void clearActivityListFinish() {
        clearActivityListFinish(null);
    }

    /**
     * 关闭所有activity
     *
     * @param currentActivity 当前activity,可最后退出
     */
    public static void clearActivityListFinish(Activity currentActivity) {
        for (Activity activity : sActivityList) {
            //保证当前activity最后退出
            if (currentActivity == null || activity != currentActivity) activity.finish();
        }
        //保证当前activity最后退出
        if (currentActivity != null) currentActivity.finish();
        sActivityList.clear();
        sApplication = null;
    }
}
