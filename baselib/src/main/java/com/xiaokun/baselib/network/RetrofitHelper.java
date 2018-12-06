package com.xiaokun.baselib.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xiaokun.baselib.rx.download.DownloadEntity;

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
public class RetrofitHelper {
    //假设这个是默认的retrofit,因为一个App可能有多个baseUrl
    //所以可能有多个retrofit
    private Retrofit retrofit1;

    private Retrofit retrofit2;

    private Retrofit.Builder builder;

    private String baseUrl;

    private RetrofitHelper() {
    }

    public static RetrofitHelper getInstance() {
        return RetrofitHelperHolder.sInstance;
    }

    private static class RetrofitHelperHolder {
        private static final RetrofitHelper sInstance = new RetrofitHelper();
    }

    public RetrofitHelper setDebug(boolean isDebug) {
        OkhttpHelper.setDebug(isDebug);
        return this;
    }

    public RetrofitHelper cache(boolean isCache) {
        OkhttpHelper.setCache(isCache);
        return this;
    }

    public RetrofitHelper baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    private Retrofit.Builder getDefaultRetrofitBuilder() {
        if (builder == null) {
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
    public Retrofit getRetrofit(OkHttpClient client, String baseUrl) {
        if (retrofit2 == null) {
            retrofit2 = getDefaultRetrofitBuilder()
                    .client(client)
                    .baseUrl(baseUrl)
                    .build();
        }
        return retrofit2;
    }

    public Retrofit getRetrofit1() {
        if (retrofit1 == null) {
            OkHttpClient client = OkhttpHelper.getDefaultClient();
            Retrofit.Builder builder = getDefaultRetrofitBuilder();
            retrofit1 = builder.client(client)
                    .baseUrl(baseUrl)
                    .build();
        }
        return retrofit1;
    }

    /**
     * 专门下载的retrofit,最好不要重用以防出错误。
     *
     * @return
     */
    public Retrofit getDownloadRetrofit(DownloadEntity entity) {
        OkHttpClient client = OkhttpHelper.initDownloadClient(entity);
        Retrofit.Builder builder = getDefaultRetrofitBuilder();
        return builder.baseUrl(baseUrl)
                .client(client).build();
    }

    /**
     * 默认调用此方法
     */
    public <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, getRetrofit1());
    }

    /**
     * 如果需要定制化的调用这个方法
     */
    public <S> S createService(Class<S> serviceClass, Retrofit retrofit) {
        if (retrofit == null) {
            throw new NullPointerException("retrofit 不能为null");
        }
        return retrofit.create(serviceClass);
    }
}
