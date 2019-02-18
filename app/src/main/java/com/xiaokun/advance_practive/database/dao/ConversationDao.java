package com.xiaokun.advance_practive.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xiaokun.advance_practive.database.DatabaseHelper;
import com.xiaokun.advance_practive.database.table.ConversationTable;
import com.xiaokun.advance_practive.database.bean.Conversation;

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

    public boolean insert(Conversation conversation) {
        ContentValues values = new ContentValues();
        values.put(ConversationTable.ID, conversation.conversationId);
        values.put(ConversationTable.TRANSFER, conversation.transfer);
        values.put(ConversationTable.HISTORY, conversation.history);
        values.put(ConversationTable.LAST_MSG_ID, conversation.lastMsgId);
        values.put(ConversationTable.CONVERSATION_TYPE, conversation.conversationType);
        values.put(ConversationTable.CONVERSATION_USER_ID, conversation.conversationUserId);
        long result = mDb.insert(ConversationTable.TABLE_NAME, null, values);
        return result != -1;
    }

    /**
     * 查询所有会话
     *
     * @return
     */
    public List<Conversation> queryAllConversations() {
        Cursor cursor = mDb.query(ConversationTable.TABLE_NAME, null,
                null, null, null, null, null);
        List<Conversation> conversations = new ArrayList<>();
        while (cursor.moveToNext()) {
            Conversation conversation = new Conversation();
            conversation.conversationId = cursor.getLong(ConversationTable.ID_COLUMN_INDEX);
            conversation.transfer = cursor.getInt(ConversationTable.TRANSFER_COLUMN_INDEX);
            conversation.history = cursor.getInt(ConversationTable.HISTORY_COLUMN_INDEX);
            conversation.lastMsgId = cursor.getLong(ConversationTable.LAST_MSG_ID_COLUMN_INDEX);
            conversation.conversationType = cursor.getInt(ConversationTable.CONVERSATION_TYPE_COLUMN_INDEX);
            conversation.conversationUserId = cursor.getLong(ConversationTable.CONVERSATION_USER_ID_COLUMN_INDEX);
            conversation.nickName = cursor.getString(ConversationTable.NICK_NAME_COLUMN_INDEX);
            conversation.avatar = cursor.getString(ConversationTable.AVATAR_COLUMN_INDEX);
            conversations.add(conversation);
        }
        cursor.close();
        return conversations;
    }


    public boolean deleteById(long conversationId) {
        int result = mDb.delete(ConversationTable.TABLE_NAME, ConversationTable.ID + "=?", new String[]{conversationId + ""});
        return result != -1;
    }

}
