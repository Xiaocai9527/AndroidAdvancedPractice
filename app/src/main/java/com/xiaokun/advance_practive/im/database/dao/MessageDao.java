package com.xiaokun.advance_practive.im.database.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.xiaokun.advance_practive.im.database.DatabaseHelper;
import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.database.table.MessageTable;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * 添加一条消息
     *
     * @param pdMessage
     * @return
     */
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
        long result = mDb.replace(MessageTable.TABLE_NAME, null, values);
        return result != -1;
    }

    /**
     * 添加多条消息
     *
     * @param pdMessages
     * @return
     */
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

    /**
     * 更新一条消息
     *
     * @param pdMessage
     * @return
     */
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

    /**
     * 删除一条消息
     *
     * @param pdMessage
     * @return
     */
    public boolean deleteMsg(PdMessage pdMessage) {
        if (pdMessage == null) {
            return false;
        }
        return deleteMsgById(pdMessage.businessId);
    }

    /**
     * 删除一条消息
     *
     * @param pdMessageId
     * @return
     */
    public boolean deleteMsgById(long pdMessageId) {
        int result = mDb.delete(MessageTable.TABLE_NAME, MessageTable.ID + "=?", new String[]{pdMessageId + ""});
        return result != 0;
    }

    /**
     * 通过消息id来获取消息,通常是用来获取会话表中最后一条消息
     *
     * @param msgId
     * @return
     */
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

    /**
     * 从表中分页获取数据
     *
     * @param conversationId
     * @param limit
     * @param offset
     * @return
     */
    public List<PdMessage> loadMsgsPagination(String conversationId, int limit, int offset) {
        Cursor cursor = mDb.rawQuery("select * from " + MessageTable.TABLE_NAME + " where conversation_id =?" +
                " order by update_time desc limit " + limit + " offset " + offset, new String[]{conversationId});
        List<PdMessage> pdMessages = new ArrayList<>();
        while (cursor.moveToNext()) {
            PdMessage messageByCursor = getMessageByCursor(cursor);
            pdMessages.add(messageByCursor);
        }
        //反序一下
        Collections.reverse(pdMessages);
        cursor.close();
        return pdMessages;
    }

    /**
     * 更新消息为已读
     *
     * @param imMsgId
     * @return
     */
    public boolean updateMsgAsReadByMsgId(String imMsgId) {
        if (TextUtils.isEmpty(imMsgId)) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MessageTable.READ, PdMessage.PDRead.READ.mType);
        int result = mDb.update(MessageTable.TABLE_NAME, contentValues, MessageTable.ID + "=?", new String[]{imMsgId});
        return result > 0;
    }

    /**
     * 更新消息发送失败状态
     *
     * @param message
     */
    public boolean updateMsgFailStatus(PdMessage message) {
        if (message == null || TextUtils.isEmpty(message.imMsgId)) {
            return false;
        }
        return updateMsgFailStatusById(message.imMsgId);
    }

    /**
     * 更新消息发送失败状态
     *
     * @param msgId
     */
    public boolean updateMsgFailStatusById(String msgId) {
        if (TextUtils.isEmpty(msgId)) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(MessageTable.STATUS, PdMessage.PDMessageStatus.FAIL.status);
        int result = mDb.update(MessageTable.TABLE_NAME, values, MessageTable.ID + "=?", new String[]{msgId});
        return result > 0;
    }

    /**
     * 更新消息发送成功状态
     *
     * @param message
     */
    public boolean updateMsgSucStatus(PdMessage message) {
        if (message == null || TextUtils.isEmpty(message.imMsgId)) {
            return false;
        }
        return updateMsgSucStatusById(message.imMsgId);
    }

    /**
     * 更新消息发送成功状态
     *
     * @param msgId
     */
    public boolean updateMsgSucStatusById(String msgId) {
        if (TextUtils.isEmpty(msgId)) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(MessageTable.STATUS, PdMessage.PDMessageStatus.SUCCESS.status);
        int result = mDb.update(MessageTable.TABLE_NAME, values, MessageTable.ID + "=?", new String[]{msgId});
        return result > 0;
    }

    /**
     * 获取所有发送中的消息
     *
     * @return
     */
    public List<PdMessage> getDeliverMsgs() {
        Cursor cursor = mDb.rawQuery("select * from " + MessageTable.TABLE_NAME + " where status=?", new String[]{"2"});

        List<PdMessage> pdMessages = new ArrayList<>();
        while (cursor.moveToNext()) {
            pdMessages.add(getMessageByCursor(cursor));
        }
        return pdMessages;
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
