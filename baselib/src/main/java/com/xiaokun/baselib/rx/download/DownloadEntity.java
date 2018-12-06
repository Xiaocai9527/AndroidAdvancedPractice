package com.xiaokun.baselib.rx.download;

/**
 * Created by 肖坤 on 2018/4/22.
 *
 * @author 肖坤
 * @date 2018/4/22
 */

public class DownloadEntity
{
    private DownLoadListener downLoadListener;
    private String fileName;

    public DownloadEntity(DownLoadListener listener, String fileName)
    {
        this.downLoadListener = listener;
        this.fileName = fileName;
    }

    public DownLoadListener getDownLoadListener()
    {
        return downLoadListener;
    }

    public void setDownLoadListener(DownLoadListener downLoadListener)
    {
        this.downLoadListener = downLoadListener;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
