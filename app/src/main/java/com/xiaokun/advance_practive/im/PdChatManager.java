package com.xiaokun.advance_practive.im;

import android.text.TextUtils;

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

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/18
 *      描述  ：会话管理器
 *      版本  ：1.0
 * </pre>
 */
public class PdChatManager implements ChatMessageListener, ChatManagerListener {

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
    public PdMessage sendMessage(PdMessage pdMessage) {
        if (pdMessage == null) {
            return pdMessage;
        }
        Message message = generateMsg(pdMessage);
        pdMessage.imMsgId = message.getStanzaId();
        sendMsg(message, pdMessage);
        return pdMessage;
    }

    private Message generateMsg(PdMessage pdMessage) {
        Message message = new Message();
        message.setTo(pdMessage.msgReceiver);
        message.setFrom(pdMessage.msgSender);
        message.setBody("单聊-" + getMsgType(pdMessage.msgType));
        message.setType(Message.Type.chat);

        //这里为了兼容发送正在发送中的消息
        if (!TextUtils.isEmpty(pdMessage.imMsgId)) {
            message.setStanzaId(pdMessage.imMsgId);
        }

        RequestElement requestElement = new RequestElement();
        message.addExtension(requestElement);
        message.addExtension(getExtensionElement(pdMessage));
        return message;
    }

    private Chat mChat;

    private Set<PdMessageListener> mPdMessageListeners = new HashSet<>();

    /**
     * 初始化聊天消息监听
     */
    public void addMessageListener(PdMessageListener pdMessageListener) {
        mPdMessageListeners.add(pdMessageListener);
        ChatManager manager = ChatManager.getInstanceFor(connection);
        manager.addChatListener(this);
    }

    public void removeMessageListener(PdMessageListener pdMessageListener) {
        mPdMessageListeners.remove(pdMessageListener);
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
        // TODO: 2019/2/22 这里的时间获取可能会有问题
        pdMessage.updateTime = System.currentTimeMillis();

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
                    pdMessage.msgContent = pdTextMsgBody.content;
                } else if (extension instanceof ImgElement) {
                    PdImgMsgBody pdImgMsgBody = new PdImgMsgBody();
                    pdImgMsgBody.remoteUrl = ((ImgElement) extension).getUrl();
                    pdImgMsgBody.thumbnailRemoteUrl = ((ImgElement) extension).getProperty();
                    pdMessage.msgType = PdMsgBody.PDMessageBodyType_IMAGE;
                    pdMessage.msgContent = toJson(pdImgMsgBody);
                } else if (extension instanceof AudioElement) {
                    PdVoiceMsgBody pdVoiceMsgBody = new PdVoiceMsgBody();
                    pdVoiceMsgBody.remoteUrl = ((AudioElement) extension).getUrl();
                    pdVoiceMsgBody.timeLength = ((AudioElement) extension).getProperty();
                    pdMessage.msgType = PdMsgBody.PDMessageBodyType_VOICE;
                    pdMessage.msgContent = toJson(pdVoiceMsgBody);
                } else if (extension instanceof LocationElement) {
                    // TODO: 2019/2/20 地图扩展
                } else if (extension instanceof ReceiptsElement) {
                    //回执消息
                    String msgId = ((ReceiptsElement) extension).getMsgId();
                    MessageDao.getInstance().updateMsgStatusById(msgId);
                    pdMessage.imMsgId = msgId;
                    //回执消息更新会话表取的是发送者的im账号
                    //ConversationDao.getInstance().updateLastMsgById(pdMessage.msgSender, msgId);
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
                element = new TextElement();
                ((TextElement) element).setContent(((PdTextMsgBody) pdMessage.pdMsgBody).content);
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

    private boolean sendMsg(Message message, PdMessage pdMessage) {
        boolean isSend;
        try {
            ChatManager manager = ChatManager.getInstanceFor(connection);
            Chat chat = manager.createChat(message.getTo());

            saveMessage(message);
            saveConversation(message, pdMessage);
            chat.sendMessage(message);
            isSend = true;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            //发送失败
            isSend = false;
            //updateSendMsgFail(pdMessage);
        }
        return isSend;
    }

    private void updateSendMsgFail(PdMessage pdMessage) {
        MessageDao.getInstance().updateMsgFailStatus(pdMessage);
    }

    private void savePdMessage(PdMessage pdMessage) {
        if (pdMessage.msgDirection == PdMessage.PDDirection.SEND) {
            pdMessage.conversationId = pdMessage.msgReceiver;
            pdMessage.read = PdMessage.PDRead.READ;
        } else {
            pdMessage.conversationId = pdMessage.msgSender;
            pdMessage.read = PdMessage.PDRead.UNREAD;
        }
        MessageDao.getInstance().insertMsg(pdMessage);
    }

    private void saveMessage(Message message) {
        PdMessage pdMessage = parserMsg(message);
        if (pdMessage.msgDirection == PdMessage.PDDirection.SEND) {
            pdMessage.conversationId = message.getTo();
            pdMessage.read = PdMessage.PDRead.READ;
        } else {
            pdMessage.conversationId = message.getFrom();
            pdMessage.read = PdMessage.PDRead.UNREAD;
        }
        MessageDao.getInstance().insertMsg(pdMessage);
    }

    private void saveConversation(Message message, PdMessage pdMessage) {
        PdConversation pdConversation = new PdConversation();
        if (pdMessage.msgDirection == PdMessage.PDDirection.SEND) {
            pdConversation.imUserId = message.getTo();
        } else {
            pdConversation.imUserId = message.getFrom();
        }
        pdConversation.lastMsgId = message.getStanzaId();
        pdConversation.conversationType = PdConversation.ConversationType.Single;
        // TODO: 2019/2/22 这里默认是正常会话,将来是否转接后台返回来的字段来进行更新
        pdConversation.transfer = PdConversation.TransferType.Normal;
        // TODO: 2019/2/22 这个需要后台来判断是否是历史
        pdConversation.history = PdConversation.HistoryType.Normal;
        ConversationDao.getInstance().insert(pdConversation);
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
     * 查询已经降序排序的普通会话
     *
     * @return
     */
    public List<PdConversation> queryAllNormalConversationsSorted() {
        return ConversationDao.getInstance().queryAllNormalConversationsSorted();
    }

    /**
     * 查询已经降序排序的历史会话
     *
     * @return
     */
    public List<PdConversation> queryAllHistoryConversationsSorted() {
        return ConversationDao.getInstance().queryAllHistoryConversationsSorted();
    }

    private String toJson(PdMsgBody pdMsgBody) {
        switch (pdMsgBody.getMsgType()) {
            case PdMsgBody.PDMessageBodyType_IMAGE:
                return toFileJson(((PdImgMsgBody) pdMsgBody).remoteUrl, ((PdImgMsgBody) pdMsgBody).thumbnailRemoteUrl);
            case PdMsgBody.PDMessageBodyType_VOICE:
                return toFileJson(((PdVoiceMsgBody) pdMsgBody).remoteUrl, ((PdVoiceMsgBody) pdMsgBody).timeLength);
            default:

                break;
        }
        return null;
    }

    private String toFileJson(String url, String property) {
        try {
            JSONObject object = new JSONObject();
            object.put("url", url);
            object.put("property", property);
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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

    @Override
    public void processMessage(Chat chat, Message message) {
        if (message == null) {
            return;
        }
        PdMessage pdMessage = parserMsg(message);
        //接收到的消息直接设置成功
        pdMessage.msgStatus = PdMessage.PDMessageStatus.SUCCESS;
        if (!pdMessage.receipts) {
            //非回执消息
            pdMessage.conversationId = pdMessage.imMsgId;
            savePdMessage(pdMessage);
            saveConversation(message, pdMessage);
            Flowable.just(pdMessage)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<PdMessage>() {
                        @Override
                        public void accept(PdMessage pdMessage) throws Exception {
                            for (PdMessageListener pdMessageListener : mPdMessageListeners) {
                                pdMessageListener.onMessageReceived(pdMessage);
                            }
                        }
                    });
        } else {
            //回执消息,通知消息已经发送成功
            //1.更新数据库状态,绘制消息的发送者就是msgId。跟其他消息类型相反
            MessageDao.getInstance().updateMsgSucStatusById(pdMessage.msgSender);
            //2.通知ui界面发送成功
            Flowable.just(pdMessage)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<PdMessage>() {
                        @Override
                        public void accept(PdMessage pdMessage) throws Exception {
                            for (PdMessageListener pdMessageListener : mPdMessageListeners) {
                                pdMessageListener.onReceiptsMessageReceived(pdMessage.imMsgId);
                            }
                        }
                    });
        }
    }

    @Override
    public void chatCreated(Chat chat, boolean b) {
        chat.addMessageListener(this);
    }
}
