package com.xiaokun.baselib.muti_rv;

import android.view.View;

import com.xiaokun.baselib.util.RefInvoke;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class HolderFactoryList implements HolderFactory {

    private HashMap<Integer, Class<? extends BaseMultiHodler>> mHoderHashMap = new HashMap<>();

    public static HolderFactoryList getInstance() {
        return new HolderFactoryList();
    }

    private HolderFactoryList() {
    }

    public HolderFactoryList addTypeHolders(HashMap<Integer, Class<? extends BaseMultiHodler>> hoderHashMap) {
        mHoderHashMap.putAll(hoderHashMap);
        return this;
    }

    /**
     * 暂不支持添加内部类holder
     *
     * @param classz
     * @param layoutId
     * @return
     */
    public HolderFactoryList addTypeHolder(Class<? extends BaseMultiHodler> classz, int layoutId) {
        mHoderHashMap.put(layoutId, classz);
        return this;
    }

    @Override
    public BaseMultiHodler createViewHolder(View parent, int type) {
        BaseMultiHodler baseMultiHodler = null;
        if (mHoderHashMap != null && !mHoderHashMap.isEmpty()) {
            for (Map.Entry<Integer, Class<? extends BaseMultiHodler>> classEntry : mHoderHashMap.entrySet()) {
                if (classEntry.getKey() == type) {
                    baseMultiHodler = (BaseMultiHodler) RefInvoke.createObject(classEntry.getValue(), View.class, parent);
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
