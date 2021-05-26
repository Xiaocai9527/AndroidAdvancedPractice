package com.xiaokun.advance_practive.robolectric;

import android.text.format.DateUtils;
import android.util.Log;

import com.xiaokun.advance_practive.BuildConfig;
import com.xiaokun.advance_practive.ui.rxjava.RxjavaActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.Formatter;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.observers.CallbackCompletableObserver;

/**
 * Created by 肖坤 on 2018/12/28.
 *
 * @author 肖坤
 * @date 2018/12/28
 */
@RunWith(RobolectricTestRunner.class)

public class RobolectricRxJavaActivity {
    private static final String TAG = "RobolectricRxJavaActivi";

    private RxjavaActivity rxjavaActivity;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
        // rxjavaActivity = Robolectric.setupActivity(RxjavaActivity.class);
    }

    @Test
    public void testFrom() {
        //rxjavaActivity.from();


        Observable.fromArray(1, 2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, integer + "");
            }
        }, new CallbackCompletableObserver(new Action() {
            @Override
            public void run() throws Exception {
                Log.e(TAG, "onComplete");
            }
        }));

        Observable.just(1, 2, 3, 4)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "");
                    }
                }, new CallbackCompletableObserver(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "onComplete");
                    }
                }));

    }

    @Test
    public void testDate1() {
        long timeMillis = System.currentTimeMillis() - (60 * 24 * 60 * 60 * 1000);
        Formatter mFormatter = new Formatter(new StringBuilder(0));
        System.out.println(DateUtils.formatDateRange(rxjavaActivity, mFormatter, timeMillis,
                timeMillis, DateUtils.FORMAT_NUMERIC_DATE).toString());
    }

}
