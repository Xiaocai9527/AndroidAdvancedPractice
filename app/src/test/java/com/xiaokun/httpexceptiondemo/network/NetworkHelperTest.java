package com.xiaokun.httpexceptiondemo.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xiaokun.httpexceptiondemo.network.api.ApiService;
import com.xiaokun.httpexceptiondemo.network.api.WanApiService;
import com.xiaokun.httpexceptiondemo.network.wanAndroid.TotalResEntity;
import com.xiaokun.baselib.network.WanBaseResponseEntity;
import com.xiaokun.httpexceptiondemo.network.wanAndroid.WanLoginEntityRes;

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

    @Before
    public void setUp() throws Exception {
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
        RxAndroidPlugins.reset();
        RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request original = chain.request();
                        //添加cookie
                        final Request authorized = original.newBuilder()
                                .addHeader("Cookie", "loginUserName=13886149842")
                                .addHeader("Cookie", "loginUserPassword=xk939291")
                                .build();

                        return chain.proceed(authorized);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder().client(client).baseUrl(ApiService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mApiService = retrofit.create(WanApiService.class);
    }

    @Test
    public void login() {
        String username = "13886149842";
        String password = "xk939291";
        mApiService.login(username, password).subscribe(new Consumer<WanBaseResponseEntity<WanLoginEntityRes.DataBean>>() {
            @Override
            public void accept(WanBaseResponseEntity<WanLoginEntityRes.DataBean> dataBeanWanBaseResponseEntity) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }

    @Test
    public void getHomeArticles() {
        int page = 0;
        mApiService.getHomeArticles(page).subscribe(new Consumer<WanBaseResponseEntity<TotalResEntity.HomeArticles>>() {
            @Override
            public void accept(WanBaseResponseEntity<TotalResEntity.HomeArticles> homeArticlesWanBaseResponseEntity) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }

    @Test
    public void getCollect() {
        int page = 0;
        mApiService.getCollect(page).subscribe(new Consumer<WanBaseResponseEntity<TotalResEntity.Collect>>() {
            @Override
            public void accept(WanBaseResponseEntity<TotalResEntity.Collect> collectWanBaseResponseEntity) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }
}