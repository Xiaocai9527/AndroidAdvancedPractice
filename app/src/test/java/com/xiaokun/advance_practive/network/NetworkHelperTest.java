package com.xiaokun.advance_practive.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xiaokun.advance_practive.network.api.ApiService;
import com.xiaokun.advance_practive.network.api.WanApiService;
import com.xiaokun.advance_practive.network.wanAndroid.TotalResEntity;
import com.xiaokun.baselib.network.WanBaseResponseEntity;
import com.xiaokun.advance_practive.network.wanAndroid.WanLoginEntityRes;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/06
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class NetworkHelperTest {

    private WanApiService mApiService;

    String username = "canglashi";
    String password = "123456";

    @Before
    public void setUp() throws Exception {
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.reset();
        RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

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
        mApiService = retrofit.create(WanApiService.class);
    }

    @Test
    public void testRegister() {
        mApiService.register(username, password, password).subscribe(registerWanBaseResponseEntity -> {
        }, throwable -> {
        });
    }

    @Test
    public void login() {
        mApiService.login(username, password).subscribe(dataBeanWanBaseResponseEntity -> {

        }, throwable -> {

        });
    }

    @Test
    public void getHomeArticles() {
        int page = 0;
        mApiService.getHomeArticles(page).subscribe(homeArticlesWanBaseResponseEntity -> {

        }, throwable -> {

        });
    }

    @Test
    public void getCollect() {
        int page = 0;
        mApiService.getCollect(page).subscribe(collectWanBaseResponseEntity -> {

        }, throwable -> {

        });
    }
}