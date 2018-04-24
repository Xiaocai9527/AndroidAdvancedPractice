package com.xiaokun.httpexceptiondemo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.stetho.Stetho;
import com.xiaokun.httpexceptiondemo.util.ACache;

import java.io.File;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/19
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class App extends Application
{
    private static App app;
    private static SharedPreferences mSp;
    private static ACache cache;

    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;
        mSp = getSharedPreferences("xiaokun", MODE_PRIVATE);
        cache = ACache.get(getCacheFile());
        if (BuildConfig.DEBUG)
        {
            Stetho.initializeWithDefaults(this);
        }
    }

    public static Context getAppContext()
    {
        return app;
    }

    public static SharedPreferences getSp()
    {
        return mSp;
    }

    public static ACache getCache()
    {
        return cache;
    }

    //获取缓存目录
    private File getCacheFile()
    {
        File file = new File(getExternalCacheDir() + "/http_exception_data");
        if (!(file.exists() && file.isDirectory()))
        {
            file.mkdirs();
        }
        return file;
    }

}
