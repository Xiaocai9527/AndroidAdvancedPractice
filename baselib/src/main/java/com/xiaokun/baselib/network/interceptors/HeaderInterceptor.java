package com.xiaokun.baselib.network.interceptors;

import com.xiaokun.httpexceptiondemo.App;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/24
 *     描述   : 请求头属性拦截器
 *     版本   : 1.0
 * </pre>
 */
public class HeaderInterceptor implements Interceptor
{
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        //这里我随便写一个token
        //其实应该从sp中取出token,这个token首次是从登录接口取到的
        String token = App.getSp().getString("token", "");
        Request request = chain.request().newBuilder()
                .header("token", token)
                .build();
        return chain.proceed(request);
    }
}
