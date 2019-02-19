package com.xiaokun.advance_practive.im.database.bean;

import com.google.gson.Gson;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdMsgBody;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/18
 *      描述  ：消息基类
 *      版本  ：1.0
 * </pre>
 */
public class PdMessage {
    public String imMsgId;
    //租户id
    public long tenantId;
    public long businessId;
    //会话id
    public long sessionId;
    public long sendTime;
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
    public int msgType;
    public String msgSender;
    public String msgReceiver;
    //是否已读
    public int read;
    //消息内容
    public String msgContent;
    public PdMsgBody pdMsgBody;
    //单聊-群聊-聊天室-公众号
    public PDChatType msgChatType;
    //消息方向
    public PDDirection msgDirection;
    //消息状态
    public PDMessageStatus msgStatus;

    public void addBody(PdMsgBody pdMsgBody) {
        this.pdMsgBody = pdMsgBody;
        String json = new Gson().toJson(pdMsgBody);
        msgContent = json;
    }

    /**
     * 消息方向
     */
    public enum PDDirection {
        //
        SEND(1, "发送"),
        RECEIVE(2, "接收");

        public int direction;
        public String desc;

        PDDirection(int direction, String desc) {
            this.direction = direction;
            this.desc = desc;
        }
    }

    /**
     * 消息状态
     */
    public enum PDMessageStatus {
        //
        INVAILD(0, "无法识别"),
        NEW(1, "新消息"),
        DELIVERING(2, "发送中"),
        SUCCESS(3, "成功"),
        FAIL(4, "失败");

        public int status;
        public String desc;

        PDMessageStatus(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    /**
     * 消息聊天类型
     */
    public enum PDChatType {
        //
        SINGLE(1, "单聊"),
        GROUP(2, "群聊"),
        WECHAT(3, "公众号"),
        CHAT_ROOM(4, "聊天室");

        public int type;
        public String desc;

        PDChatType(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    private void a() {
        PDDirection b = PDDirection.SEND;
        if (b == PDDirection.SEND) {

        }
    }
}
