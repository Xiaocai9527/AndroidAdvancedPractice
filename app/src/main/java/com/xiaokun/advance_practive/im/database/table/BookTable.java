package com.xiaokun.advance_practive.im.database.table;


/**
 * Created by 肖坤 on 2019/2/16.
 *
 * @author 肖坤
 * @date 2019/2/16
 */

public class BookTable extends BaseTable {

    /**
     * 表名
     */
    public static final String TABLE_NAME = "Book";

    /**
     * 表字段
     */
    public static final String ID = "id";
    public static final String AUTHOR = "author";
    public static final String PRICE = "price";
    public static final String PAGES = "pages";
    public static final String NAME = "name";

    /**
     * 字段对应的columnIndex
     */
    public static final int ID_COLUMN_INDEX = 0;
    public static final int AUTHOR_COLUMN_INDEX = 1;
    public static final int PRICE_COLUMN_INDEX = 2;
    public static final int PAGES_COLUMN_INDEX = 3;
    public static final int NAME_COLUMN_INDEX = 4;

    /**
     * 创建表sql
     */
    public static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(" +
            ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
            AUTHOR + TEXT_TYPE_SEP +
            PRICE + REAL_TYPE_SEP +
            PAGES + INTEGER_TYPE_SEP +
            NAME + TEXT_TYPE + ")";


}
