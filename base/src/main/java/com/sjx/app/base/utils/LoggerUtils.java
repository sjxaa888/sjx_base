package com.sjx.app.base.utils;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;
import com.sjx.app.base.BaseApplication;

public class LoggerUtils {
    public static void init() {
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BaseApplication.sDebug;
            }
        });
    }

    public static void i(String msg) {
        Logger.i(msg);
    }

    public static void json(String json) {
        Logger.json(json);
    }

    public static void xml(String json) {
        Logger.xml(json);
    }

    /**
     * 保存日志到文件
     */
    public static void saveLogsToTheFile() {
        Logger.addLogAdapter(new DiskLogAdapter(CsvFormatStrategy.newBuilder()
                .tag("custom")
                .build()
        ));
    }
}
