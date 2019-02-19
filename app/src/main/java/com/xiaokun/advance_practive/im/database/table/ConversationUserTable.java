package com.xiaokun.advance_practive.im.database.table;

import java.util.LinkedHashMap;

import static com.xiaokun.advance_practive.im.database.table.BaseTable.DataType.INTEGER;
import static com.xiaokun.advance_practive.im.database.table.BaseTable.DataType.TEXT;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/18
 *      描述  ：会话表中的user
 *      版本  ：1.0
 * </pre>
 */
public class ConversationUserTable extends BaseTable {

    /**
     * 表名
     */
    public static final String TABLE_NAME = "conversation_user";

    /**
     * 表字段
     */
    public static final String ID = "conversation_user_id";
    //头像
    public static final String AVATAR = "avatar";
    //昵称
    public static final String NICKNAME = "nickname";

    /**
     * 字段对应的columnIndex
     */
    public static final int ID_COLUMN_INDEX = 0;
    public static final int AVATAR_COLUMN_INDEX = 1;
    public static final int NICKNAME_COLUMN_INDEX = 2;

    public static LinkedHashMap<String, DataType> map = new LinkedHashMap<>();

    static {
        map.put(AVATAR, TEXT);
        map.put(NICKNAME, TEXT);
    }

    /**
     * 获取建表语句
     *
     * @return
     */
    public static String getSql() {
        return createTableSql(TABLE_NAME, map, ID, INTEGER);
    }

}
