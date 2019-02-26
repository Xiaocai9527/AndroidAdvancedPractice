package com.xiaokun.advance_practive.im.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.im.PdIMClient;
import com.xiaokun.advance_practive.im.database.bean.PdConversation;
import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.database.dao.ConversationDao;
import com.xiaokun.advance_practive.im.database.dao.MessageDao;
import com.xiaokun.advance_practive.im.entity.Conversation;
import com.xiaokun.advance_practive.im.util.PdDateUtils;
import com.xiaokun.baselib.muti_rv.BaseMultiHodler;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/22
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class MyHolder extends BaseMultiHodler<Conversation> {

    public MyHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Conversation conversation) {
        setText(R.id.chat_user_name, conversation.nickName);

        // TODO: 2019/2/26 缺少发送失败的情况
        if (conversation.mPdMessage.msgStatus == PdMessage.PDMessageStatus.SUCCESS) {
            setText(R.id.last_msg_tv, conversation.msgContent);
            setText(R.id.last_msg_time_tv, PdDateUtils.format(conversation.updateTime, "yyyy-MM-dd HH:mm"));
        } else {
            setText(R.id.last_msg_tv, "<==" + conversation.msgContent);
            setText(R.id.last_msg_time_tv, "正在发送中");
        }

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
                ImChatActivity.start(mContext, conversation.userImId);
            }
        });

    }
}
