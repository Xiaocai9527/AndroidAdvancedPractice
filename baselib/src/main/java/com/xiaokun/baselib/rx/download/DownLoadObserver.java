package com.xiaokun.baselib.rx.download;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/20
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public abstract class DownLoadObserver implements Observer<ResponseBody>
{

    @Override
    public abstract void onSubscribe(Disposable d);

    @Override
    public void onNext(ResponseBody responseBody)
    {
        //这里不用做什么
    }

    @Override
    public void onError(Throwable e)
    {
        //这里不用做什么
    }

    @Override
    public void onComplete()
    {
        //这里不用做什么
    }

}
