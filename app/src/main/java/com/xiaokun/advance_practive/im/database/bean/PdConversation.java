package com.xiaokun.advance_practive.im.database.bean;

import com.xiaokun.advance_practive.im.database.dao.MessageDao;

import java.util.List;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/18
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class PdConversation {

    public long conversationId;
    //是否转接
    public TransferType transfer;
    //是否历史会话
    public HistoryType history;
    //最后一条消息消息id
    public String lastMsgId;
    //单聊群聊
    public ConversationType conversationType;
    //会话用户id
    public long conversationUserId;
    //对方用户昵称
    public String nickName;
    //对方用户头像url
    public String avatar;
    //对方用户im账号
    public String imUserId;
    //未读消息数量
    public int unRead;

    /**
     * 获取未读消息数量
     *
     * @return
     */
    public int getUnReadCount() {
        List<PdMessage> pdMessages = MessageDao.getInstance().queryMsgsByConversationId(imUserId);
        int count = 0;
        for (PdMessage pdMessage : pdMessages) {
            if (pdMessage.read == PdMessage.PDRead.UNREAD) {
                count++;
            }
        }
        return count;
    }

    public void markAllMessagesAsRead() {

        List<PdMessage> pdMessages = MessageDao.getInstance().queryMsgsByConversationId(imUserId);
        for (PdMessage pdMessage : pdMessages) {
            pdMessage.read = PdMessage.PDRead.READ;
        }
    }

    public enum HistoryType {
        //
        Normal(1, "普通会话"),
        History(2, "历史会话");

        public int mType;
        public String mDesc;

        HistoryType(int type, String desc) {
            mType = type;
            mDesc = desc;
        }
    }

    public enum TransferType {
        //
        Normal(1, "普通会话"),
        Transfer(2, "转接会话"),;

        public int mType;
        public String mDesc;

        TransferType(int type, String desc) {
            mType = type;
            mDesc = desc;
        }
    }

    public static enum ConversationType {
        //
        Single(1, "单聊"),
        Group(2, "群聊"),
        ChatRoom(3, "聊天室");

        public int mType;
        public String mDesc;

        ConversationType(int type, String desc) {
            mType = type;
            mDesc = desc;
        }

    }

}
