package com.xiaokun.advance_practive.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.im.PdIMClient;
import com.xiaokun.advance_practive.im.PdMessageListener;
import com.xiaokun.advance_practive.im.database.bean.PdConversation;
import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.database.dao.ConversationDao;
import com.xiaokun.advance_practive.im.database.dao.MessageDao;
import com.xiaokun.advance_practive.im.entity.Conversation;
import com.xiaokun.advance_practive.im.util.PdDateUtils;
import com.xiaokun.baselib.muti_rv.BaseMultiHodler;
import com.xiaokun.baselib.muti_rv.HolderFactoryList;
import com.xiaokun.baselib.muti_rv.MultiAdapter;
import com.xiaokun.baselib.muti_rv.MultiItem;

import java.util.ArrayList;
import java.util.List;

import static com.xiaokun.advance_practive.im.util.Utils.createView;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/21
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class ImConversationListActivity extends AppCompatActivity implements PdMessageListener {

    private static final String TAG = "ImConversationListActiv";
    private RecyclerView mRvConversationList;
    private MultiAdapter mMultiAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, ImConversationListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_conversation_list);
        PdIMClient.getInstance().getChatManager().addMessageListener(this);
        initView();
    }

    private void initView() {
        mRvConversationList = findViewById(R.id.rv_conversation_list);
        HolderFactoryList instance = HolderFactoryList.getInstance().addTypeHolder(new BaseMultiHodler<Conversation>(
                createView(R.layout.item_customer_dialogue, mRvConversationList)) {
            @Override
            public void bind(Conversation conversation) {
                setText(R.id.chat_user_name, conversation.nickName);
                setText(R.id.last_msg_tv, conversation.msgContent);
                setText(R.id.last_msg_time_tv, PdDateUtils.format(conversation.updateTime, "yyyy-MM-dd HH:mm"));
                PdConversation pdConversation = PdIMClient.getInstance().getChatManager().getConversation(conversation.userImId);

                if (pdConversation.getUnReadCount() > 0) {
                    setVisible(R.id.unread_msg_num_tv, true);
                } else {
                    setVisible(R.id.unread_msg_num_tv, false);
                }

                setText(R.id.unread_msg_num_tv, pdConversation.getUnReadCount() + "");

                Glide.with(mContext).load(conversation.url)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop())
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher))
                        .into(((ImageView) getView(R.id.chat_user_avatar)));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "点击进入会话窗口", Toast.LENGTH_SHORT).show();
                        pdConversation.markAllMessagesAsRead();
                    }
                });
            }
        });
        mMultiAdapter = new MultiAdapter(instance);

        mMultiAdapter.addItems(getConversations());
        mRvConversationList.setAdapter(mMultiAdapter);
    }

    private List<MultiItem> getConversations() {
        List<PdConversation> pdConversations = ConversationDao.getInstance().queryConversations();
        List<Conversation> conversations = new ArrayList<>();

        for (PdConversation pdConversation : pdConversations) {
            Conversation conversation = new Conversation();
            PdMessage pdMessage = MessageDao.getInstance().queryMsgById(pdConversation.lastMsgId);
            conversation.msgContent = pdMessage.msgContent;
            conversation.userImId = pdConversation.imUserId;
            conversation.history = pdConversation.history.mType == 2;
            conversation.transfer = pdConversation.transfer.mType == 2;
            conversation.nickName = "小小";
            conversation.url = "https://ws1.sinaimg.cn/large/0065oQSqly1g0ajj4h6ndj30sg11xdmj.jpg";
            conversation.updateTime = pdMessage.updateTime;
            conversations.add(conversation);
        }

        return transferList(conversations);
    }

    private <T> List<MultiItem> transferList(List<T> list) {
        if (list == null) {
            return null;
        }
        List<MultiItem> items = new ArrayList<>();

        for (T t : list) {
            MultiItem item = (MultiItem) t;
            items.add(item);
        }
        return items;
    }

    @Override
    public void onMessageReceived(PdMessage pdMessage) {
        Log.e(TAG, "onMessageReceived(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + pdMessage.msgSender);
        List<MultiItem> conversations = getConversations();
        mMultiAdapter.setNewItems(conversations);
    }
}
