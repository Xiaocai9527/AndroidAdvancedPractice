package com.xiaokun.baselib.network.interceptors;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 肖坤 on 2018/4/30.
 * 动态更换baseuRL
 *
 * @author 肖坤
 * @date 2018/4/30
 */

public class DynamicBaseUrlInterceptor implements Interceptor
{
    private String baseUrl;

    public DynamicBaseUrlInterceptor(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        if (baseUrl != null)
        {
            HttpUrl url = request.url().newBuilder().host(baseUrl).build();
            request = request.newBuilder().url(url).build();
        }
        return chain.proceed(request);
    }
}
