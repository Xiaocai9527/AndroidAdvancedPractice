package com.xiaokun.advance_practive.im.util;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/22
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class Utils {

    public static View createView(@LayoutRes int layoutId, ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
    }
}
