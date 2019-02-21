package com.xiaokun.advance_practive.im;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.xiaokun.advance_practive.im.database.bean.User;
import com.xiaokun.advance_practive.im.database.dao.UserDao;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;

import java.io.IOException;

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
}
