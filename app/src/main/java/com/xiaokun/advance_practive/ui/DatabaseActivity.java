package com.xiaokun.advance_practive.ui;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.database.DatabaseHelper;
import com.xiaokun.advance_practive.database.bean.Conversation;
import com.xiaokun.advance_practive.database.bean.PdMessage;
import com.xiaokun.advance_practive.database.bean.User;
import com.xiaokun.advance_practive.database.dao.ConversationDao;
import com.xiaokun.advance_practive.database.dao.MessageDao;
import com.xiaokun.advance_practive.database.dao.UserDao;

import java.util.ArrayList;
import java.util.List;

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
        user.userId = testRandom1();
        user.gender = 0;
        user.phone = "13812341234";
        user.nickName = "小菜";
        user.name = "肖坤";
        UserDao.getInstance().insert(user);
    }

    public void addConversationDatas(View view) {
        createDb();
        Conversation conversation = new Conversation();
        conversation.conversationId = testRandom1();
        conversation.conversationType = 0;
        conversation.conversationUserId = testRandom1();
        conversation.history = 0;
        conversation.lastMsgId = testRandom1();
        conversation.transfer = 0;
        boolean insert = ConversationDao.getInstance().insert(conversation);
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
        List<Conversation> conversations = ConversationDao.getInstance().queryAllConversations();
        Log.e(TAG, "queryCurrentConversation(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + conversations.size());
        for (Conversation conversation : conversations) {
            Log.e(TAG, "queryCurrentConversation(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                    conversation.conversationId);
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
        //-1316820833
        Log.e(TAG, "msgId:" + msg.imMsgId);
        MessageDao.getInstance().insertMsg(msg);
    }

    private PdMessage getMsg() {
        PdMessage pdMessage = new PdMessage();
        pdMessage.imMsgId = testRandom1();
        pdMessage.tenantId = testRandom1();
        pdMessage.businessId = testRandom1();
        pdMessage.sessionId = testRandom1();
        pdMessage.sendTime = System.currentTimeMillis();
        pdMessage.msgType = 1;
        pdMessage.msgSender = "肖坤";
        pdMessage.msgReceiver = "小菜";
        pdMessage.read = 0;
        pdMessage.msgContent = "你好我是小菜";
        pdMessage.msgChatType = PdMessage.PDAChatType.SINGLE;
        pdMessage.msgDirection = PdMessage.PDADirection.SEND;
        pdMessage.msgStatus = PdMessage.PDAMessageStatus.SUCCESS;
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
        pdMessage.imMsgId = -1316820833;
        pdMessage.tenantId = testRandom1();
        pdMessage.businessId = testRandom1();
        pdMessage.sessionId = testRandom1();
        pdMessage.sendTime = System.currentTimeMillis();
        pdMessage.msgType = 1;
        pdMessage.msgSender = "肖坤-更新";
        pdMessage.msgReceiver = "小菜-更新";
        pdMessage.read = 0;
        pdMessage.msgContent = "你好我是小菜-更新";
        pdMessage.msgChatType = PdMessage.PDAChatType.SINGLE;
        pdMessage.msgDirection = PdMessage.PDADirection.SEND;
        pdMessage.msgStatus = PdMessage.PDAMessageStatus.SUCCESS;
        MessageDao.getInstance().updateMsg(pdMessage);
    }

    public void queryMsg(View view) {

    }
}
