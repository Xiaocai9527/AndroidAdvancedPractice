package com.xiaokun.advance_practive.im.database.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.xiaokun.advance_practive.im.database.DatabaseHelper;
import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.database.table.MessageTable;

import java.util.ArrayList;
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
        values.put(MessageTable.READ, pdMessage.read.mType);
        values.put(MessageTable.CONVERSATION_ID, pdMessage.conversationId);
        values.put(MessageTable.CONTENT, pdMessage.msgContent);
        values.put(MessageTable.CHAT_TYPE, pdMessage.msgChatType.type);
        values.put(MessageTable.DIRECTION, pdMessage.msgDirection.direction);
        values.put(MessageTable.STATUS, pdMessage.msgStatus.status);
        values.put(MessageTable.UPDATE_TIME, pdMessage.updateTime);
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
        values.put(MessageTable.READ, pdMessage.read.mType);
        values.put(MessageTable.CONVERSATION_ID, pdMessage.conversationId);
        values.put(MessageTable.CONTENT, pdMessage.msgContent);
        values.put(MessageTable.CHAT_TYPE, pdMessage.msgChatType.type);
        values.put(MessageTable.DIRECTION, pdMessage.msgDirection.direction);
        values.put(MessageTable.STATUS, pdMessage.msgStatus.status);

        int result = mDb.update(MessageTable.TABLE_NAME, values, MessageTable.ID + "=?", new String[]{pdMessage.imMsgId});
        return result > 0;
    }

    /**
     * 更新消息状态
     *
     * @param msgId
     * @return
     */
    public boolean updateMsgStatusById(String msgId) {
        if (TextUtils.isEmpty(msgId)) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(MessageTable.STATUS, PdMessage.PDMessageStatus.SUCCESS.status);
        int result = mDb.update(MessageTable.TABLE_NAME, values, MessageTable.ID + "=?", new String[]{msgId});
        return result > 0;
    }

    /**
     * 当前会话所有消息置为已读
     *
     * @param imUserId
     * @return
     */
    public boolean updateMsgsAsReadByConversationId(String imUserId) {
        if (TextUtils.isEmpty(imUserId)) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(MessageTable.READ, PdMessage.PDRead.READ.mType);
        int result = mDb.update(MessageTable.TABLE_NAME, values, MessageTable.READ + "=?", new String[]{imUserId});
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

    public PdMessage queryMsgById(String msgId) {
        if (TextUtils.isEmpty(msgId)) {
            return null;
        }
        Cursor cursor = mDb.rawQuery("select * from " + MessageTable.TABLE_NAME + " where message_id =?", new String[]{msgId});
        if (cursor.moveToFirst()) {
            PdMessage messageByCursor = getMessageByCursor(cursor);
            cursor.close();
            return messageByCursor;
        } else {
            cursor.close();
            return null;
        }
    }

    /**
     * 通过会话id查询所属这个会话的所有消息
     *
     * @param conversationId
     * @return
     */
    public List<PdMessage> queryMsgsByConversationId(String conversationId) {
        if (TextUtils.isEmpty(conversationId)) {
            return null;
        }
        Cursor cursor = mDb.rawQuery("select * from " + MessageTable.TABLE_NAME + " where conversation_id =?", new String[]{conversationId});
        List<PdMessage> pdMessages = new ArrayList<>();
        while (cursor.moveToNext()) {
            PdMessage messageByCursor = getMessageByCursor(cursor);
            pdMessages.add(messageByCursor);
        }
        cursor.close();
        return pdMessages;
    }

    /**
     * 通过会话id查询所属这个会话的所有未读消息
     *
     * @param conversationId
     * @return
     */
    public List<PdMessage> queryUnreadMsgsByConversationId(String conversationId) {
        if (TextUtils.isEmpty(conversationId)) {
            return null;
        }
        Cursor cursor = mDb.rawQuery("select * from " + MessageTable.TABLE_NAME + " where conversation_id =? and read =?",
                new String[]{conversationId, "2"});
        List<PdMessage> pdMessages = new ArrayList<>();
        while (cursor.moveToNext()) {
            PdMessage messageByCursor = getMessageByCursor(cursor);
            pdMessages.add(messageByCursor);
        }
        cursor.close();
        return pdMessages;
    }

    public boolean updateMsgAsReadByMsgId(String imMsgId) {
        if (TextUtils.isEmpty(imMsgId)) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MessageTable.READ, PdMessage.PDRead.READ.mType);
        mDb.update(MessageTable.TABLE_NAME, contentValues, MessageTable.READ + "=?", new String[]{imMsgId});
        return false;
    }

    private PdMessage getMessageByCursor(Cursor cursor) {
        PdMessage pdMessage = new PdMessage();
        pdMessage.imMsgId = cursor.getString(MessageTable.ID_COLUMN_INDEX);
        pdMessage.msgSender = cursor.getString(MessageTable.FROM_COLUMN_INDEX);
        pdMessage.msgReceiver = cursor.getString(MessageTable.TO_COLUMN_INDEX);
        pdMessage.msgType = cursor.getInt(MessageTable.TYPE_COLUMN_INDEX);
        pdMessage.conversationId = cursor.getString(MessageTable.CONVERSATION_ID_COLUMN_INDEX);
        pdMessage.msgContent = cursor.getString(MessageTable.CONTENT_COLUMN_INDEX);
        pdMessage.updateTime = cursor.getLong(MessageTable.UPDATE_TIME_COLUMN_INDEX);

        switch (cursor.getInt(MessageTable.READ_COLUMN_INDEX)) {
            case 1:
                pdMessage.read = PdMessage.PDRead.READ;
                break;
            case 2:
                pdMessage.read = PdMessage.PDRead.UNREAD;
                break;
            default:
                pdMessage.read = PdMessage.PDRead.READ;
                break;
        }

        switch (cursor.getInt(MessageTable.STATUS_COLUMN_INDEX)) {
            case 0:
                pdMessage.msgStatus = PdMessage.PDMessageStatus.INVAILD;
                break;
            case 1:
                pdMessage.msgStatus = PdMessage.PDMessageStatus.NEW;
                break;
            case 2:
                pdMessage.msgStatus = PdMessage.PDMessageStatus.DELIVERING;
                break;
            case 3:
                pdMessage.msgStatus = PdMessage.PDMessageStatus.SUCCESS;
                break;
            case 4:
                pdMessage.msgStatus = PdMessage.PDMessageStatus.FAIL;
                break;
            default:
                pdMessage.msgStatus = PdMessage.PDMessageStatus.SUCCESS;
                break;
        }

        switch (cursor.getInt(MessageTable.DIRECTION_COLUMN_INDEX)) {
            case 1:
                pdMessage.msgDirection = PdMessage.PDDirection.SEND;
                break;
            case 2:
                pdMessage.msgDirection = PdMessage.PDDirection.RECEIVE;
                break;
            default:
                pdMessage.msgDirection = PdMessage.PDDirection.SEND;
                break;
        }

        switch (cursor.getInt(MessageTable.CHAT_TYPE_COLUMN_INDEX)) {
            case 1:
                pdMessage.msgChatType = PdMessage.PDChatType.SINGLE;
                break;
            case 2:
                pdMessage.msgChatType = PdMessage.PDChatType.GROUP;
                break;
            case 3:
                pdMessage.msgChatType = PdMessage.PDChatType.WECHAT;
                break;
            case 4:
                pdMessage.msgChatType = PdMessage.PDChatType.CHAT_ROOM;
                break;
            default:
                pdMessage.msgChatType = PdMessage.PDChatType.SINGLE;
                break;
        }
        return pdMessage;
    }

}
