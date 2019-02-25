package com.xiaokun.advance_practive.im.entity;

import android.support.annotation.LayoutRes;

import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.baselib.muti_rv.MultiItem;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/25
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public interface MsgItem extends MultiItem {

    @LayoutRes
    int getLeftItemType();

    @LayoutRes
    int getRightItemType();

    PdMessage.PDDirection getDirection();

}
