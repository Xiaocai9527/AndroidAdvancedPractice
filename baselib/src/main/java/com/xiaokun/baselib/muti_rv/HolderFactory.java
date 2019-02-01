package com.xiaokun.baselib.muti_rv;


import android.view.View;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public interface HolderFactory {
    BaseMultiHodler createViewHolder(View parent, int type);
}
