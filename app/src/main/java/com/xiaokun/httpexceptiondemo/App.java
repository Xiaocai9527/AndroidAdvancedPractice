package com.xiaokun.httpexceptiondemo;

import android.app.Application;
import android.content.Context;

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

    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;
    }

    public static Context getAppContext()
    {
        return app;
    }
}
