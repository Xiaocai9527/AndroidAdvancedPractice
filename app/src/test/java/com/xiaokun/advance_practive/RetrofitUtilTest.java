package com.xiaokun.advance_practive;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xiaokun.advance_practive.network.api.ApiService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/29
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class RetrofitUtilTest {

    String username = "canglashi";
    String password = "123456";

    private static RetrofitUtilTest mRetrofitUtilTest = null;

    private RetrofitUtilTest() {
    }

    public static RetrofitUtilTest getInstance() {
        synchronized (RetrofitUtilTest.class) {
            if (mRetrofitUtilTest == null) {
                mRetrofitUtilTest = new RetrofitUtilTest();
            }
        }
        return mRetrofitUtilTest;
    }

    public <T> T getRetrofit(Class<T> serviceClass) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(chain -> {
                    final Request original = chain.request();
                    //添加cookie
                    final Request authorized = original.newBuilder()
                            .addHeader("Cookie", "loginUserName=" + username)
                            .addHeader("Cookie", "loginUserPassword=" + password)
                            .build();

                    return chain.proceed(authorized);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder().client(client).baseUrl(ApiService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }


}
