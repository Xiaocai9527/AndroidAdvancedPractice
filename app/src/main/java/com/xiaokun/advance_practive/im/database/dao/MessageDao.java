package com.xiaokun.advance_practive.im.database.dao;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.xiaokun.advance_practive.im.database.DatabaseHelper;
import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.database.table.MessageTable;

import java.util.List;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/18
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class MessageDao {

    private SQLiteDatabase mDb;

    private MessageDao() {
        mDb = DatabaseHelper.getDatabase();

    }

    public static MessageDao getInstance() {
        return MessageDaoHolder.sInstance;
    }

    private static class MessageDaoHolder {
        private static final MessageDao sInstance = new MessageDao();
    }

    public boolean insertMsg(PdMessage pdMessage) {
        if (pdMessage == null) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(MessageTable.ID, pdMessage.imMsgId);
        values.put(MessageTable.FROM, pdMessage.msgSender);
        values.put(MessageTable.TO, pdMessage.msgReceiver);
        values.put(MessageTable.TYPE, pdMessage.msgType);
        values.put(MessageTable.READ, pdMessage.read);
        values.put(MessageTable.CONVERSATION_ID, pdMessage.sessionId);
        values.put(MessageTable.CONTENT, pdMessage.msgContent);
        values.put(MessageTable.CHAT_TYPE, pdMessage.msgChatType.type);
        values.put(MessageTable.DIRECTION, pdMessage.msgDirection.direction);
        values.put(MessageTable.STATUS, pdMessage.msgStatus.status);
        long result = mDb.insert(MessageTable.TABLE_NAME, null, values);
        return result != -1;
    }

    public boolean insertMsgs(List<PdMessage> pdMessages) {
        if (pdMessages == null) {
            return false;
        }
        for (PdMessage pdMessage : pdMessages) {
            if (!insertMsg(pdMessage)) {
                return false;
            }
        }
        return true;
    }

    public boolean updateMsg(PdMessage pdMessage) {
        if (pdMessage == null) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(MessageTable.ID, pdMessage.imMsgId);
        values.put(MessageTable.FROM, pdMessage.msgSender);
        values.put(MessageTable.TO, pdMessage.msgReceiver);
        values.put(MessageTable.TYPE, pdMessage.msgType);
        values.put(MessageTable.READ, pdMessage.read);
        values.put(MessageTable.CONVERSATION_ID, pdMessage.sessionId);
        values.put(MessageTable.CONTENT, pdMessage.msgContent);
        values.put(MessageTable.CHAT_TYPE, pdMessage.msgChatType.type);
        values.put(MessageTable.DIRECTION, pdMessage.msgDirection.direction);
        values.put(MessageTable.STATUS, pdMessage.msgStatus.status);

        int result = mDb.update(MessageTable.TABLE_NAME, values, MessageTable.ID + "=?", new String[]{pdMessage.imMsgId});
        return result > 0;
    }

    public boolean deleteMsg(PdMessage pdMessage) {
        if (pdMessage == null) {
            return false;
        }
        return deleteMsgById(pdMessage.businessId);
    }

    public boolean deleteMsgById(long pdMessageId) {
        int result = mDb.delete(MessageTable.TABLE_NAME, MessageTable.ID + "=?", new String[]{pdMessageId + ""});
        return result != 0;
    }

}
