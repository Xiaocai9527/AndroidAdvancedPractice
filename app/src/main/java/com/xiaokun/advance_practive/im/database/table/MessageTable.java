package com.xiaokun.advance_practive.im.database.table;


import java.util.LinkedHashMap;

import static com.xiaokun.advance_practive.im.database.table.BaseTable.DataType.INTEGER;
import static com.xiaokun.advance_practive.im.database.table.BaseTable.DataType.TEXT;

/**
 * Created by 肖坤 on 2019/2/16.
 * 1.文本消息
 * 2.图片消息
 * 3视频
 * 4位置
 * 5.语音消息
 * 6文件
 * 7命令
 * 8超文本
 * 9：json
 * 10：提示
 * 11：通知
 * 12：踢人',
 *
 * @author 肖坤
 * @date 2019/2/16
 */

public class MessageTable extends BaseTable {

    /**
     * 表名
     */
    public static final String TABLE_NAME = "message";

    /**
     * 表字段
     */
    public static final String ID = "message_id";
    //发送者
    public static final String FROM = "send";
    //接收者
    public static final String TO = "receive";
    //消息类型
    public static final String TYPE = "type";
    //是否已读
    public static final String READ = "read";
    //会话id-会话表的主键
    public static final String CONVERSATION_ID = "conversation_id";
    //消息内容主体
    public static final String CONTENT = "content";
    //单聊-群聊-聊天室
    public static final String CHAT_TYPE = "chat_type";
    //消息方向 receive/send
    public static final String DIRECTION = "direction";
    //消息状态
    public static final String STATUS = "status";
    //时间
    public static final String UPDATE_TIME = "update_time";

    /**
     * 字段对应的columnIndex
     */
    public static final int ID_COLUMN_INDEX = 0;
    public static final int FROM_COLUMN_INDEX = 1;
    public static final int TO_COLUMN_INDEX = 2;
    public static final int TYPE_COLUMN_INDEX = 3;
    public static final int READ_COLUMN_INDEX = 4;
    public static final int CONVERSATION_ID_COLUMN_INDEX = 5;
    public static final int CONTENT_COLUMN_INDEX = 6;
    public static final int CHAT_TYPE_COLUMN_INDEX = 7;
    public static final int DIRECTION_COLUMN_INDEX = 8;
    public static final int STATUS_COLUMN_INDEX = 9;
    public static final int UPDATE_TIME_COLUMN_INDEX = 10;


    public static LinkedHashMap<String, DataType> map = new LinkedHashMap<>();

    static {
        map.put(FROM, TEXT);
        map.put(TO, TEXT);
        map.put(TYPE, INTEGER);
        map.put(READ, INTEGER);
        map.put(CONVERSATION_ID, TEXT);
        map.put(CONTENT, TEXT);
        map.put(CHAT_TYPE, INTEGER);
        map.put(DIRECTION, INTEGER);
        map.put(STATUS, INTEGER);
        map.put(UPDATE_TIME, INTEGER);
    }

    /**
     * 获取建表语句
     *
     * @return
     */
    public static String getSql() {
        return createTableSql(TABLE_NAME, map, ID, TEXT);
    }

    /**
     * 创建表sql
     */
    public static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(" +
            ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
            FROM + TEXT_TYPE_SEP +
            TO + TEXT_TYPE_SEP +
            TYPE + INTEGER_TYPE_SEP +
            READ + INTEGER_TYPE +
            CONVERSATION_ID + TEXT_TYPE +
            ")";
}
