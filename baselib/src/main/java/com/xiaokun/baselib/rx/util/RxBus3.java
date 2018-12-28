package com.xiaokun.baselib.rx.util;

import android.support.annotation.NonNull;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
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

    /**
     * ConcurrentHashMap: 线程安全集合
     * PublishProcessor 同时充当了Observer和Observable的角色
     */
    @SuppressWarnings("rawtypes")
    private ConcurrentHashMap<Object, List<Relay>> subjectMapper = new ConcurrentHashMap<>();
    /**
     * 粘性
     */
    private ConcurrentHashMap<Object, Relay> stickSubjectMapper = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Object, Boolean> mHashMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Object, Object> mHashMap2 = new ConcurrentHashMap<>();

    private RxBus3() {
    }

    public static RxBus3 getInstance() {
        return RxBus3Holder.sInstance;
    }

    private static class RxBus3Holder {
        private static final RxBus3 sInstance = new RxBus3();
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
     * 注册粘性事件
     */
    public <T> Relay<T> registerStick(@NonNull Object tag, @NonNull Consumer<T> consumer) {
        Relay relay = stickSubjectMapper.get(tag);
        if (null == relay) {
            relay = PublishRelay.create().toSerialized();
        }
        //如果已经post,直接订阅和发送
        if (mHashMap.get(tag)) {
            //先订阅
            relay.subscribe(consumer);
            //后发送
            relay.accept(mHashMap2.get(tag));
            return relay;
        }
        return relay;
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
     * 取消tag粘性监听
     *
     * @param tag
     */
    public void unregisterStick(@NonNull Object tag) {
        Relay relay = stickSubjectMapper.get(tag);
        if (null != relay) {
            stickSubjectMapper.remove(tag);
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
     * 发送粘性事件
     *
     * @param tag
     */
    public <T> void postStick(@NonNull Object tag, T t) {
        //做一个粘性标记
        mHashMap.put(tag, true);
        mHashMap2.put(tag, t);
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
