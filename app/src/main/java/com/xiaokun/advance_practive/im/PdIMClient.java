package com.xiaokun.advance_practive.im;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.xiaokun.advance_practive.im.element.TextElement;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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
public class PdIMClient {
    private static final String TAG = "PdIMClient";
    private static PdIMClient mPdIMClient = null;
    private Context mContext;
    private PdChatManager mPdChatManager;
    private static final String SERVER_NAME = "peidou";//主机名
    private static final String SERVER_IP = "192.168.1.12";//ip
    private static final String SERVER_RESOURCE = "android";//资源
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

        initProvider();
    }

    private void initProvider() {
        ProviderManager.addExtensionProvider("mobilePeidou", "peidou", new Provider());
    }

    /**
     * 登录im
     *
     * @param userName
     * @param password
     */
    public void login(String userName, String password) {
        if (TextUtils.isEmpty(mPdOptions.getAppkey())) {
            throw new IllegalArgumentException("请设置appkey");
        } else {
            if (connection == null) {
                throw new IllegalArgumentException("需要先初始化init方法");
            }
            if (mIsConnect) {
                try {
                    connection.login(userName, password);
                    Log.e(TAG, "login(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + "登录成功");
                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "login(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + "connect连接失败");
            }
        }
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
            }
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
