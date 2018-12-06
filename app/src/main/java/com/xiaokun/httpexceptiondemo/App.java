package com.xiaokun.httpexceptiondemo;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.stetho.Stetho;
import com.xiaokun.baselib.BaseApplication;
import com.xiaokun.baselib.network.RetrofitHelper;
import com.xiaokun.baselib.util.ACache;
import com.xiaokun.httpexceptiondemo.network.api.ApiService;
import com.xiaokun.httpexceptiondemo.network.api.WanApiService;

import java.io.File;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/19
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
//        DiskCache.openCache(this);
        //配置网络
        RetrofitHelper.getInstance()
                .setDebug(BuildConfig.DEBUG)
                .cache(false)
                .baseUrl(ApiService.baseUrl);
    }


}
