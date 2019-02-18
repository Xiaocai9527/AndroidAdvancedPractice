package com.xiaokun.advance_practive.database.table;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 肖坤 on 2019/2/17.
 *
 * @author 肖坤
 * @date 2019/2/17
 */

public class BaseTable {

    protected static final String TEXT_TYPE_SEP = " text,";
    protected static final String INTEGER_TYPE_SEP = " integer,";
    protected static final String REAL_TYPE_SEP = " real,";
    protected static final String BLOB_TYPE_SEP = " blob,";

    protected static final String TEXT_TYPE = " text";
    protected static final String INTEGER_TYPE = " integer";
    protected static final String REAL_TYPE = " real";
    protected static final String BLOB_TYPE = " blob";


    protected static final String COMMA_SEP = ",";
    protected static final String PRIMARY_KEY = " primary key";
    protected static final String AUTOINCREMENT = " autoincrement";

    protected static String CREATE_TABLE = "";

    protected static String createTableSql(String tableName, LinkedHashMap<String, DataType> hashMap, String primaryKey, DataType dataType) {
        CREATE_TABLE = "";
        CREATE_TABLE += "create table if not exists " + tableName + "(";

        CREATE_TABLE += primaryKey + getResultByType(dataType) + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP;

        for (Map.Entry<String, DataType> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            DataType value = entry.getValue();
            CREATE_TABLE += key + getResultByType(value) + COMMA_SEP;
        }

        CREATE_TABLE = CREATE_TABLE.substring(0, CREATE_TABLE.lastIndexOf(","));
        CREATE_TABLE += ")";
        return CREATE_TABLE;
    }

    private static String getResultByType(DataType dataType) {
        String str = "";
        switch (dataType) {
            case TEXT:
                str = TEXT_TYPE;
                break;
            case INTEGER:
                str = INTEGER_TYPE;
                break;
            case REAL:
                str = REAL_TYPE;
                break;
            case BLOB:
                str = BLOB_TYPE;
                break;
            default:
                throw new IllegalArgumentException("违规的数据库数据类型");
        }
        return str;
    }

    public enum DataType {
        TEXT,
        INTEGER,
        REAL,
        BLOB
    }

}
