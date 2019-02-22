package com.xiaokun.advance_practive.im.database.bean.msgBody;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/18
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class PdTextMsgBody extends PdMsgBody {

    public String content;

    @Override
    public int getMsgType() {
        return PDMessageBodyType_TEXT;
    }
}
