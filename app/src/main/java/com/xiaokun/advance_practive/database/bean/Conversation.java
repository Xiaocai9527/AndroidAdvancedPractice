package com.xiaokun.advance_practive.database.bean;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/18
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class Conversation {

    public long conversationId;
    //是否转接
    public int transfer;
    //是否历史会话
    public int history;
    //最后一条消息消息id
    public long lastMsgId;
    //单聊群聊
    public int conversationType;
    //会话用户id
    public long conversationUserId;
    //对方用户昵称
    public String nickName;
    //对方用户头像url
    public String avatar;


}
