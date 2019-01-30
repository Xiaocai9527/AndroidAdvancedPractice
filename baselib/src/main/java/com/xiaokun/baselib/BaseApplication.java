package com.xiaokun.baselib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.flipper.android.AndroidFlipperClient;
import com.facebook.flipper.core.FlipperClient;
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin;
import com.facebook.flipper.plugins.example.ExampleFlipperPlugin;
import com.facebook.flipper.plugins.inspector.DescriptorMapping;
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin;
import com.facebook.flipper.plugins.leakcanary.LeakCanaryFlipperPlugin;
import com.facebook.flipper.plugins.litho.LithoFlipperDescriptors;
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor;
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin;
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin;
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin.SharedPreferencesDescriptor;
import com.facebook.litho.config.ComponentsConfiguration;
import com.facebook.soloader.SoLoader;
import com.xiaokun.baselib.util.ACache;
import com.xiaokun.baselib.util.ContextHolder;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static com.xiaokun.baselib.network.OkhttpHelper.getDefaultClient;

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

    protected Activity mCurrentActivity;

    @Nullable
    public static OkHttpClient sOkHttpClient = null;

    public static FlipperOkhttpInterceptor flipperOkhttpInterceptor;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        ContextHolder.setContext(this);
        mSp = getSharedPreferences("xiaokun", MODE_PRIVATE);
        cache = ACache.get(getCacheFile());


        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);

        SoLoader.init(this, false);

        final FlipperClient client = AndroidFlipperClient.getInstance(this);
        final DescriptorMapping descriptorMapping = DescriptorMapping.withDefaults();
        final NetworkFlipperPlugin networkPlugin = new NetworkFlipperPlugin();
        flipperOkhttpInterceptor = new FlipperOkhttpInterceptor(networkPlugin);

        // Normally, you would want to make this dependent on a BuildConfig flag, but
        // for this demo application we can safely assume that you always want to debug.
        ComponentsConfiguration.isDebugModeEnabled = true;
        LithoFlipperDescriptors.add(descriptorMapping);
        client.addPlugin(new InspectorFlipperPlugin(this, descriptorMapping));
        client.addPlugin(networkPlugin);
        client.addPlugin(
                new SharedPreferencesFlipperPlugin(
                        this,
                        Arrays.asList(
                                new SharedPreferencesDescriptor("sample", Context.MODE_PRIVATE),
                                new SharedPreferencesDescriptor("other_sample", Context.MODE_PRIVATE))));
        client.addPlugin(new LeakCanaryFlipperPlugin());
        client.addPlugin(new ExampleFlipperPlugin());
        client.addPlugin(CrashReporterPlugin.getInstance());
        client.start();

        getSharedPreferences("sample", Context.MODE_PRIVATE).edit().putString("Hello", "world").apply();
        getSharedPreferences("other_sample", Context.MODE_PRIVATE)
                .edit()
                .putInt("SomeKey", 1337)
                .apply();
    }

    ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            //在创建时设置
            mCurrentActivity = activity;
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            //在Activity声明周期时设置顶层Activity对象
            mCurrentActivity = activity;
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    public Activity getCurrentActivity() {
        return mCurrentActivity;
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
