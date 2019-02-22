package com.xiaokun.advance_practive.im.entity;

import com.xiaokun.advance_practive.R;
import com.xiaokun.baselib.muti_rv.MultiItem;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/21
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class Conversation implements MultiItem {

    public String userImId;
    public boolean transfer;
    public boolean history;
    public String nickName;
    public String url;
    public String msgContent;
    public long updateTime;

    @Override
    public int getItemType() {
        return R.layout.item_customer_dialogue;
    }
}
