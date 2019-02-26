package com.xiaokun.advance_practive.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.database.bean.User;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdImgMsgBody;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdMsgBody;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdTextMsgBody;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdVoiceMsgBody;
import com.xiaokun.advance_practive.im.database.dao.MessageDao;
import com.xiaokun.advance_practive.im.database.dao.UserDao;
import com.xiaokun.advance_practive.ui.HomeActivity;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/19
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class PdIMClient implements ConnectionListener {
    private static final String TAG = "PdIMClient";
    private static PdIMClient mPdIMClient = null;
    private Context mContext;
    private PdChatManager mPdChatManager;
    private static final String SERVER_NAME = "peidou";//主机名
    private static final String SERVER_IP = "192.168.1.12";//ip
    private static final String SERVER_RESOURCE = "pd";//资源
    private static final int PORT = 5222;//端口
    private XMPPTCPConnection connection;
    private PdOptions mPdOptions;
    private boolean mIsConnect;

    private PdIMClient() {

    }

    public static PdIMClient getInstance() {
        if (mPdIMClient == null) {
            synchronized (PdIMClient.class) {
                if (mPdIMClient == null) {
                    mPdIMClient = new PdIMClient();
                }
            }
        }
        return mPdIMClient;
    }

    public void init(Context context, PdOptions pdOptions) {
        mContext = context.getApplicationContext();
        mPdOptions = pdOptions;

        // TODO: 2019/2/19 检验appkey合法性

        connection = getConnection();
        conn(connection);
        initProvider();
        initNetworkListener();
    }

    private void conn(XMPPTCPConnection connection) {
        Flowable.just(connection)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<XMPPTCPConnection>() {
                    @Override
                    public void accept(XMPPTCPConnection xmpptcpConnection) throws Exception {
                        try {
                            XMPPTCPConnection connect = (XMPPTCPConnection) connection.connect();
                            mIsConnect = connect.isConnected();
                        } catch (SmackException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + throwable.getMessage());
                    }
                });
    }

    private void initProvider() {
        ProviderManager.addExtensionProvider("mobilePeidou", "peidou", new Provider());
    }

    private void initNetworkListener() {
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        NetworkChangeReceiver mNetworkChangeReceiver = new NetworkChangeReceiver();
        mContext.registerReceiver(mNetworkChangeReceiver, mIntentFilter);
    }

    private String mUserName;
    private String mPassword;

    /**
     * 登录im,默认情况下smack会尝试重新连接,以防突然断开
     *
     * @param userName
     * @param password
     */
    public void login(String userName, String password, LoginCallback loginCallback) {
        if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(password)) {
            return;
        }
        mUserName = userName;
        mPassword = password;
        if (TextUtils.isEmpty(mPdOptions.getAppkey())) {
            throw new IllegalArgumentException("请设置appkey");
        } else {
            if (connection == null) {
                throw new IllegalArgumentException("需要先初始化init方法");
            }
            if (mIsConnect) {
                try {
                    connection.login(userName, password);
                    User user = new User();
                    user.userImId = userName + "@" + connection.getConfiguration().getServiceName() +
                            "/" + connection.getConfiguration().getResource();
                    UserDao.getInstance().insert(user);
                    loginCallback.onSuccess();
                    sendDeliveringMsg();
                    //登录成功后获取离线消息
                    getOfflineMessage();
                } catch (XMPPException e) {
                    e.printStackTrace();
                    loginCallback.onError(ErrorCode.XMPP_ERROR_CODE, e.getMessage());
                    logout();
                    conn(connection);
                } catch (SmackException e) {
                    e.printStackTrace();
                    loginCallback.onError(ErrorCode.SMACK_ERROR_CODE, e.getMessage());
                    logout();
                    conn(connection);
                } catch (IOException e) {
                    e.printStackTrace();
                    loginCallback.onError(ErrorCode.IO_ERROR_CODE, e.getMessage());
                    logout();
                    conn(connection);
                }
            } else {
                loginCallback.onError(ErrorCode.CONNECT_ERROR_CODE, "connect连接失败");
                conn(connection);
            }
        }
    }

    /**
     * 退出im
     */
    public void logout() {
        if (connection == null) {
            return;
        }
        connection.disconnect();
        Log.e(TAG, "logout(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + "退出连接");
    }

    @Override
    public void connected(XMPPConnection xmppConnection) {
        Log.e(TAG, "connected(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                xmppConnection.getHost() + "已连接");
    }

    @Override
    public void authenticated(XMPPConnection xmppConnection, boolean b) {
        Log.e(TAG, "authenticated(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                xmppConnection.getHost() + "验证通过");
    }

    @Override
    public void connectionClosed() {
        Log.e(TAG, "connectionClosed(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                "连接关闭");
        //尝试重连
        conn(connection);
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.e(TAG, "connectionClosedOnError(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                "连接关闭失败:" + e.getMessage());
    }

    @Override
    public void reconnectionSuccessful() {
        Log.e(TAG, "reconnectionSuccessful(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                "重连成功");
    }

    @Override
    public void reconnectingIn(int i) {
        Log.e(TAG, "reconnectingIn(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                "重连状态:" + i);
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.e(TAG, "reconnectionFailed(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                "重连失败:" + e.getMessage());
    }

    public interface LoginCallback {

        void onSuccess();

        void onError(int code, String errorMsg);
    }

    public PdChatManager getChatManager() {
        if (mPdChatManager == null) {
            if (connection == null) {
                throw new IllegalArgumentException("需要先初始化init方法");
            }
            mPdChatManager = new PdChatManager(connection);
        }
        return mPdChatManager;
    }

    /**
     * 获得与服务器的连接
     *
     * @return
     */
    private XMPPTCPConnection getConnection() {
        try {
            if (connection == null) {
                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                        .setHost(SERVER_IP)//服务器IP地址
                        //服务器端口
                        .setPort(PORT)
                        //设置登录状态
                        .setSendPresence(false)
                        //服务器名称
                        .setServiceName(SERVER_NAME)
                        //是否开启安全模式
                        .setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)
                        //是否开启压缩
                        .setCompressionEnabled(false)
                        .setResource(SERVER_RESOURCE)
                        //开启调试模式
                        .setDebuggerEnabled(true).build();
                connection = new XMPPTCPConnection(config);
                connection.addConnectionListener(this);
            }
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void getOfflineMessage() {
        OfflineMessageManager offlineManager = new OfflineMessageManager(connection);
        try {
            boolean retrieval = offlineManager.supportsFlexibleRetrieval();
            if (!retrieval) {
                return;
            }
            List<Message> list = offlineManager.getMessages();
            for (Message message : list) {
                message.setFrom(message.getFrom().split("/")[0]);
                JSONObject object = new JSONObject(message.getBody());
                String type = object.getString("type");
                String data = object.getString("data");
                //保存离线信息
                // TODO: 2019/2/26 保存离线消息
            }
            //删除离线消息
            offlineManager.deleteMessages();
            //将状态设置成在线
            Presence presence = new Presence(Presence.Type.available);
            connection.sendStanza(presence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info == null || !info.isAvailable()) {
                //有网络变没有网络
                Toast.makeText(mContext, "当前没有网路", Toast.LENGTH_SHORT).show();
                return;
            } else {
                //无网络变有网络,重新连接、登录、找到数据库中还在发送状态的消息并send出去
                connLoginSend();
            }
        }
    }

    private void connLoginSend() {
        Flowable.just(connection)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<XMPPTCPConnection>() {
                    @Override
                    public void accept(XMPPTCPConnection xmpptcpConnection) throws Exception {
                        try {
                            XMPPTCPConnection connect = (XMPPTCPConnection) connection.connect();
                            mIsConnect = connect.isConnected();
                            if (mIsConnect) {
                                //connection.login(mUserName, mPassword);
                                sendDeliveringMsg();
                            }
                        } catch (SmackException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + throwable.getMessage());
                    }
                });
    }

    /**
     * 发送未发送成功的消息
     */
    private void sendDeliveringMsg() {
        List<PdMessage> deliverMsgs = MessageDao.getInstance().getDeliverMsgs();
        for (PdMessage deliverMsg : deliverMsgs) {
            deliverMsg = addBody(deliverMsg);
            getChatManager().sendMessage(deliverMsg);
        }
    }

    private PdMessage addBody(PdMessage deliverMsg) {
        PdMsgBody pdMsgBody = parseJson(deliverMsg.msgContent, deliverMsg.msgType);
        deliverMsg.addBody(pdMsgBody);
        return deliverMsg;
    }

    private PdMsgBody parseJson(String json, int type) {
        if (type == PdMsgBody.PDMessageBodyType_TEXT) {
            PdTextMsgBody pdTextMsgBody = new PdTextMsgBody();
            pdTextMsgBody.content = json;
            return pdTextMsgBody;
        }
        // TODO: 2019/2/26 暂时只处理三种消息类型
        try {
            JSONObject jsonObject = new JSONObject(json);
            String url = jsonObject.getString("url");
            String property = jsonObject.getString("property");
            switch (type) {
                case PdMsgBody.PDMessageBodyType_IMAGE:
                    PdImgMsgBody pdImgMsgBody = new PdImgMsgBody();
                    pdImgMsgBody.remoteUrl = url;
                    pdImgMsgBody.thumbnailRemoteUrl = property;
                    return pdImgMsgBody;
                case PdMsgBody.PDMessageBodyType_VOICE:
                    PdVoiceMsgBody pdVoiceMsgBody = new PdVoiceMsgBody();
                    pdVoiceMsgBody.remoteUrl = url;
                    pdVoiceMsgBody.timeLength = property;
                    return pdVoiceMsgBody;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
