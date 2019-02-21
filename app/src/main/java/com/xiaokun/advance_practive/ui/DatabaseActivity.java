package com.xiaokun.advance_practive.ui;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.im.PdIMClient;
import com.xiaokun.advance_practive.im.PdMessageListener;
import com.xiaokun.advance_practive.im.PdOptions;
import com.xiaokun.advance_practive.im.database.DatabaseHelper;
import com.xiaokun.advance_practive.im.database.bean.PdConversation;
import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.database.bean.User;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdTextMsgBody;
import com.xiaokun.advance_practive.im.database.dao.ConversationDao;
import com.xiaokun.advance_practive.im.database.dao.MessageDao;
import com.xiaokun.advance_practive.im.database.dao.UserDao;

import java.util.ArrayList;
import java.util.List;

import static com.xiaokun.baselib.util.Utils.getRandomString;
import static com.xiaokun.baselib.util.Utils.getTel;
import static com.xiaokun.baselib.util.Utils.testRandom1;

/**
 * Created by 肖坤 on 2019/2/17.
 *
 * @author 肖坤
 * @date 2019/2/17
 */

public class DatabaseActivity extends AppCompatActivity {

    private static final String TAG = "DatabaseActivity";
    private SQLiteDatabase mDb;

    public static void start(Context context) {
        Intent starter = new Intent(context, DatabaseActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        PdOptions pdOptions = new PdOptions();
        pdOptions.setAppKey("12345678");
        PdIMClient.getInstance().init(this, pdOptions);
    }

    private void createDb() {
        if (mDb == null) {
            mDb = DatabaseHelper.getDatabase();
        }
    }

    public void createBook(View view) {
        createDb();
    }

    public void addBookDatas(View view) {
        createDb();
//        mDb.execSQL("insert into Book (name,author,pages,price) values(?,?,?,?)",
//                new String[]{"第一行代码", "郭霖", "100", "56"});
        mDb.execSQL("insert into Book (name,author,pages,price) " +
                "values(\"第二行代码\", \"郭霖\", \"100\", \"56\")");
    }

    public void addUserDatas(View view) {
        createDb();
        User user = new User();
        user.gender = 0;
        user.phone = getTel();
        user.nickName = getRandomString(2);
        user.name = "肖坤";
        UserDao.getInstance().insert(user);
    }

    public void addConversationDatas(View view) {
        createDb();
        PdConversation pdConversation = new PdConversation();
        //4FNTMPaP
        pdConversation.imUserId = getRandomString(8);
        pdConversation.imUserId = "4FNTMPaP";
        pdConversation.conversationId = testRandom1();
        pdConversation.conversationType = PdConversation.ConversationType.Single;
        pdConversation.conversationUserId = testRandom1();
        pdConversation.history = 0;
        pdConversation.lastMsgId = testRandom1();
        pdConversation.transfer = 0;
        pdConversation.nickName = getRandomString(2);
        pdConversation.avatar = "http://" + getRandomString(10);
        boolean insert = ConversationDao.getInstance().insert(pdConversation);
        Log.e(TAG, "addConversationDatas(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + insert);
    }

    public void queryCurrentUser(View view) {
        createDb();
        User user = UserDao.getInstance().queryCurrentUser();
        Log.e(TAG, "queryCurrentUser(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                user.name + "," + user.nickName);
    }

    public void queryCurrentConversation(View view) {
        //837976333
        createDb();
        List<PdConversation> pdConversations = ConversationDao.getInstance().queryAllConversations();
        Log.e(TAG, "queryCurrentConversation(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + pdConversations.size());
        for (PdConversation pdConversation : pdConversations) {
            Log.e(TAG, "queryCurrentConversation(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                    pdConversation.conversationId);
        }
    }

    public void deleteCurrentConversation(View view) {
        createDb();
        boolean result = ConversationDao.getInstance().deleteById(837976333);
        Log.e(TAG, "result:" + result);
    }

    public void addMsg(View view) {
        createDb();
        PdMessage msg = getMsg();
        //hsBLApo7
        Log.e(TAG, "msgId:" + msg.imMsgId);
        MessageDao.getInstance().insertMsg(msg);
    }

    private PdMessage getMsg() {
        PdMessage pdMessage = new PdMessage();
        pdMessage.imMsgId = getRandomString(8);
        pdMessage.tenantId = testRandom1();
        pdMessage.businessId = testRandom1();
        pdMessage.sessionId = testRandom1();
        pdMessage.sendTime = System.currentTimeMillis();
        pdMessage.msgType = 1;
        pdMessage.msgSender = "肖坤";
        pdMessage.msgReceiver = "小菜";
        pdMessage.read = 0;
        pdMessage.msgContent = "你好我是小菜";
        pdMessage.msgChatType = PdMessage.PDChatType.SINGLE;
        pdMessage.msgDirection = PdMessage.PDDirection.SEND;
        pdMessage.msgStatus = PdMessage.PDMessageStatus.SUCCESS;
        return pdMessage;
    }


    public void addMsgs(View view) {
        createDb();
        List<PdMessage> pdMessages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            pdMessages.add(getMsg());
        }
        MessageDao.getInstance().insertMsgs(pdMessages);
    }

    public void updateMsg(View view) {
        createDb();
        PdMessage pdMessage = new PdMessage();
        pdMessage.imMsgId = "hsBLApo7";
        pdMessage.tenantId = testRandom1();
        pdMessage.businessId = testRandom1();
        pdMessage.sessionId = testRandom1();
        pdMessage.sendTime = System.currentTimeMillis();
        pdMessage.msgType = 1;
        pdMessage.msgSender = "肖坤-更新";
        pdMessage.msgReceiver = "小菜-更新";
        pdMessage.read = 0;
        pdMessage.msgContent = "你好我是小菜-更新";
        pdMessage.msgChatType = PdMessage.PDChatType.SINGLE;
        pdMessage.msgDirection = PdMessage.PDDirection.SEND;
        pdMessage.msgStatus = PdMessage.PDMessageStatus.SUCCESS;
        MessageDao.getInstance().updateMsg(pdMessage);
    }

    public void queryMsg(View view) {

    }

    public void sendMsg(View view) {
        PdMessage pdMessage = new PdMessage();
        pdMessage.imMsgId = "hsBLApo7";
        pdMessage.tenantId = testRandom1();
        pdMessage.businessId = testRandom1();
        pdMessage.sessionId = testRandom1();
        pdMessage.sendTime = System.currentTimeMillis();
        pdMessage.msgType = 1;
        pdMessage.msgSender = "test7@peidou/pd";
        pdMessage.msgReceiver = "test8@peidou/pd";
        pdMessage.read = 0;
        pdMessage.msgContent = "你好我是小菜-更新";
        pdMessage.msgChatType = PdMessage.PDChatType.SINGLE;
        pdMessage.msgDirection = PdMessage.PDDirection.SEND;
        pdMessage.msgStatus = PdMessage.PDMessageStatus.NEW;
        PdTextMsgBody pdTextMsgBody = new PdTextMsgBody();
        pdTextMsgBody.content = "你好我是小菜-更新";
        pdMessage.pdMsgBody = pdTextMsgBody;
        PdIMClient.getInstance().getChatManager().sendMessage(pdMessage);
    }

    public void login(View view) {
        PdIMClient.getInstance().login("test7", "test7", new PdIMClient.LoginCallback() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "onSuccess(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + "登录成功");
            }

            @Override
            public void onError(int code, String errorMsg) {
                Log.e(TAG, "onError(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                        code + ";msg:" + errorMsg);
            }
        });
        PdIMClient.getInstance().getChatManager().addMessageListener(new PdMessageListener() {
            @Override
            public void onMessageReceived(PdMessage pdMessage) {
                Log.e(TAG, "to:" + pdMessage.msgReceiver + ";from:" + pdMessage.msgSender +
                        ";content:");
            }
        });
    }
}
