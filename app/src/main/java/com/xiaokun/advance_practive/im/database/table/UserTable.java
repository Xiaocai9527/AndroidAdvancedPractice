package com.xiaokun.advance_practive.im.database.table;


import java.util.LinkedHashMap;

import static com.xiaokun.advance_practive.im.database.table.BaseTable.DataType.INTEGER;
import static com.xiaokun.advance_practive.im.database.table.BaseTable.DataType.TEXT;

/**
 * Created by 肖坤 on 2019/2/16.
 *
 * @author 肖坤
 * @date 2019/2/16
 */

public class UserTable extends BaseTable {

    /**
     * 表名
     */
    public static final String TABLE_NAME = "user";

    /**
     * 表字段-主键
     */
    public static final String ID = "userid";
    //昵称
    public static final String NICKNAME = "nickname";
    //电话
    public static final String PHONE = "phone";
    //性别
    public static final String GENDER = "gender";
    //真实姓名
    public static final String NAME = "name";
    //im账号
    public static final String USER_IM_ID = "user_im_id";

    /**
     * 字段对应的columnIndex
     */
    public static final int ID_COLUMN_INDEX = 0;
    public static final int NICKNAME_COLUMN_INDEX = 1;
    public static final int PHONE_COLUMN_INDEX = 2;
    public static final int GENDER_COLUMN_INDEX = 3;
    public static final int NAME_COLUMN_INDEX = 4;
    public static final int USER_IM_ID_COLUMN_INDEX = 5;

    public static LinkedHashMap<String, DataType> map = new LinkedHashMap<>();

    static {
        map.put(NICKNAME, TEXT);
        map.put(PHONE, TEXT);
        map.put(GENDER, INTEGER);
        map.put(NAME, TEXT);
        map.put(USER_IM_ID, TEXT);
    }

    /**
     * 获取建表语句
     *
     * @return
     */
    public static String getSql() {
        return createTableSql(TABLE_NAME, map, ID, INTEGER);
    }

    /**
     * 创建表sql
     */
    public static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(" +
            ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
            NICKNAME + TEXT_TYPE_SEP +
            PHONE + TEXT_TYPE_SEP +
            GENDER + INTEGER_TYPE_SEP +
            NAME + TEXT_TYPE + ")";

}
