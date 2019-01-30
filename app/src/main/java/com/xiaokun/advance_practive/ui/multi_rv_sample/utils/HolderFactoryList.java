package com.xiaokun.advance_practive.ui.multi_rv_sample.utils;

import android.view.View;

import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.BaseMultiHoder;
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

    private HashMap<Integer, Class<? extends BaseMultiHoder>> mHoderHashMap = new HashMap<>();

    public static HolderFactoryList getInstance() {
        return new HolderFactoryList();
    }

    private HolderFactoryList() {
    }

    public void addTypeHolders(HashMap<Integer, Class<? extends BaseMultiHoder>> hoderHashMap) {
        mHoderHashMap.putAll(hoderHashMap);
    }

    public void addTypeHolder(Class<? extends BaseMultiHoder> classz, int layoutId) {
        mHoderHashMap.put(layoutId, classz);
    }

    @Override
    public BaseMultiHoder createViewHolder(View parent, int type) {
        BaseMultiHoder baseMultiHoder = null;
        if (mHoderHashMap != null) {
            for (Map.Entry<Integer, Class<? extends BaseMultiHoder>> classEntry : mHoderHashMap.entrySet()) {
                if (classEntry.getKey() == type) {
                    baseMultiHoder = (BaseMultiHoder) RefInvoke.createObject(classEntry.getValue(), View.class, parent);
                    //退出for循环,节省性能
                    break;
                }
            }
        } else {
            throw new IllegalArgumentException("需要添加holder");
        }
        return baseMultiHoder;
    }
}
