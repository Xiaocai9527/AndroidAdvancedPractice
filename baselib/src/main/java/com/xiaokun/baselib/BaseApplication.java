package com.xiaokun.baselib;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.xiaokun.baselib.util.ACache;

import java.io.File;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/06
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class BaseApplication extends Application {

    private static BaseApplication app;
    private static SharedPreferences mSp;
    private static ACache cache;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        mSp = getSharedPreferences("xiaokun", MODE_PRIVATE);
        cache = ACache.get(getCacheFile());
    }

    public static Context getAppContext() {
        return app;
    }

    public static SharedPreferences getSp() {
        return mSp;
    }

    public static ACache getCache() {
        return cache;
    }

    //获取缓存目录
    private File getCacheFile() {
        File file = new File(getExternalCacheDir() + "/http_exception_data");
        if (!(file.exists() && file.isDirectory())) {
            file.mkdirs();
        }
        return file;
    }
}
