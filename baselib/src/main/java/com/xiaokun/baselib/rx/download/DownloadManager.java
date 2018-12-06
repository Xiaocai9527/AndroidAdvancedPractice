package com.xiaokun.baselib.rx.download;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

import io.reactivex.disposables.Disposable;

/**
 * Created by 肖坤 on 2018/4/22.
 *
 * @author 肖坤
 * @date 2018/4/22
 */

public class DownloadManager
{
    public static SharedPreferences dSp;

    /**
     * 初始化DownloadManager
     *
     * @param context
     */
    public static void initDownManager(Context context)
    {
        dSp = context.getSharedPreferences("download_file", Context.MODE_PRIVATE);
    }

    /**
     * 暂停下载
     *
     * @param disposable 控制rxjava的开关
     * @param fileName   下载的文件名,必须包含后缀
     */
    public static void pauseDownload(Disposable disposable, String fileName)
    {
        if (disposable == null || TextUtils.isEmpty(fileName))
        {
            return;
        }
        if (!disposable.isDisposed())
        {
            disposable.dispose();
        }
        if (dSp == null)
        {
            throw new NullPointerException("必须首先初始化DownloadManager");
        }
        File file = initFile(fileName);
        if (file.exists() && dSp != null)
        {
            dSp.edit().putLong(file.getName(), file.length()).commit();
        }
    }

    /**
     * 取消下载
     *
     * @param disposable 控制rxjava的开关
     * @param fileName   下载的文件名,必须包含后缀
     */
    public static void cancelDownload(Disposable disposable, String fileName)
    {
        if (disposable == null || TextUtils.isEmpty(fileName))
        {
            return;
        }
        if (!disposable.isDisposed())
        {
            disposable.dispose();
        }
        File file = initFile(fileName);
        if (file.exists() && dSp != null)
        {
            dSp.edit().putLong(file.getName(), 0).commit();
        }
        //取消下载，最后一步记得删除掉已经下载的文件
        if (file.exists())
        {
            file.delete();
        }
    }

    public static File initFile(String fileName)
    {
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File file = new File(directory + File.separator + fileName);
        return file;
    }
}
