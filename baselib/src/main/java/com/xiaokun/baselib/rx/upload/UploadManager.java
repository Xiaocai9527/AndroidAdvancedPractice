package com.xiaokun.baselib.rx.upload;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/25
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class UploadManager {

    public static SharedPreferences dSp;

    public static void init(Context context) {
        dSp = context.getSharedPreferences("upload_file", Context.MODE_PRIVATE);
    }

    /**
     * 暂停上传
     *
     * @param disposable 控制网络开关
     * @param file   需包含后缀名
     */
    public static void pauseUpload(Disposable disposable, File file) {
        if (disposable == null || !file.exists()) {
            return;
        }
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        if (dSp == null) {
            throw new NullPointerException("必须首先初始化DownloadManager");
        }
//        File file = initFile(fileName);
        if (file.exists() && dSp != null) {
            dSp.edit().putLong(file.getName(), file.length()).apply();
        }
    }

    /**
     * 取消上传
     *
     * @param disposable 控制网络开关
     * @param file   需包含后缀名
     */
    public static void cancelUpload(Disposable disposable, File file) {
        if (disposable == null || !file.exists()) {
            return;
        }
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        // File file = initFile(fileName);
        if (file.exists() && dSp != null) {
            dSp.edit().putLong(file.getName(), 0).apply();
        }
        //取消上传,不用删除文件
        if (file.exists()) {
            //file.delete();
        }
    }

    public static File initFile(String fileName) {
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File file = new File(directory + File.separator + fileName);
        return file;
    }

}
