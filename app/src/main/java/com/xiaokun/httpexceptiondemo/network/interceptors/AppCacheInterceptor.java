package com.xiaokun.httpexceptiondemo.network.interceptors;

import com.xiaokun.httpexceptiondemo.util.SystemUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/24
 *     描述   : okHttp缓存拦截器
 *     版本   : 1.0
 * </pre>
 */
public class AppCacheInterceptor implements Interceptor
{
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        if (!SystemUtils.isNetworkConnected())
        {
            //强制使用缓存
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        int tryCount = 0;
        Response response = chain.proceed(request);
        while (!response.isSuccessful() && tryCount < 3)
        {
            tryCount++;
            // 重试
            response = chain.proceed(request);
        }
        return response;
    }
}
