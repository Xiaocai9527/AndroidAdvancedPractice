package com.xiaokun.baselib.rx.util;

import android.support.annotation.NonNull;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.Subject;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/07
 *      描述  ：PublishProcessor存在onError后,不再接受消息。使用Relay来解决
 *      版本  ：1.0
 * </pre>
 */
public class RxBus3 {
    private static RxBus3 instance;

    /**
     * ConcurrentHashMap: 线程安全集合
     * PublishProcessor 同时充当了Observer和Observable的角色
     */
    @SuppressWarnings("rawtypes")
    private ConcurrentHashMap<Object, List<Relay>> subjectMapper = new ConcurrentHashMap<>();

    public static synchronized RxBus3 getInstance() {
        if (null == instance) {
            instance = new RxBus3();
        }
        return instance;
    }

    private RxBus3() {
    }

    /**
     * 订阅事件源
     *
     * @param observable
     * @param consumer
     * @return
     */
    public RxBus3 onEvent(Observable<?> observable, Consumer<Object> consumer) {
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        return getInstance();
    }

    /**
     * 注册事件源
     *
     * @param tag key
     * @param <T>
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    public <T> Relay<T> register(@NonNull Object tag) {
        List<Relay> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }

        //考虑到多线程原因使用toSerialized方法
        Relay<T> processor = (Relay<T>) PublishRelay.create().toSerialized();

        subjectList.add(processor);
        return processor;
    }

    /**
     * 取消整个tag的监听
     *
     * @param tag key
     */
    @SuppressWarnings("rawtypes")
    public void unregister(@NonNull Object tag) {
        List<Relay> subjectList = subjectMapper.get(tag);
        if (null != subjectList) {
            subjectMapper.remove(tag);
        }
    }

    /**
     * 取消tag里某个observable的监听
     *
     * @param tag        key
     * @param observable 要删除的observable
     * @return
     */
    @SuppressWarnings("rawtypes")
    public RxBus3 unregister(@NonNull Object tag, @NonNull Observable<?> observable) {
        if (null == observable) {
            return getInstance();
        }

        List<Relay> subjectList = subjectMapper.get(tag);
        if (null != subjectList) {
            // 从subjectList中删去observable
            subjectList.remove((Subject<?>) observable);
            // 若此时subjectList为空则从subjectMapper中删去
            if (isEmpty(subjectList)) {
                subjectMapper.remove(tag);
            }
        }
        return getInstance();
    }

    /**
     * 触发事件
     *
     * @param content
     */
    public void post(@NonNull Object content) {
        post(content.getClass().getName(), content);
    }

    /**
     * 触发事件
     *
     * @param tag     key
     * @param content
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void post(@NonNull Object tag, @NonNull Object content) {
        List<Relay> subjectList = subjectMapper.get(tag);
        if (!isEmpty(subjectList)) {
            for (Relay subject : subjectList) {
                subject.accept(content);
            }
        }
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 集合
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection<Relay> collection) {
        return null == collection || collection.isEmpty();
    }

}
