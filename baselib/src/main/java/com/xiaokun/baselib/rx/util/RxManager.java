package com.xiaokun.baselib.rx.util;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/19
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class RxManager
{
    public RxBus mRxBus = RxBus.getInstance();

    /**
     * 管理观察源
     */
    private Map<String, Observable<?>> mObservableMap = new HashMap<>();

    /**
     * 管理订阅者
     */
    private CompositeDisposable mCompositeSubscription = new CompositeDisposable();


    public void on(String eventName, Consumer<Object> consumer)
    {
        // 注册
        Observable<?> mObservable = mRxBus.register(eventName);

        mObservableMap.put(eventName, mObservable);

        mCompositeSubscription
                .add(mObservable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(consumer, new Consumer<Throwable>()
                        {
                            @Override
                            public void accept(Throwable throwable) throws Exception
                            {
                                throwable.printStackTrace();
                            }
                        }));
    }

    /**
     * 添加订阅者到mCompositeSubscription
     *
     * @param m 要添加的订阅者
     */
    public void add(Disposable m)
    {
        if (mCompositeSubscription != null)
        {
            mCompositeSubscription.add(m);
        }
    }

    /**
     * @param disposable
     */
    public void remove(Disposable disposable)
    {
        if (mCompositeSubscription != null)
        {
            mCompositeSubscription.remove(disposable);
        }
    }

    /**
     * 取消所有注册
     */
    public void clear()
    {
        if (mRxBus != null && mCompositeSubscription != null && mObservableMap != null)
        {
            // 取消订阅
            mCompositeSubscription.dispose();
            for (Map.Entry<String, Observable<?>> entry : mObservableMap.entrySet())
            {
                // 取消注册
                mRxBus.unregister(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 触发事件
     *
     * @param tag
     * @param content
     */
    public void post(Object tag, Object content)
    {
        if (mRxBus != null)
        {
            mRxBus.post(tag, content);
        }
    }
}
