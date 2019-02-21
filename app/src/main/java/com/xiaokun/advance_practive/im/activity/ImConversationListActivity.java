package com.xiaokun.advance_practive.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.im.database.bean.PdConversation;
import com.xiaokun.advance_practive.im.entity.Conversation;
import com.xiaokun.advance_practive.ui.adapter.NightModeHolder;
import com.xiaokun.baselib.muti_rv.BaseMultiHodler;
import com.xiaokun.baselib.muti_rv.HolderFactoryList;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/21
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class ImConversationListActivity extends AppCompatActivity {

    private RecyclerView mRvConversationList;

    public static void start(Context context) {
        Intent starter = new Intent(context, ImConversationListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_conversation_list);


        initView();
    }

    private void initView() {
        mRvConversationList = findViewById(R.id.rv_conversation_list);
        HolderFactoryList instance = HolderFactoryList.getInstance();
        View itemView = LayoutInflater.from(this).inflate(R.layout.item_customer_dialogue, mRvConversationList, false);
        instance.addTypeHolder(new BaseMultiHodler<Conversation>(itemView) {

            @Override
            public void bind(Conversation conversation) {
                setText(R.id.chat_user_name, conversation.nickName);
                setText(R.id.last_msg_tv, conversation.msgContent);
                //setText(R.id.last_msg_time_tv, conversation.updateTime);

            }
        }, R.layout.item_customer_dialogue);

    }
}
