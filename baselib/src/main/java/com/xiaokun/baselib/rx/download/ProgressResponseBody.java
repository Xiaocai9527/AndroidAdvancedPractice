package com.xiaokun.baselib.rx.download;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by 肖坤 on 2018/4/22.
 *
 * @author 肖坤
 * @date 2018/4/22
 */

public class ProgressResponseBody extends ResponseBody
{

    private ResponseBody mResponseBody;
    private DownLoadListener mListener;
    private BufferedSource bufferedSource;
    private DownloadEntity entity;
    private File file;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public ProgressResponseBody(ResponseBody responseBody, DownloadEntity entity)
    {
        this.mResponseBody = responseBody;
        this.mListener = entity.getDownLoadListener();
        this.entity = entity;
        initFile(entity);
    }

    private void initFile(DownloadEntity entity)
    {
        String fileName = entity.getFileName();
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        file = new File(directory + File.separator + fileName);
        if (DownloadManager.dSp == null)
        {
            throw new NullPointerException("必须首先初始化DownloadManager");
        }
    }

    @Nullable
    @Override
    public MediaType contentType()
    {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength()
    {
        long contentLength = mResponseBody.contentLength();
        if (contentLength == 0)
        {
            mListener.onProgress(0, false, true);
        }
        return contentLength;
    }

    @Override
    public BufferedSource source()
    {
        if (bufferedSource == null)
        {
            bufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source)
    {
        return new ForwardingSource(source)
        {
            long totalBytesRead = DownloadManager.dSp.getLong(entity.getFileName(), 0);
            long contentLength = DownloadManager.dSp.getLong(entity.getFileName() + "content_length", 0);

            @Override
            public long read(Buffer sink, long byteCount) throws IOException
            {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (bytesRead == -1)
                {
                    sHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mListener.onProgress(100, true, false);
                        }
                    });
                    DownloadManager.dSp.edit().putLong(entity.getFileName(), 0).commit();
                } else
                {
                    sHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            int progress = (int) (totalBytesRead * 100 / contentLength);
                            mListener.onProgress(progress, false, false);
                        }
                    });
                }
                saveToFile(sink);
                return bytesRead;
            }
        };
    }

    //写入文件
    private void saveToFile(Buffer buffer)
    {
        InputStream inputStream = buffer.inputStream();
        RandomAccessFile saveFile = null;
        try
        {
            saveFile = new RandomAccessFile(file, "rw");
            saveFile.seek(getDownloadedLength());
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1)
            {
                saveFile.write(bytes, 0, len);
            }
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
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private Long getDownloadedLength()
    {
        if (file.exists())
        {
            return file.length();
        } else
        {
            throw new NullPointerException("file对象为null");
        }
    }
}
