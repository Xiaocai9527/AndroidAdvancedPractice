package com.xiaokun.advance_practive;

import com.xiaokun.baselib.util.ContextHolder;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowLog;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/29
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class RxJavaRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        //log打印
        ShadowLog.stream = System.out;
        //获取context application级别的
        ContextHolder.setContext(RuntimeEnvironment.application);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.reset();
                RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
                    @Override
                    public Scheduler apply(Scheduler scheduler) throws Exception {
                        //返回队列工作的线程
                        return Schedulers.trampoline();
                    }
                });
                RxAndroidPlugins.reset();
                RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
                    @Override
                    public Scheduler apply(Scheduler scheduler) throws Exception {
                        //返回队列工作的线程
                        return Schedulers.trampoline();
                    }
                });

                try {
                    base.evaluate();
                } finally {
                    RxJavaPlugins.reset();
                    RxAndroidPlugins.reset();
                }
            }
        };
    }
}
