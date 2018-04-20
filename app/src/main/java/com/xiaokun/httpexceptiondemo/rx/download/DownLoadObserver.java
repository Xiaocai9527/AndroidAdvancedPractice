package com.xiaokun.httpexceptiondemo.rx.download;

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
    public void onSubscribe(Disposable d)
    {

    }

    public abstract void onNext(ResponseBody responseBody);


    public abstract void onError(Throwable e);

    @Override
    public void onComplete()
    {

    }

}
