package com.xiaokun.baselib.rx.transform;

import io.reactivex.ObservableTransformer;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/19
 *     描述   : 线程切换
 *     版本   : 1.0
 * </pre>
 */
public class RxSchedulers
{
    public static <T> ObservableTransformer<T, T> io_main()
    {
        return new SchedulerTransformer<T>();
    }
}
