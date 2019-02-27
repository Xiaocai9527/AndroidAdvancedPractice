package com.xiaokun.advance_practive.im.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.impl.ScrollBoundaryDeciderAdapter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.im.PdIMClient;
import com.xiaokun.advance_practive.im.PdMessageListener;
import com.xiaokun.advance_practive.im.adapter.MsgHolderFactoryList;
import com.xiaokun.advance_practive.im.adapter.MsgsAdapter;
import com.xiaokun.advance_practive.im.adapter.holder.TextHolder;
import com.xiaokun.advance_practive.im.database.bean.PdConversation;
import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdImgMsgBody;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdTextMsgBody;
import com.xiaokun.advance_practive.im.entity.Message;
import com.xiaokun.advance_practive.im.util.IMEUtils;
import com.xiaokun.advance_practive.im.util.TextWatcherHelper;
import com.xiaokun.advance_practive.im.util.Utils;
import com.xiaokun.baselib.muti_rv.MultiItem;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/27
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class ImChatFragment extends Fragment implements View.OnClickListener, PdMessageListener {

    private static final String TAG = "ImChatFragment";

    private static final String KEY_TO_CHAT_USER_ID = "TO_CHAT_USER_ID";
    //    private SwipeRefreshLayout mSwLayout;
    private RecyclerView mRvMsg;
    private EditText mEtMsg;
    private Button mBtnSend;
    private PdConversation mConversation;
    private String mToChatUserImId;
    private MsgsAdapter mMsgsAdapter;
    private int mPageNum = 1;
    private int mPageSize = 10;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.activity_im_chat, container, false);

        initView(contentView);

        return contentView;
    }

    private void initView(View view) {
//        mSwLayout = findViewById(R.id.sw_layout);
        mRvMsg = view.findViewById(R.id.rv_msg);
        mEtMsg = view.findViewById(R.id.et_msg);
        mBtnSend = view.findViewById(R.id.btn_send);

        final ClassicsFooter footer = view.findViewById(R.id.footer);
        View arrow = footer.findViewById(ClassicsFooter.ID_IMAGE_ARROW);
        arrow.setScaleY(-1);

        final SmartRefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setEnableNestedScroll(false);
        refreshLayout.setEnableScrollContentWhenLoaded(false);
        refreshLayout.getLayout().setScaleY(-1);
        refreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDeciderAdapter() {
            @Override
            public boolean canLoadMore(View content) {
                return super.canRefresh(content);
            }
        });

        //监听加载，而不是监听 刷新
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                loadMsgs(mPageNum, mPageSize);
                refreshLayout.finishLoadMore();
            }
        });

        initListener(mBtnSend);

        mEtMsg.addTextChangedListener(new TextWatcherHelper() {
            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    mBtnSend.setEnabled(false);
                } else {
                    mBtnSend.setEnabled(true);
                }
            }
        });

//        mSwLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mPageNum++;
//                loadMsgs(mPageNum, mPageSize);
//            }
//        });
        mRvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMEUtils.hideSoftInput(v);
            }
        });

        refreshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMEUtils.hideSoftInput(v);
            }
        });

        MsgHolderFactoryList holder = MsgHolderFactoryList.getInstance()
                .addTypeHolder(TextHolder.class, R.layout.item_text_receive_msg, R.layout.item_text_send_msg);
        mMsgsAdapter = new MsgsAdapter(holder);
        mRvMsg.setAdapter(mMsgsAdapter);
    }

    private void loadMsgs(int pageNum, int pageSize) {
        List<PdMessage> pdMessages = mConversation.loadMsgs(pageNum, pageSize);
        if (pdMessages.isEmpty()) {
            Toast.makeText(getContext(), "没有更多消息了~", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Message> mMessages = new ArrayList<>();
        for (PdMessage pdMessage : pdMessages) {
            mMessages.add(getMessage(pdMessage));
        }
        mMsgsAdapter.swapData(Utils.transferList(mMessages));
//        mSwLayout.setRefreshing(false);
        if (pageNum == 1) {
            mRvMsg.scrollToPosition(mMsgsAdapter.getItemCount() - 1);
        }
        mPageNum++;
    }

    private void initListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                String msg = mEtMsg.getText().toString().trim();
                //暂时发送文本
                PdMessage pdMessage = PdMessage.createPdMessage(mToChatUserImId);
                PdTextMsgBody pdTextMsgBody = new PdTextMsgBody();
                pdTextMsgBody.content = msg;
                pdMessage.addBody(pdTextMsgBody);
                pdMessage.msgContent = msg;
                sendMsg(pdMessage);
                mEtMsg.setText("");
                break;
            default:
                break;
        }
    }

    private void sendMsg(PdMessage pdMessage) {
        pdMessage = PdIMClient.getInstance().getChatManager().sendMessage(pdMessage);
        Message message = getMessage(pdMessage);
        mMsgsAdapter.addItem(message);
        mRvMsg.smoothScrollToPosition(mMsgsAdapter.getItemCount() - 1);
    }

    private void sendImgMsg() {
        PdMessage pdMessage = PdMessage.createPdMessage(mToChatUserImId);
        PdImgMsgBody pdImgMsgBody = new PdImgMsgBody();
        pdImgMsgBody.thumbnailRemoteUrl = "111";
        pdImgMsgBody.remoteUrl = "222";
        pdMessage.addBody(pdImgMsgBody);
        sendMsg(pdMessage);
    }

    @Override
    public void onMessageReceived(PdMessage pdMessage) {
        Log.e(TAG, "onMessageReceived(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                pdMessage.msgContent);
        //属于当前会话的直接标记已读
        if (pdMessage.imMsgId.equals(mToChatUserImId) || pdMessage.msgReceiver.equals(mToChatUserImId)
                || pdMessage.msgSender.equals(mToChatUserImId)) {
            mConversation.markMessageAsRead(pdMessage.imMsgId);
            Message message = getMessage(pdMessage);
            mMsgsAdapter.addItem(message);
            mRvMsg.smoothScrollToPosition(mMsgsAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void onReceiptsMessageReceived(String msgId) {
        //回执消息-消息发送成功
        List<MultiItem> data = mMsgsAdapter.getData();
        List<Message> messages = Utils.transferMultiItem(data);
        for (Message message : messages) {
            if (msgId.equals(message.imMsgId)) {
                message.msgStatus = PdMessage.PDMessageStatus.SUCCESS;
            }
        }
        mMsgsAdapter.setNewItems(Utils.transferList(messages));
    }

    @Override
    public void onFailedMessageReceived(PdMessage pdMessage) {

    }

    private Message getMessage(PdMessage pdMessage) {
        Message message = new Message();
        message.leftItemLayoutId = R.layout.item_text_receive_msg;
        message.rightItemLayoutId = R.layout.item_text_send_msg;
        message.msgDirection = pdMessage.msgDirection;
        message.msgStatus = pdMessage.msgStatus;
        if (message.msgDirection == PdMessage.PDDirection.SEND) {
            message.avatarUrl = "https://ws1.sinaimg.cn/large/0065oQSqly1fytdr77urlj30sg10najf.jpg";
        } else {
            message.avatarUrl = "https://ws1.sinaimg.cn/large/0065oQSqly1g0ajj4h6ndj30sg11xdmj.jpg";
        }
        message.msgContent = pdMessage.msgContent;
        message.imMsgId = pdMessage.imMsgId;
        message.conversationId = pdMessage.conversationId;
        return message;
    }

}
