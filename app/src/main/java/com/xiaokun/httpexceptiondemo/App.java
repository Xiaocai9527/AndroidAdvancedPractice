package com.xiaokun.httpexceptiondemo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.stetho.Stetho;

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

    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;
        mSp = getSharedPreferences("xiaokun", MODE_PRIVATE);
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
}
