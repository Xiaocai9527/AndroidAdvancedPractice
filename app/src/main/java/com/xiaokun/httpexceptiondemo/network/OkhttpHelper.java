package com.xiaokun.httpexceptiondemo.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.xiaokun.httpexceptiondemo.App;
import com.xiaokun.httpexceptiondemo.BuildConfig;
import com.xiaokun.httpexceptiondemo.Constants;
import com.xiaokun.httpexceptiondemo.rx.download.DownloadEntity;
import com.xiaokun.httpexceptiondemo.rx.download.DownloadManager;
import com.xiaokun.httpexceptiondemo.rx.download.ProgressResponseBody;
import com.xiaokun.httpexceptiondemo.util.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/20
 *     描述   : okhttp配置
 *     版本   : 1.0
 * </pre>
 */
public class OkhttpHelper
{
    private static int CONNECT_TIME = 10;
    private static int READ_TIME = 20;
    private static int WRITE_TIME = 20;

    public static OkHttpClient initOkHttp1(boolean isCache)
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG)
        {
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
        if (isCache)
        {
            builder.addInterceptor(appCacheInterceptor)
                    .cache(cache);
        }
        //这里用到okhttp的拦截器知识  //下面3个超时,不设置默认就是10s
        builder.connectTimeout(CONNECT_TIME, TimeUnit.SECONDS)
                .readTimeout(READ_TIME, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME, TimeUnit.SECONDS)
//               .addNetworkInterceptor(netCacheInterceptor)
                //失败重试
                .retryOnConnectionFailure(true)
                .build();
        return builder.build();
    }

    /**
     * 模拟token刷新
     *
     * @return
     */
    public static OkHttpClient initOkHttp2()
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG)
        {
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
        builder.addInterceptor(headerInterceptor)
                .addInterceptor(appCacheInterceptor)
//                .addNetworkInterceptor(netCacheInterceptor)
                //token刷新拦截器
                .addInterceptor(tokenInterceptor)
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

    private static DownloadEntity downloadEntity;

    public static OkHttpClient initDownloadClient(DownloadEntity entity)
    {
        downloadEntity = entity;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG)
        {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BODY);
            //打印拦截器
            builder.addInterceptor(loggingInterceptor);
            //调试拦截器
            builder.addInterceptor(new StethoInterceptor());
        }
        builder
                .addInterceptor(downloadInterceptor)
                //下面3个超时,不设置默认就是10s
                .connectTimeout(CONNECT_TIME, TimeUnit.SECONDS)
                .readTimeout(READ_TIME, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME, TimeUnit.SECONDS)
                //失败重试
                .retryOnConnectionFailure(true)
                .build();
        return builder.build();
    }

    //添加验证token的header头属性
    static Interceptor headerInterceptor = new Interceptor()
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
    };

    static Interceptor tokenInterceptor = new Interceptor()
    {
        @Override
        public Response intercept(Chain chain) throws IOException
        {
            Request request = chain.request();
            Response response = chain.proceed(request);
            //判断token是否过期
            if (isTokenExpired(response))
            {
                //同步请求方式,获取新token
                ApiService service = RetrofitHelper.createService(ApiService.class, false);
                Call<BaseResponse<ResEntity1.DataBean>> call = service.getNewToken();
                retrofit2.Response<BaseResponse<ResEntity1.DataBean>> tokenRes = call.execute();
                String newToken = tokenRes.body().getData().getRes();
                //然后把这个新token存到sp中
                App.getSp().edit().putString("token", newToken).commit();
                Request newRequest = chain.request()
                        .newBuilder()
                        .header("token", newToken)
                        .build();
                response.body().close();//释放资源
                //重新请求
                return chain.proceed(newRequest);
            }
            //若没有过期,直接返回response
            return response;
        }
    };

    static Interceptor appCacheInterceptor = new Interceptor()
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
    };

    private static final String TAG = "OkhttpHelper";

    //下载文件拦截器
    static Interceptor downloadInterceptor = new Interceptor()
    {
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
    };

    //貌似这个云端拦截器设置了也无效
    //因为我觉得做不做缓存处理，拿缓存数据和拿服务端数据都是客户端的事情
    //所有我觉得不用给response做相应头处理
    //有木有大佬支支招
    static Interceptor netCacheInterceptor = new Interceptor()
    {
        @Override
        public Response intercept(Chain chain) throws IOException
        {
            Response response = chain.proceed(chain.request());
            if (SystemUtils.isNetworkConnected())
            {
                int maxAge = 0;
                // 有网络时, 不缓存, 最大保存时长为0
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
            } else
            {
                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
            return response;
        }
    };

    /**
     * 判断token是否过期
     *
     * @param response
     * @return
     */
    private static boolean isTokenExpired(Response response)
    {
        try
        {
            String bodyString = getBodyString(response);
            BaseResponse tokenExpiredData = new Gson().fromJson(bodyString, BaseResponse.class);
            int retCode = tokenExpiredData.getCode();
            if (retCode == Constants.EXPIRED_TOKEN)
            {
                return true;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将response转换为json字符串
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static String getBodyString(Response response) throws IOException
    {
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null)
        {
            contentType.charset(charset);
        }
        //注意这里的方式,是仿写的HttpLoggingInterceptor
        //在okhttp中buffer只能被read一次,所以只能先clone然后在read
        //否则会报错
        return buffer.clone().readString(charset);
    }
}
