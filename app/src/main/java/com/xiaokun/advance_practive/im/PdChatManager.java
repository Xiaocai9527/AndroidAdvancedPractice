package com.xiaokun.advance_practive.im;

import com.xiaokun.advance_practive.im.database.bean.PdConversation;
import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.database.bean.PdMessage.PDChatType;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdMsgBody;
import com.xiaokun.advance_practive.im.database.dao.ConversationDao;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/18
 *      描述  ：会话管理器
 *      版本  ：1.0
 * </pre>
 */
public class PdChatManager {

    private XMPPTCPConnection connection;

    public PdChatManager(XMPPTCPConnection connection) {
        this.connection = connection;
    }

    public void sendMessage(PdMessage pdMessage) {
        if (pdMessage == null) {
            return;
        }
        getMsgType(pdMessage.msgChatType.type);
        sendMsg(getMsgType(pdMessage.msgChatType.type), pdMessage.msgContent, pdMessage.msgReceiver);
    }

    private String getMsgType(int msgType) {
        String type = "text";
        switch (msgType) {
            case PdMsgBody.PDMessageBodyType_TEXT:
                type = "text";
                break;
            case PdMsgBody.PDMessageBodyType_IMAGE:
                type = "img";
                break;
            case PdMsgBody.PDMessageBodyType_VIDEO:
                type = "video";
                break;
            case PdMsgBody.PDMessageBodyType_LOCATION:
                type = "location";
                break;
            case PdMsgBody.PDMessageBodyType_VOICE:
                type = "audio";
                break;
            case PdMsgBody.PDMessageBodyType_FILE:
                type = "file";
                break;
            case PdMsgBody.PDMESSAGEBODYTYPE_COMMAND:
                //命令
                type = "text";
                break;
            case PdMsgBody.PDMessageBodyType_SUPER_TEXT:
                type = "html";
                break;
            case PdMsgBody.PDMessageBodyType_JSON:
                type = "json";
                break;
            case PdMsgBody.PDMessageBodyType_TIP:
                type = "tips";
                break;
            case PdMsgBody.PDMessageBodyType_NOTIFICATION:
                // TODO: 2019/2/19  //通知
                type = "text";
                break;
            case PdMsgBody.PDMESSAGEBODYTYPE_KICK:
                //踢人
                type = "text";
                break;
            default:
                type = "text";
                break;
        }
        return type;
    }

    /**
     * 发送一条消息
     *
     * @param type    消息类型
     * @param msg     消息内容
     * @param to_name 好友userJid
     * @return
     */
    private boolean sendMsg(String type, String msg, String to_name) {
        boolean isSend;
        String json = toJson(msg, type);
        try {
            ChatManager manager = ChatManager.getInstanceFor(connection);
            Chat chat = manager.createChat(to_name, new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, Message message) {
                    // TODO: 2019/2/19 消息处理

                }
            });
            chat.sendMessage(json);
            // TODO: 2019/2/19  插入数据库-会话,消息
            isSend = true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            //发送失败
            isSend = false;
        }
        return isSend;
    }

    /**
     * 通过userId来查会话
     *
     * @param toChatUserImId
     * @return 当返回null表示需要创建会话
     */
    public PdConversation getConversation(String toChatUserImId) {
        PdConversation pdConversation = ConversationDao.getInstance().queryConversation(toChatUserImId);
        return pdConversation;
    }

    public PdConversation getConversation(String toChatUserImId, PDChatType type) {
        PdConversation pdConversation = ConversationDao.getInstance().queryConversation(toChatUserImId, type.type);
        return pdConversation;
    }


    /**
     * 打包成json字符串
     *
     * @param msg
     * @param type text>>文本,voice>>语音,image>>图片
     */
    private String toJson(String msg, String type) {
        try {
            JSONObject object = new JSONObject();
            object.put("type", type);
            object.put("data", msg);
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
