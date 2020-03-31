package com.sjx.app.network.downloadfile;

import android.app.Application;

import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher;


public class DownloadFileHelper {

    private DownloadFileHelper() {
    }

    private static class Holder {
        private static DownloadFileHelper instance = new DownloadFileHelper();
    }

    public static DownloadFileHelper getInstance() {
        return Holder.instance;
    }

    /**
     * app中初始化
     */
    public void init(Application context) {
        OkDownload.Builder builder = new OkDownload.Builder(context);

        OkDownload.setSingletonInstance(builder.build());

        DownloadDispatcher.setMaxParallelRunningCount(1);
    }

    /**
     * 取消全部下载
     */
    public void cancelAll() {
        OkDownload.with().downloadDispatcher().cancelAll();
    }

}
