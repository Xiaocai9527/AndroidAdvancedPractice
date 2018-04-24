package com.xiaokun.httpexceptiondemo.network.interceptors;

import com.xiaokun.httpexceptiondemo.App;
import com.xiaokun.httpexceptiondemo.Constants;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/24
 *     描述   : cookie管理拦截器
 *     版本   : 1.0
 * </pre>
 */
public class CookieInterceptor implements Interceptor
{
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        List<String> cookies = (List<String>) App.getCache().getAsObject(Constants.COOKIES);
        if (cookies != null)
        {
            Request.Builder builder = chain.request().newBuilder();
            for (String cookie : cookies)
            {
                builder.addHeader("Cookie", cookie);
            }
            request = builder.build();
        }
        Response response = chain.proceed(request);
        List<String> headers = response.headers("Set-Cookie");
        App.getCache().put(Constants.COOKIES, (Serializable) headers);
        return response;
    }
}
