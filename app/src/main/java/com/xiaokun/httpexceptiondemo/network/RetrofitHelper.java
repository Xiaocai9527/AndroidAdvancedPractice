package com.xiaokun.httpexceptiondemo.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xiaokun.httpexceptiondemo.rx.download.DownloadEntity;

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

    private static Retrofit.Builder builder;

    private static Retrofit.Builder getDefaultRetrofitBuilder()
    {
        if (builder == null)
        {
            //设置gson解析不严格模式,防止一些解析错误,比如double数据出现NaN时
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            builder = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        }
        return builder;
    }

    //更灵活的获取retrofit方式
    public static Retrofit getRetrofit(OkHttpClient client, String baseUrl)
    {
        if (retrofit2 == null)
        {
            retrofit2 = getDefaultRetrofitBuilder()
                    .client(client)
                    .baseUrl(baseUrl)
                    .build();
        }
        return retrofit2;
    }

    public static Retrofit getRetrofit1(boolean isCache)
    {
        if (retrofit1 == null)
        {
            OkHttpClient client = OkhttpHelper.getDefaultClient(isCache);
            Retrofit.Builder builder = getDefaultRetrofitBuilder();
            retrofit1 = builder.client(client)
                    .baseUrl(ApiService.baseUrl)
                    .build();
        }
        return retrofit1;
    }

    /**
     * 专门下载的retrofit,最好不要重用以防出错误。
     *
     * @return
     */
    public static Retrofit getDownloadRetrofit(DownloadEntity entity)
    {
        OkHttpClient client = OkhttpHelper.initDownloadClient(entity);
        Retrofit.Builder builder = getDefaultRetrofitBuilder();
        return builder.baseUrl(ApiService.baseUrl)
                .client(client).build();
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
