package com.xiaokun.advance_practive.database.table;


/**
 * Created by 肖坤 on 2019/2/16.
 *
 * @author 肖坤
 * @date 2019/2/16
 */

public class ConversationBaseTable extends BaseTable {

    /**
     * 表名
     */
    public static final String TABLE_NAME = "Conversation";

    /**
     * 表字段
     */
    public static final String ID = "conversationid";
    public static final String AVATAR = "avatar";
    public static final String TRANSFER = "transfer";
    public static final String NICKNAME = "nickname";
    public static final String UPDATETIME = "updatetime";
    public static final String HISTORY = "history";

    /**
     * 字段对应的columnIndex
     */
    public static final int ID_COLUMN_INDEX = 0;
    public static final int AVATAR_COLUMN_INDEX = 1;
    public static final int TRANSFER_COLUMN_INDEX = 2;
    public static final int NICKNAME_COLUMN_INDEX = 3;
    public static final int UPDATETIME_COLUMN_INDEX = 4;
    public static final int HISTORY_COLUMN_INDEX = 5;

    /**
     * 创建表sql
     */
    public static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(" +
            ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
            AVATAR + TEXT_TYPE_SEP +
            TRANSFER + INTEGER_TYPE_SEP +
            NICKNAME + TEXT_TYPE_SEP +
            UPDATETIME + INTEGER_TYPE_SEP +
            HISTORY + INTEGER_TYPE + ")";
}
