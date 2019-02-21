package com.xiaokun.advance_practive.im;

import android.util.Log;

import com.xiaokun.advance_practive.im.database.bean.PdConversation;
import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.database.bean.PdMessage.PDChatType;
import com.xiaokun.advance_practive.im.database.bean.User;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdImgMsgBody;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdLocationMsgBody;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdMsgBody;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdTextMsgBody;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdVideoMsgBody;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdVoiceMsgBody;
import com.xiaokun.advance_practive.im.database.dao.ConversationDao;
import com.xiaokun.advance_practive.im.database.dao.MessageDao;
import com.xiaokun.advance_practive.im.database.dao.UserDao;
import com.xiaokun.advance_practive.im.element.AudioElement;
import com.xiaokun.advance_practive.im.element.BasePeidouElement;
import com.xiaokun.advance_practive.im.element.ImgElement;
import com.xiaokun.advance_practive.im.element.LocationElement;
import com.xiaokun.advance_practive.im.element.ReceiptsElement;
import com.xiaokun.advance_practive.im.element.RequestElement;
import com.xiaokun.advance_practive.im.element.TextElement;
import com.xiaokun.advance_practive.im.element.VideoElement;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.sm.StreamManagementException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/18
 *      描述  ：会话管理器
 *      版本  ：1.0
 * </pre>
 */
public class PdChatManager {

    private static final String TAG = "PdChatManager";
    private XMPPTCPConnection connection;


    public PdChatManager(XMPPTCPConnection connection) {
        this.connection = connection;
    }

    /**
     * 发送消息
     *
     * @param pdMessage
     */
    public void sendMessage(PdMessage pdMessage) {
        if (pdMessage == null) {
            return;
        }
        sendMsg(generateMsg(pdMessage));
    }

    private Message generateMsg(PdMessage pdMessage) {
        Message message = new Message();
        message.setTo(pdMessage.msgReceiver);
        message.setFrom(pdMessage.msgSender);
        message.setBody("单聊-" + getMsgType(pdMessage.msgType));
        message.setType(Message.Type.chat);

        RequestElement requestElement = new RequestElement();
        message.addExtension(requestElement);
        message.addExtension(getExtensionElement(pdMessage));
        return message;
    }

    /**
     * 初始化聊天消息监听
     */
    public void addMessageListener(PdMessageListener pdMessageListener) {
        ChatManager manager = ChatManager.getInstanceFor(connection);
        //设置信息的监听
        final ChatMessageListener messageListener = new ChatMessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                if (message == null) {
                    return;
                }
                PdMessage pdMessage = parserMsg(message);
                if (!pdMessage.receipts) {
                    pdMessageListener.onMessageReceived(pdMessage);
                }
            }
        };
        ChatManagerListener chatManagerListener = new ChatManagerListener() {

            @Override
            public void chatCreated(Chat chat, boolean arg1) {
                chat.addMessageListener(messageListener);
            }
        };
        manager.addChatListener(chatManagerListener);
    }

    /**
     * 解析消息
     *
     * @param message
     */
    private PdMessage parserMsg(Message message) {
        List<ExtensionElement> extensions = message.getExtensions();
        PdMessage pdMessage = new PdMessage();
        pdMessage.msgSender = message.getFrom();
        pdMessage.msgReceiver = message.getTo();
        pdMessage.imMsgId = message.getStanzaId();
        pdMessage.msgStatus = PdMessage.PDMessageStatus.DELIVERING;
        // TODO: 2019/2/21 单聊和其它的区分，这里只是只做单聊
        pdMessage.msgChatType = PDChatType.SINGLE;

        User user = UserDao.getInstance().queryCurrentUser();
        if (user.userImId.equals(message.getFrom())) {
            pdMessage.msgDirection = PdMessage.PDDirection.SEND;
        } else {
            pdMessage.msgDirection = PdMessage.PDDirection.RECEIVE;
        }

        for (ExtensionElement extension : extensions) {
            if (extension instanceof BasePeidouElement) {
                if (extension instanceof TextElement) {
                    PdTextMsgBody pdTextMsgBody = new PdTextMsgBody();
                    pdTextMsgBody.content = ((TextElement) extension).getContent();
                    pdMessage.pdMsgBody = pdTextMsgBody;
                    pdMessage.msgType = PdMsgBody.PDMessageBodyType_TEXT;
                } else if (extension instanceof ImgElement) {
                    PdImgMsgBody pdImgMsgBody = new PdImgMsgBody();
                    pdImgMsgBody.remoteUrl = ((ImgElement) extension).getUrl();
                    pdImgMsgBody.thumbnailRemoteUrl = ((ImgElement) extension).getProperty();
                    pdMessage.msgType = PdMsgBody.PDMessageBodyType_IMAGE;
                } else if (extension instanceof AudioElement) {
                    PdVoiceMsgBody pdVoiceMsgBody = new PdVoiceMsgBody();
                    pdVoiceMsgBody.remoteUrl = ((AudioElement) extension).getUrl();
                    pdVoiceMsgBody.timeLength = ((AudioElement) extension).getProperty();
                    pdMessage.msgType = PdMsgBody.PDMessageBodyType_VOICE;
                } else if (extension instanceof LocationElement) {
                    // TODO: 2019/2/20 地图扩展
                } else if (extension instanceof ReceiptsElement) {
                    //回执消息
                    String msgId = ((ReceiptsElement) extension).getMsgId();
                    MessageDao.getInstance().updateMsgStatusById(msgId);
                    pdMessage.receipts = true;
                }
            }
        }
        return pdMessage;
    }

    private BasePeidouElement getExtensionElement(PdMessage pdMessage) {
        BasePeidouElement element = null;
        String type = "";
        switch (pdMessage.msgType) {
            case PdMsgBody.PDMessageBodyType_TEXT:
                type = "text";
                element = new TextElement();
                ((TextElement) element).setContent(((PdTextMsgBody) pdMessage.pdMsgBody).content);
                break;
            case PdMsgBody.PDMessageBodyType_IMAGE:
                type = "img";
                element = new ImgElement();
                ImgElement imgElement = (ImgElement) element;
                imgElement.setUrl(((PdImgMsgBody) pdMessage.pdMsgBody).remoteUrl);
                imgElement.setProperty(((PdImgMsgBody) pdMessage.pdMsgBody).thumbnailRemoteUrl);
                break;
            case PdMsgBody.PDMessageBodyType_VIDEO:
                type = "video";
                element = new VideoElement();
                VideoElement videoElement = (VideoElement) element;
                videoElement.setUrl(((PdVideoMsgBody) pdMessage.pdMsgBody).remoteUrl);
                videoElement.setProperty(((PdVideoMsgBody) pdMessage.pdMsgBody).thumbnailRemoteUrl);
                break;
            case PdMsgBody.PDMessageBodyType_LOCATION:
                type = "location";
                element = new LocationElement();
                LocationElement locationElement = (LocationElement) element;
                locationElement.setContent(((PdLocationMsgBody) pdMessage.pdMsgBody).content);
                locationElement.setProperty(((PdLocationMsgBody) pdMessage.pdMsgBody).locationDetail);
                locationElement.setOther(((PdLocationMsgBody) pdMessage.pdMsgBody).mapType);
                break;
            case PdMsgBody.PDMessageBodyType_VOICE:
                type = "audio";
                element = new AudioElement();
                AudioElement audioElement = (AudioElement) element;
                audioElement.setUrl(((PdVoiceMsgBody) pdMessage.pdMsgBody).remoteUrl);
                audioElement.setProperty(((PdVoiceMsgBody) pdMessage.pdMsgBody).timeLength);
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
        element.setNamespace(connection.getServiceName());
        return element;
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

    private boolean sendMsg(Message message) {
        boolean isSend;
        try {
            ChatManager manager = ChatManager.getInstanceFor(connection);
            Chat chat = manager.createChat(message.getTo());
            MessageDao.getInstance().insertMsg(parserMsg(message));
            chat.sendMessage(message);
            isSend = true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            //发送失败
            isSend = false;
        }
        return isSend;
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

    public List<PdConversation> getConversationByType(PdConversation.ConversationType conversationType) {
        return ConversationDao.getInstance().queryConversationByType(conversationType);
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
