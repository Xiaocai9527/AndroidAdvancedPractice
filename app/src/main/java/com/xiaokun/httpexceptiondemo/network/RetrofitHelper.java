package com.xiaokun.httpexceptiondemo.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/20
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class RetrofitHelper
{
    //假设这个是默认的retrofit,因为一个App可能有多个baseUrl
    //所以可能有多个retrofit
    private static Retrofit retrofit1;

    private static Retrofit retrofit2;

    public static Retrofit getRetrofit1(boolean isCache)
    {
        //设置gson解析不严格模式,防止一些解析错误,比如double数据出现NaN时
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = OkhttpHelper.initOkHttp1(isCache);
        if (retrofit1 == null)
        {
            retrofit1 = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(ApiService.baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit1;
    }

    public static Retrofit getRetrofit2()
    {
        //设置gson解析不严格模式,防止一些解析错误,比如double数据出现NaN时
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = OkhttpHelper.initOkHttp2();
        if (retrofit2 == null)
        {
            retrofit2 = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(ApiService.baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit2;
    }

    public static <S> S createService(Class<S> serviceClass, boolean isCache)
    {
        return createService(serviceClass, getRetrofit1(isCache));
    }

    public static <S> S createService(Class<S> serviceClass, Retrofit retrofit)
    {
        if (retrofit == null)
        {
            throw new NullPointerException("retrofit 不能为null");
        }
        return retrofit.create(serviceClass);
    }
}
