package com.xiaokun.advance_practive.im.database.table;


import java.util.LinkedHashMap;

import static com.xiaokun.advance_practive.im.database.table.BaseTable.DataType.INTEGER;
import static com.xiaokun.advance_practive.im.database.table.BaseTable.DataType.TEXT;

/**
 * Created by 肖坤 on 2019/2/16.
 * 会话表
 *
 * @author 肖坤
 * @date 2019/2/16
 */

public class ConversationTable extends BaseTable {

    /**
     * 表名
     */
    public static final String TABLE_NAME = "conversation";

    /**
     * 表字段
     */
    //对方的im账号(根据这个字段和会话类型字段来查会话)主键
    public static final String TO_CHAT_USER_IM_ID = "to_chat_user_im_id";
    //会话id,考虑到客户端和服务端都要创建会话,此字段不作为主键
    public static final String ID = "conversationid";
    //会话业务类型-普通/转接
    public static final String TRANSFER = "transfer";
    //是否历史会话
    public static final String HISTORY = "history";
    //最后一条消息的msg_id
    public static final String LAST_MSG_ID = "last_msg_id";
    //会话类型-单聊/群聊
    public static final String CONVERSATION_TYPE = "conversation_type";
    //会话用户id
    public static final String CONVERSATION_USER_ID = "conversation_user_id";
    //对方的用户昵称
    public static final String NICK_NAME = "nick_name";
    //对方的用户头像
    public static final String AVATAR = "avatar";


    /**
     * 字段对应的columnIndex
     */
    public static final int TO_CHAT_USER_IM_ID_COLUMN_INDEX = 0;
    public static final int ID_COLUMN_INDEX = 1;
    public static final int TRANSFER_COLUMN_INDEX = 2;
    public static final int HISTORY_COLUMN_INDEX = 3;
    public static final int LAST_MSG_ID_COLUMN_INDEX = 4;
    public static final int CONVERSATION_TYPE_COLUMN_INDEX = 5;
    public static final int CONVERSATION_USER_ID_COLUMN_INDEX = 6;
    public static final int NICK_NAME_COLUMN_INDEX = 7;
    public static final int AVATAR_COLUMN_INDEX = 8;


    public static LinkedHashMap<String, DataType> map = new LinkedHashMap<>();

    static {
        map.put(ConversationTable.ID, INTEGER);
        map.put(ConversationTable.TRANSFER, INTEGER);
        map.put(ConversationTable.HISTORY, INTEGER);
        map.put(ConversationTable.LAST_MSG_ID, INTEGER);
        map.put(ConversationTable.CONVERSATION_TYPE, TEXT);
        map.put(ConversationTable.CONVERSATION_USER_ID, INTEGER);
        map.put(ConversationTable.NICK_NAME, TEXT);
        map.put(ConversationTable.AVATAR, TEXT);
    }

    /**
     * 获取建表语句
     *
     * @return
     */
    public static String getSql() {
        return createTableSql(TABLE_NAME, map, TO_CHAT_USER_IM_ID, TEXT);
    }

    /**
     * 创建表sql
     */
    public static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(" +
            ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
            TRANSFER + INTEGER_TYPE_SEP +
            HISTORY + INTEGER_TYPE +
            LAST_MSG_ID + INTEGER_TYPE_SEP +
            CONVERSATION_TYPE + INTEGER_TYPE_SEP +
            CONVERSATION_USER_ID + INTEGER_TYPE_SEP +
            ")";

}
