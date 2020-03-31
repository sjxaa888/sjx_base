package com.sjx.app.network.downloadfile;

import com.liulishuo.okdownload.DownloadTask;

import java.util.List;

public class DownloadFileModel {
    private DownloadTask[] mMultiTasks;

    /**
     * 开启单任务下载
     *
     * @param listener 下载监听,传null使用默认
     */
    public void startSingleDownloadFile(String url, DownloadFileListener listener, String savePath) {
        //创建Task 队列
        mMultiTasks = new DownloadTask[1];
        mMultiTasks[0] = new DownloadTask
                .Builder(url, savePath, DownloadFileUtils.getFileNameFromUrl(url))
                // 下载进度回调的间隔时间（毫秒）
                .setMinIntervalMillisCallbackProcess(30)
                // 任务过去已完成是否要重新下载
                .setPassIfAlreadyCompleted(false)
                .build();
        //初始化监听数据
        listener.init(1);
        DownloadTask.enqueue(mMultiTasks, listener);
    }

    /**
     * 开始多任务下载
     *
     * @param urls     下载连接集合(string 集合)
     * @param listener 下载监听,传null使用默认
     * @param savePath 下载文件的存储绝对路径
     */
    public void startStrMultiDownloadFile(List<String> urls, DownloadFileListener listener, String savePath) {
        //创建Task 队列
        mMultiTasks = new DownloadTask[urls.size()];
        for (int i = 0; i < urls.size(); i++) {
            mMultiTasks[i] = new DownloadTask
                    .Builder(urls.get(i), savePath, DownloadFileUtils.getFileNameFromUrl(urls.get(i)))
                    // 任务过去已完成是否要重新下载
                    .setPassIfAlreadyCompleted(false)
                    .build();
        }
        listener.init(urls.size());
        DownloadTask.enqueue(mMultiTasks, listener);
    }

    /**
     * 开始多任务下载
     *
     * @param urls     下载连接集合 (需要实现IDownloadUrl 集合)
     * @param listener 下载监听,传null使用默认
     * @param savePath 下载文件的存储绝对路径
     */
    public <T extends IDownloadUrl> void startImportMultiDownloadFile(List<T> urls, DownloadFileListener listener, String savePath) {
        //创建Task 队列
        mMultiTasks = new DownloadTask[urls.size()];
        for (int i = 0; i < urls.size(); i++) {
            mMultiTasks[i] = new DownloadTask
                    .Builder(urls.get(i).getUrl(), savePath, DownloadFileUtils.getFileNameFromUrl(urls.get(i).getUrl()))
                    // 任务过去已完成是否要重新下载
                    .setPassIfAlreadyCompleted(false)
                    .build();
        }
        listener.init(urls.size());
        DownloadTask.enqueue(mMultiTasks, listener);
    }

    /**
     * 停止多任务下载
     */
    public void cancelDownloadFile() {
        if (mMultiTasks != null && mMultiTasks.length != 0) {
            for (DownloadTask multiTask : mMultiTasks) {
                if (multiTask != null) multiTask.cancel();
            }
        }
    }
}
