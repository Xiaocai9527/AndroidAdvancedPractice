package com.xiaokun.advance_practive.im.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.xiaokun.advance_practive.im.database.DatabaseHelper;
import com.xiaokun.advance_practive.im.database.table.ConversationTable;
import com.xiaokun.advance_practive.im.database.bean.PdConversation;
import com.xiaokun.advance_practive.im.entity.Conversation;

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
public class ConversationDao {

    private static final String TAG = "ConversationDao";
    private SQLiteDatabase mDb;

    private ConversationDao() {
        mDb = DatabaseHelper.getDatabase();
    }

    public static ConversationDao getInstance() {
        return ConversationDaoHolder.sInstance;
    }

    private static class ConversationDaoHolder {
        private static final ConversationDao sInstance = new ConversationDao();
    }

    public boolean insert(PdConversation pdConversation) {
        if (pdConversation == null) {
            return false;
        }
        long result = mDb.replace(ConversationTable.TABLE_NAME, null, getContentValues(pdConversation));
        return result != -1;
    }

    /**
     * 查询所有会话
     *
     * @return
     */
    public List<PdConversation> queryAllConversations() {
        Cursor cursor = mDb.query(ConversationTable.TABLE_NAME, null,
                null, null, null, null, null);
        List<PdConversation> pdConversations = new ArrayList<>();
        while (cursor.moveToNext()) {
            pdConversations.add(getConversationByCursor(cursor));
        }
        cursor.close();
        return pdConversations;
    }

    /**
     * 更新某一条会话,一般用于最后一条消息的更新
     *
     * @param pdConversation
     * @return
     */
    public boolean updateConversation(PdConversation pdConversation) {
        if (pdConversation == null) {
            return false;
        }
        int result = mDb.update(ConversationTable.TABLE_NAME, getContentValues(pdConversation), ConversationTable.ID + "=?",
                new String[]{pdConversation.conversationId + ""});
        return result > 0;
    }

    /**
     * 更新会话
     *
     * @param imId
     * @param msgId
     * @return
     */
    public boolean updateLastMsgById(String imId, String msgId) {
        if (TextUtils.isEmpty(imId)) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(ConversationTable.LAST_MSG_ID, msgId);
        int result = mDb.update(ConversationTable.TABLE_NAME, values, ConversationTable.TO_CHAT_USER_IM_ID + "=?",
                new String[]{imId});
        return result > 0;
    }

    private ContentValues getContentValues(PdConversation pdConversation) {
        ContentValues values = new ContentValues();
        values.put(ConversationTable.TO_CHAT_USER_IM_ID, pdConversation.imUserId);
        values.put(ConversationTable.ID, pdConversation.conversationId);
        values.put(ConversationTable.TRANSFER, pdConversation.transfer.mType);
        values.put(ConversationTable.HISTORY, pdConversation.history.mType);
        values.put(ConversationTable.LAST_MSG_ID, pdConversation.lastMsgId);
        values.put(ConversationTable.CONVERSATION_TYPE, pdConversation.conversationType.mType);
        values.put(ConversationTable.CONVERSATION_USER_ID, pdConversation.conversationUserId);
        values.put(ConversationTable.NICK_NAME, pdConversation.nickName);
        values.put(ConversationTable.AVATAR, pdConversation.avatar);
        values.put(ConversationTable.UN_READ, pdConversation.unRead);
        return values;
    }

    private PdConversation getConversationByCursor(Cursor cursor) {
        PdConversation pdConversation = new PdConversation();
        pdConversation.imUserId = cursor.getString(ConversationTable.TO_CHAT_USER_IM_ID_COLUMN_INDEX);
        pdConversation.conversationId = cursor.getLong(ConversationTable.ID_COLUMN_INDEX);
        pdConversation.lastMsgId = cursor.getString(ConversationTable.LAST_MSG_ID_COLUMN_INDEX);

        switch (cursor.getInt(ConversationTable.TRANSFER_COLUMN_INDEX)) {
            case 1:
                pdConversation.transfer = PdConversation.TransferType.Normal;
                break;
            case 2:
                pdConversation.transfer = PdConversation.TransferType.Transfer;
                break;
            default:
                pdConversation.transfer = PdConversation.TransferType.Normal;
                break;
        }

        switch (cursor.getInt(ConversationTable.HISTORY_COLUMN_INDEX)) {
            case 1:
                pdConversation.history = PdConversation.HistoryType.Normal;
                break;
            case 2:
                pdConversation.history = PdConversation.HistoryType.History;
                break;
            default:
                pdConversation.history = PdConversation.HistoryType.Normal;
                break;
        }

        switch (cursor.getInt(ConversationTable.CONVERSATION_TYPE_COLUMN_INDEX)) {
            case 1:
                pdConversation.conversationType = PdConversation.ConversationType.Single;
                break;
            case 2:
                pdConversation.conversationType = PdConversation.ConversationType.Group;
                break;
            case 3:
                pdConversation.conversationType = PdConversation.ConversationType.ChatRoom;
                break;
            default:

                break;
        }
        pdConversation.conversationUserId = cursor.getLong(ConversationTable.CONVERSATION_USER_ID_COLUMN_INDEX);
        pdConversation.nickName = cursor.getString(ConversationTable.NICK_NAME_COLUMN_INDEX);
        pdConversation.avatar = cursor.getString(ConversationTable.AVATAR_COLUMN_INDEX);
        pdConversation.unRead = cursor.getInt(ConversationTable.UN_READ_COLUMN_INDEX);
        return pdConversation;
    }

    public boolean delete(PdConversation pdConversation) {
        if (pdConversation == null) {
            return false;
        }
        return deleteById(pdConversation.conversationId);
    }

    public boolean deleteById(long conversationId) {
        int result = mDb.delete(ConversationTable.TABLE_NAME, ConversationTable.ID + "=?", new String[]{conversationId + ""});
        return result != 0;
    }

    /**
     * 查询所有的会话
     *
     * @return
     */
    public List<PdConversation> queryConversations() {
        List<PdConversation> pdConversations = new ArrayList<>();
        Cursor cursor = mDb.rawQuery("select * from " + ConversationTable.TABLE_NAME, new String[]{});
        while (cursor.moveToNext()) {
            pdConversations.add(getConversationByCursor(cursor));
        }
        cursor.close();
        return pdConversations;
    }

    public PdConversation queryConversation(String toChatUserImId) {
        Cursor cursor = mDb.rawQuery("select * from " + ConversationTable.TABLE_NAME + " where to_chat_user_im_id =?",
                new String[]{toChatUserImId});
        if (cursor.moveToFirst()) {
            PdConversation conversationByCursor = getConversationByCursor(cursor);
            cursor.close();
            return conversationByCursor;
        } else {
            cursor.close();
            return null;
        }
    }

    public PdConversation queryConversation(String toChatUserImId, int type) {
        Cursor cursor = mDb.rawQuery("select * from " + ConversationTable.TABLE_NAME + " where to_chat_user_im_id =? and conversation_type =?",
                new String[]{toChatUserImId, type + ""});
        if (cursor.moveToFirst()) {
            PdConversation conversationByCursor = getConversationByCursor(cursor);
            cursor.close();
            return conversationByCursor;
        } else {
            cursor.close();
            return null;
        }
    }

    public List<PdConversation> queryConversationByType(PdConversation.ConversationType conversationType) {
        List<PdConversation> pdConversations = new ArrayList<>();
        Cursor cursor = mDb.rawQuery("select * from " + ConversationTable.TABLE_NAME + " where conversation_type =?",
                new String[]{conversationType.mType + ""});
        while (cursor.moveToNext()) {
            pdConversations.add(getConversationByCursor(cursor));
        }
        cursor.close();
        return pdConversations;
    }

}
