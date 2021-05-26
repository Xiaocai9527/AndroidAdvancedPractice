package com.xiaokun.advance_practive.rxjava;

import android.util.Log;

import com.xiaokun.advance_practive.BuildConfig;
import com.xiaokun.advance_practive.RxJavaRule;
import com.xiaokun.advance_practive.RetrofitUtilTest;
import com.xiaokun.advance_practive.network.NetworkHelper;
import com.xiaokun.advance_practive.network.api.WanApiService;
import com.xiaokun.advance_practive.network.wanAndroid.TotalResEntity;
import com.xiaokun.baselib.rx.transform.WanHttpResultFunc;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/29
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
@RunWith(RobolectricTestRunner.class)

public class RxJavaApiTest {

    private static final String TAG = "RxJavaApiTest";

    @Rule
    public RxJavaRule mRxJavaRule = new RxJavaRule();
    private WanApiService mApiService;
    private NetworkHelper mNetworkHelper;

    @Before
    public void setUp() {
        mApiService = RetrofitUtilTest.getInstance().getRetrofit(WanApiService.class);
        mNetworkHelper = new NetworkHelper(mApiService);
    }

    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();

    @Test
    public void testHomeBanner() {
        mNetworkHelper.getHomeBanner()
                .subscribe(new Consumer<List<TotalResEntity.HomeBanner>>() {
                    @Override
                    public void accept(List<TotalResEntity.HomeBanner> homeBanners) throws Exception {
                        Log.e(TAG, homeBanners.size() + "");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @Test
    public void testHomeArticles() {
        mNetworkHelper.getHomeArticles(1)
                .subscribe(new Consumer<TotalResEntity.HomeArticles>() {
                    @Override
                    public void accept(TotalResEntity.HomeArticles homeArticles) throws Exception {
                        Log.e(TAG, homeArticles.datas.size() + "");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
}
