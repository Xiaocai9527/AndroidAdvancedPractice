package com.xiaokun.advance_practive.database.table;


/**
 * Created by 肖坤 on 2019/2/16.
 *
 * @author 肖坤
 * @date 2019/2/16
 */

public class MessageBaseTable extends BaseTable {

    /**
     * 表名
     */
    public static final String TABLE_NAME = "Message";

    /**
     * 表字段
     */
    public static final String ID = "messageid";
    public static final String FROM = "send";
    public static final String TO = "receive";
    public static final String TYPE = "type";
    public static final String READ = "read";

    /**
     * 字段对应的columnIndex
     */
    public static final int ID_COLUMN_INDEX = 0;
    public static final int FROM_COLUMN_INDEX = 1;
    public static final int TO_COLUMN_INDEX = 2;
    public static final int TYPE_COLUMN_INDEX = 3;
    public static final int READ_COLUMN_INDEX = 4;

    /**
     * 创建表sql
     */
    public static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(" +
            ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
            FROM + TEXT_TYPE_SEP +
            TO + TEXT_TYPE_SEP +
            TYPE + INTEGER_TYPE_SEP +
            READ + INTEGER_TYPE + ")";
}
