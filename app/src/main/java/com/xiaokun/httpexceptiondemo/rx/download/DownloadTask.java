package com.xiaokun.httpexceptiondemo.rx.download;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.ResponseBody;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/20
 *     描述   : 异步下载文件
 *     版本   : 1.0
 * </pre>
 */
public class DownloadTask extends AsyncTask<ResponseBody, Integer, Integer>
{

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSE = 2;
    public static final int TYPE_CANCELED = 3;

    private boolean isCanceled = false;
    private boolean isPaused = false;

    private int lastProgress;

    private DownLoadListener listener;
    private String mFileName;

    /**
     * @param listener
     * @param fileName 文件名,注意文件默认下载路径放在系统的DownLoad文件夹下
     */
    public DownloadTask(DownLoadListener listener, String fileName)
    {
        this.listener = listener;
        this.mFileName = fileName;
    }

    //暂停下载
    public void pauseDownload()
    {
        isPaused = true;
    }

    //取消下载
    public void cancelDownload()
    {
        isCanceled = true;
    }

    @Override
    protected Integer doInBackground(ResponseBody... responseBodies)
    {
        ResponseBody responseBody = responseBodies[0];
        InputStream inputStream = responseBody.byteStream();
        RandomAccessFile saveFile = null;
        File file = null;
        //记录下载的文件长度
        long downloadedLength = 0;
        String fileName = mFileName;
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        file = new File(directory + File.separator + fileName);
        if (file.exists())
        {
            downloadedLength = file.length();
        }
        long contentLength = responseBody.contentLength();
        if (contentLength == 0)
        {
            return TYPE_FAILED;
        } else if (downloadedLength == contentLength)
        {
            return TYPE_SUCCESS;
        }
        if (inputStream != null)
        {
            try
            {
                saveFile = new RandomAccessFile(file, "rw");
                saveFile.seek(downloadedLength);
                byte[] bytes = new byte[1024];
                int total = 0;
                int len;
                while ((len = inputStream.read(bytes)) != -1)
                {
                    if (isCanceled)
                    {
                        return TYPE_CANCELED;
                    } else if (isPaused)
                    {
                        return TYPE_PAUSE;
                    } else
                    {
                        total += len;
                        saveFile.write(bytes, 0, len);
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        publishProgress(progress);
                    }
                }
                responseBody.close();
                return TYPE_SUCCESS;
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                try
                {
                    if (inputStream != null)
                    {
                        inputStream.close();
                    }
                    if (saveFile != null)
                    {
                        saveFile.close();
                    }
                    if (isCanceled && file != null)
                    {
                        file.delete();
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values)
    {
        super.onProgressUpdate(values);
        int progress = values[0];
        if (progress > lastProgress)
        {
            lastProgress = progress;
            listener.onProgress(progress);
        }
    }

    @Override
    protected void onPostExecute(Integer integer)
    {
        super.onPostExecute(integer);
        switch (integer)
        {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSE:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            default:

                break;
        }
    }
}
