package com.xiaokun.advance_practive.im.database.bean.msgBody;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/18
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public abstract class PdMsgBody {

    /**
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
     */

    public static final int PDMessageBodyType_TEXT = 1;
    public static final int PDMessageBodyType_IMAGE = 2;
    public static final int PDMessageBodyType_VIDEO = 3;
    public static final int PDMessageBodyType_LOCATION = 4;
    public static final int PDMessageBodyType_VOICE = 5;
    public static final int PDMessageBodyType_FILE = 6;
    public static final int PDMESSAGEBODYTYPE_COMMAND = 7;
    public static final int PDMessageBodyType_SUPER_TEXT = 8;
    public static final int PDMessageBodyType_JSON = 9;
    public static final int PDMessageBodyType_TIP = 10;
    public static final int PDMessageBodyType_NOTIFICATION = 11;
    public static final int PDMESSAGEBODYTYPE_KICK = 12;


    public abstract int getMsgType();
}
