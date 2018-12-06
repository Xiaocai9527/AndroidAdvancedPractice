package com.xiaokun.httpexceptiondemo.ui.rxjava;

import com.xiaokun.httpexceptiondemo.App;
import com.xiaokun.httpexceptiondemo.Constants;
import com.xiaokun.httpexceptiondemo.network.entity.GankResEntity;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Scheduler;
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
public class CacheRepository {

    /**
     * 从文件缓存中获取
     *
     * @return
     */
    public Maybe<List<GankResEntity.DataBean>> getGankData() {
        return Maybe.create(new MaybeOnSubscribe<List<GankResEntity.DataBean>>() {
            @Override
            public void subscribe(MaybeEmitter<List<GankResEntity.DataBean>> e) throws Exception {
                List<GankResEntity.DataBean> dataBeanList = (List<GankResEntity.DataBean>) App.getCache().getAsObject(Constants.GANK_DATA);

                if (dataBeanList != null) {
                    e.onSuccess(dataBeanList);
                } else {
                    e.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
