package com.xiaokun.advance_practive.ui.multi_rv_sample.utils;


import android.view.View;

import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.BaseMultiHoder;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public interface HolderFactory {
    BaseMultiHoder createViewHolder(View parent, int type);
}
