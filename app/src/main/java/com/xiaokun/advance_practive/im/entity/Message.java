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
public class Message extends PdMessage implements MsgItem {

    public String avatarUrl;

    @LayoutRes
    public int leftItemLayoutId;

    @LayoutRes
    public int rightItemLayoutId;

    @Override
    public int getItemType() {
        //这个不用管
        return 0;
    }

    @Override
    public int getLeftItemType() {
        return leftItemLayoutId;
    }

    @Override
    public int getRightItemType() {
        return rightItemLayoutId;
    }

    @Override
    public PdMessage.PDDirection getDirection() {
        return msgDirection;
    }
}
