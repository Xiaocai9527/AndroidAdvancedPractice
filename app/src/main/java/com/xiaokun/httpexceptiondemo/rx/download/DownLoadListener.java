package com.xiaokun.httpexceptiondemo.rx.download;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/20
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public interface DownLoadListener
{
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
