package com.xiaokun.advance_practive.im.adapter;

import android.support.v4.util.Pair;
import android.view.View;

import com.xiaokun.baselib.muti_rv.BaseMultiHodler;
import com.xiaokun.baselib.muti_rv.HolderFactory;
import com.xiaokun.baselib.util.RefInvoke;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/25
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class MsgHolderFactoryList implements HolderFactory {

    private HashMap<String, Pair<Integer, Integer>> mHoderHashMap = new HashMap<>();

    private MsgHolderFactoryList() {
    }

    public static MsgHolderFactoryList getInstance() {
        return MsgHolderFactoryListHolder.sInstance;
    }

    private static class MsgHolderFactoryListHolder {
        private static final MsgHolderFactoryList sInstance = new MsgHolderFactoryList();
    }

    /**
     * 暂不支持添加内部类holder
     *
     * @param classz
     * @param leftLayoutId  左边的layoutId
     * @param rightLayoutId 右边的layoutId
     * @return
     */
    public MsgHolderFactoryList addTypeHolder(Class<? extends BaseMultiHodler> classz, int leftLayoutId, int rightLayoutId) {
        mHoderHashMap.put(classz.getName(), new Pair<>(leftLayoutId, rightLayoutId));
        return this;
    }

    @Override
    public BaseMultiHodler createViewHolder(View parent, int type) {
        BaseMultiHodler baseMultiHodler = null;
        if (mHoderHashMap != null && !mHoderHashMap.isEmpty()) {
            for (Map.Entry<String, Pair<Integer, Integer>> classEntry : mHoderHashMap.entrySet()) {
                if (classEntry.getValue().first == type || classEntry.getValue().second == type) {
                    baseMultiHodler = (BaseMultiHodler) RefInvoke.createObject(classEntry.getKey(), View.class, parent);
                    //退出for循环,节省性能
                    break;
                }
            }
        } else {
            throw new IllegalArgumentException("需要添加holder");
        }
        return baseMultiHodler;
    }
}
