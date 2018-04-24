package com.xiaokun.httpexceptiondemo.network.interceptors;

import com.xiaokun.httpexceptiondemo.rx.download.DownloadEntity;
import com.xiaokun.httpexceptiondemo.rx.download.DownloadManager;
import com.xiaokun.httpexceptiondemo.rx.download.ProgressResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/24
 *     描述   : 下载文件拦截器,注意下载拦截器必须单独使用
 *     版本   : 1.0
 * </pre>
 */
public class DownloadInterceptor implements Interceptor
{
    private DownloadEntity downloadEntity;

    public DownloadInterceptor(DownloadEntity entity)
    {
        this.downloadEntity = entity;
    }

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        long downloadedLength = DownloadManager.dSp.getLong(downloadEntity.getFileName(), 0);

        Response proceed = chain.proceed(request);
        DownloadManager.dSp.edit().putLong(downloadEntity.getFileName() + "content_length", proceed.body().contentLength()).commit();
        request = request.newBuilder()
                .header("RANGE", "bytes=" + downloadedLength + "-")
                .build();
        Response response = chain.proceed(request);
        response = response.newBuilder()
                .body(new ProgressResponseBody(response.body(), downloadEntity))
                .build();
        return response;
    }
}
