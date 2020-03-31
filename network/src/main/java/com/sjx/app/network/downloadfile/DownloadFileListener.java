package com.sjx.app.network.downloadfile;

import android.util.Log;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class DownloadFileListener implements DownloadListener {
    private static final String TAG = "DownloadFile";
    private int countDownLoad = 0;
    private List<String> downLoadSuccessList = new ArrayList<>();
    private List<String> downLoadErrorList = new ArrayList<>();
    //总需下载数
    private int totalDownloadNum;

    @Override
    public void taskStart(@NonNull DownloadTask task) {
        Log.d(TAG, "taskStart taskId:" + task.getId() + " " + task.getFilename());
    }

    @Override
    public void connectTrialStart(@NonNull DownloadTask task, @NonNull Map<String, List<String>> requestHeaderFields) {
        Log.d(TAG, "connectTrialStart taskId:" + task.getId() + " " + task.getFilename());
    }

    @Override
    public void connectTrialEnd(@NonNull DownloadTask task, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {
        Log.d(TAG, "connectTrialEnd taskId:" + task.getId() + " " + task.getFilename());
    }

    @Override
    public void downloadFromBeginning(@NonNull DownloadTask task, @NonNull BreakpointInfo info, @NonNull ResumeFailedCause cause) {
        Log.d(TAG, "downloadFromBeginning taskId:" + task.getId() + " " + task.getFilename());
    }

    @Override
    public void downloadFromBreakpoint(@NonNull DownloadTask task, @NonNull BreakpointInfo info) {
        Log.d(TAG, "downloadFromBreakpoint taskId:" + task.getId() + " " + task.getFilename());
    }

    @Override
    public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {
        Log.d(TAG, "connectStart taskId:" + task.getId() + " " + task.getFilename());
    }

    @Override
    public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {
        Log.d(TAG, "connectEnd taskId:" + task.getId() + " " + task.getFilename());
    }

    @Override
    public void fetchStart(@NonNull DownloadTask task, int blockIndex, long contentLength) {
        Log.d(TAG, "fetchStart taskId:" + task.getId() + " " + task.getFilename());
    }

    @Override
    public void fetchProgress(@NonNull DownloadTask task, int blockIndex, long increaseBytes) {
        Log.d(TAG, "fetchProgress taskId:" + task.getId() + " " + task.getFilename() + "  " + blockIndex + "  " + increaseBytes);
    }

    @Override
    public void fetchEnd(@NonNull DownloadTask task, int blockIndex, long contentLength) {
        Log.d(TAG, "fetchEnd taskId:" + task.getId() + " " + task.getFilename());
    }

    @Override
    public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {
        Log.d(TAG, "taskEnd taskId:" + task.getId() + " " + task.getFilename() + "  " + cause.name() + "  " + (realCause == null ? "" : realCause.getMessage()));
        if (downLoadSuccessList == null || downLoadErrorList == null) {
            throw new SecurityException(TAG + " init()");
        }
        countDownLoad++;
        switch (cause) {
            case COMPLETED:
            case SAME_TASK_BUSY:    //相同任务
                downLoadSuccessList.add(task.getUrl());
                break;
            case ERROR:
                downLoadErrorList.add(task.getUrl());
                break;
        }
        if (totalDownloadNum == countDownLoad) {
            Log.d(TAG, "downloadComplete: success = " + downLoadSuccessList.size() + " error = " + downLoadErrorList.size());
            downloadComplete(task, downLoadSuccessList, downLoadErrorList);
        }
    }

    /**
     * 下载完成
     *
     * @param downLoadSuccessList 下载成功集合
     * @param downLoadErrorList   下载失败集合
     */
    protected abstract void downloadComplete(@NonNull DownloadTask task, @NonNull List<String> downLoadSuccessList, @NonNull List<String> downLoadErrorList);

    /**
     * 初始化(使用监听时需先初始化)
     */
    public void init(int totalDownloadNum) {
        this.totalDownloadNum = totalDownloadNum;
        countDownLoad = 0;
        downLoadSuccessList = new ArrayList<>();
        downLoadErrorList = new ArrayList<>();
    }
}
