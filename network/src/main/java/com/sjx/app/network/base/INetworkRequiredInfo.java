package com.sjx.app.network.base;

import android.app.Application;

public interface INetworkRequiredInfo {

    String getAppVersionName();

    String getAppVersionCode();

    boolean isDebug();

    /**
     * 运行环境
     *
     * @return 环境类型
     */
    String getEnvironment();

    Application getApplicationContext();

    /**
     * 正式环境url
     */
    String getBaseUrl();

    /**
     * 测试环境url
     */
    String getBaseTestUrl();
}
