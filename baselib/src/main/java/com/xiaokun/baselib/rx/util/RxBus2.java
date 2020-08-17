package com.xiaokun.baselib.rx.util;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subjects.Subject;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/28
 *      描述  ：PublishProcessor存在onError后,不再接受消息。使用Relay来解决
 *              请使用RxBus3代替RxBus2
 *      版本  ：1.0
 * </pre>
 */
public class RxBus2 {
    /**
     * ConcurrentHashMap: 线程安全集合
     * PublishProcessor 同时充当了Observer和Observable的角色
     */
    @SuppressWarnings("rawtypes")
    private ConcurrentHashMap<Object, List<PublishProcessor>> subjectMapper = new ConcurrentHashMap<>();

    private RxBus2() {
    }

    public static RxBus2 getInstance() {
        return RxBus2Holder.sInstance;
    }

    private static class RxBus2Holder {
        private static final RxBus2 sInstance = new RxBus2();
    }

    /**
     * 注册事件源
     *
     * @param tag key
     * @param <T>
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    public <T> Flowable<T> register(@NonNull Object tag) {
        List<PublishProcessor> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }

        //考虑到多线程原因使用toSerialized方法
        PublishProcessor<T> processor = (PublishProcessor<T>) PublishProcessor.create().toSerialized();
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
        List<PublishProcessor> subjectList = subjectMapper.get(tag);
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
    public RxBus2 unregister(@NonNull Object tag,
                             @NonNull Observable<?> observable) {
        if (null == observable) {
            return getInstance();
        }

        List<PublishProcessor> subjectList = subjectMapper.get(tag);
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
        List<PublishProcessor> subjectList = subjectMapper.get(tag);
        if (!isEmpty(subjectList)) {
            for (PublishProcessor subject : subjectList) {
                subject.onNext(content);
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
    public static boolean isEmpty(Collection<PublishProcessor> collection) {
        return null == collection || collection.isEmpty();
    }
}
