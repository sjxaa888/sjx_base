package com.sjx.app.network.downloadfile;

public class DownloadFileUtils {
    /**
     * 获取名字和尾缀
     */
    public static String getFileNameFromUrl(String fileUrl) {
        //处理七牛云的特殊图片
        int jpgIndex = fileUrl.indexOf(".jpg");
        if (jpgIndex > 0) {
            fileUrl = fileUrl.substring(0, jpgIndex + ".jpg".length());
        }
        int pngIndex = fileUrl.indexOf(".png");
        if (pngIndex > 0) {
            fileUrl = fileUrl.substring(0, pngIndex + ".png".length());
        }
        int apkIndex = fileUrl.indexOf(".apk?");
        if (apkIndex > 0) {
            fileUrl = fileUrl.substring(0, apkIndex + ".apk".length());
        }
        //获取文件名
        int startIndex = fileUrl.lastIndexOf("/") + 1;
        String fileName = "文件名未知";
        if (startIndex > 0) {
            fileName = fileUrl.substring(startIndex);
        }
        return fileName;
    }

    /**
     * 获取名字 不要尾缀
     *
     * @param fileUrl
     * @return
     */
    public static String getFileName(String fileUrl) {
        //处理七牛云的特殊图片
        int jpgIndex = fileUrl.indexOf(".jpg");
        if (jpgIndex > 0) {
            fileUrl = fileUrl.substring(0, jpgIndex);
        }
        int pngIndex = fileUrl.indexOf(".png");
        if (pngIndex > 0) {
            fileUrl = fileUrl.substring(0, pngIndex);
        }
        int apkIndex = fileUrl.indexOf(".apk?");
        if (apkIndex > 0) {
            fileUrl = fileUrl.substring(0, apkIndex);
        }
        //获取文件名
        int startIndex = fileUrl.lastIndexOf("/") + 1;
        String fileName = "文件名未知";
        if (startIndex > 0) {
            fileName = fileUrl.substring(startIndex);
        }
        return fileName;
    }
}
