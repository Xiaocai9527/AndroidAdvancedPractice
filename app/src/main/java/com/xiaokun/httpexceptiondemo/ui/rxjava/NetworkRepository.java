package com.xiaokun.httpexceptiondemo.ui.rxjava;

import com.xiaokun.httpexceptiondemo.network.entity.GankResEntity;
import com.xiaokun.baselib.rx.transform.HttpResultFunc;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/10/25
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class NetworkRepository {

    private FakeGankApiService mApiService;

    public NetworkRepository(FakeGankApiService apiService) {
        mApiService = apiService;
    }

    public Single<List<GankResEntity.DataBean>> getGankData() {
        return mApiService.getGankData()
                .map(new HttpResultFunc<List<GankResEntity.DataBean>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
