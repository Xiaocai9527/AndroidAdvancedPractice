package com.xiaokun.advance_practive.ui.multi_rv_sample.utils;


import android.view.View;

import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemA;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemB;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemC;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemD;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemE;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.BaseMultiHoder;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public interface TypeFactory
{
    int type(ItemA itemA);

    int type(ItemB itemB);

    int type(ItemC itemC);

    int type(ItemD itemD);

    int type(ItemE itemE);

    BaseMultiHoder createViewHolder(View parent, int type);
}
