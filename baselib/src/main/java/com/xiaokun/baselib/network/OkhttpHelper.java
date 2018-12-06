package com.xiaokun.baselib.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.xiaokun.baselib.BaseApplication;
import com.xiaokun.baselib.config.Constants;
import com.xiaokun.baselib.network.interceptors.AppCacheInterceptor;
import com.xiaokun.baselib.network.interceptors.CookieInterceptor;
import com.xiaokun.baselib.network.interceptors.DownloadInterceptor;
import com.xiaokun.baselib.network.interceptors.HeaderInterceptor;
import com.xiaokun.baselib.network.interceptors.TokenInterceptor;
import com.xiaokun.baselib.rx.download.DownloadEntity;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/20
 *     描述   : okhttp配置
 *     版本   : 1.0
 * </pre>
 */
public class OkhttpHelper {
    private static int CONNECT_TIME = 10;
    private static int READ_TIME = 20;
    private static int WRITE_TIME = 20;
    private static OkHttpClient.Builder builder;
    private static OkHttpClient client;

    /**
     * 是否debug,默认设置是false
     */
    private static boolean mIsDebug = false;
    /**
     * 是否启用okhttp缓存设置
     */
    private static boolean mIsCache = false;

    /**
     * 设置是否debug
     */
    public static void setDebug(boolean isDebug) {
        mIsDebug = isDebug;
    }

    /**
     * 设置是否启用缓存
     */
    public static void setCache(boolean isCache) {
        mIsCache = isCache;
    }

    private static OkHttpClient.Builder getDefaultBuilder() {
        builder = new OkHttpClient.Builder();
        if (mIsDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            ChuckInterceptor chuckInterceptor = new ChuckInterceptor(BaseApplication.getAppContext());
            loggingInterceptor.setLevel(BODY);
            //打印拦截器
            builder.addInterceptor(loggingInterceptor);
            //调试拦截器
            builder.addInterceptor(new StethoInterceptor());
            builder.addInterceptor(chuckInterceptor);
        }
        File cacheFile = new File(Constants.PATH_CACHE);
        //最大50M，缓存太大领导有意见！为何你App占这么多内存？
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        if (mIsCache) {
            builder.addInterceptor(new AppCacheInterceptor())
                    .cache(cache);
        }
        //下面3个超时,不设置默认就是10s
        builder.connectTimeout(CONNECT_TIME, TimeUnit.SECONDS)
                .readTimeout(READ_TIME, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME, TimeUnit.SECONDS)
                //失败重试
                .retryOnConnectionFailure(true);
        return builder;
    }

    public static OkHttpClient getOkhttpClient( Interceptor... interceptors) {
        if (builder == null) {
            builder = getDefaultBuilder();
        }
        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

    public static OkHttpClient getDefaultClient() {
        OkHttpClient.Builder builder = getDefaultBuilder();
        builder.addInterceptor(new CookieInterceptor());
        if (client == null) {
            client = builder.build();
        }
        return client;
    }

    /**
     * 模拟token刷新
     *
     * @return
     */
    public static OkHttpClient initOkHttp2() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (mIsDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BODY);
            //打印拦截器
            builder.addInterceptor(loggingInterceptor);
            //调试拦截器
            builder.addInterceptor(new StethoInterceptor());
        }

        File cacheFile = new File(Constants.PATH_CACHE);
        //最大50M，缓存太大领导有意见！为何你App占这么多内存？
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        //这里用到okhttp的拦截器知识
        builder.addInterceptor(new HeaderInterceptor())
                .addInterceptor(new AppCacheInterceptor())
//                .addNetworkInterceptor(netCacheInterceptor)
                //token刷新拦截器
                .addInterceptor(new TokenInterceptor())
                .cache(cache)
                //下面3个超时,不设置默认就是10s
                .connectTimeout(CONNECT_TIME, TimeUnit.SECONDS)
                .readTimeout(READ_TIME, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME, TimeUnit.SECONDS)
                //失败重试
                .retryOnConnectionFailure(true)
                .build();
        return builder.build();
    }

    /**
     * 初始化专门下载的okhttp client
     *
     * @param entity
     * @return
     */
    public static OkHttpClient initDownloadClient(DownloadEntity entity) {
        DownloadInterceptor downloadInterceptor = new DownloadInterceptor(entity);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (mIsDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BODY);
            //打印拦截器
            builder.addInterceptor(loggingInterceptor);
            //调试拦截器
            builder.addInterceptor(new StethoInterceptor());
        }
        builder.addInterceptor(downloadInterceptor)
                //下面3个超时,不设置默认就是10s
                .connectTimeout(CONNECT_TIME, TimeUnit.SECONDS)
                .readTimeout(READ_TIME, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME, TimeUnit.SECONDS)
                //失败重试
                .retryOnConnectionFailure(true);
        return builder.build();
    }
}
