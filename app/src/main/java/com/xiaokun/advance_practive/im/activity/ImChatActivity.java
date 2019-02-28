package com.xiaokun.advance_practive.im.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
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
import com.xiaokun.advance_practive.im.adapter.holder.ImageHolder;
import com.xiaokun.advance_practive.im.adapter.holder.TextHolder;
import com.xiaokun.advance_practive.im.database.bean.PdConversation;
import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdImgMsgBody;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdMsgBody;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdTextMsgBody;
import com.xiaokun.advance_practive.im.entity.Message;
import com.xiaokun.advance_practive.im.util.TextWatcherHelper;
import com.xiaokun.advance_practive.im.util.Utils;
import com.xiaokun.baselib.muti_rv.MultiItem;
import com.xiaokun.baselib.util.PermissionHelper;

import java.util.ArrayList;
import java.util.List;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;
import cn.dreamtobe.kpswitch.widget.KPSwitchRootLinearLayout;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/25
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class ImChatActivity extends AppCompatActivity implements View.OnClickListener, PdMessageListener {

    private static final String TAG = "ImChatActivity";

    private static final String KEY_TO_CHAT_USER_ID = "TO_CHAT_USER_ID";
    //    private SwipeRefreshLayout mSwLayout;
    private RecyclerView mRvMsg;
    //    private EditText mEtMsg;
//    private Button mBtnSend;
    private PdConversation mConversation;
    private String mToChatUserImId;
    private MsgsAdapter mMsgsAdapter;
    private LinearLayout mSendMsgLayout;
    private ImageView mVoiceTextSwitchIv;
    private EditText mSendEdt;
    private TextView mSendVoiceBtn;
    private ImageView mPlusIv;
    private TextView mSendBtn;
    private KPSwitchPanelLinearLayout mPanelRoot;
    private LinearLayout mPanelContent;
    private TextView mSendImgTv;
    private KPSwitchRootLinearLayout mKvLayout;
    private boolean mIsShowing;
    private TextView mSendPhotoTv;
    //    private List<Message> mMessages = new ArrayList<>();

    public static void start(Context context, String toChatUserId) {
        Intent starter = new Intent(context, ImChatActivity.class);
        starter.putExtra(KEY_TO_CHAT_USER_ID, toChatUserId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_chat);
        mToChatUserImId = getIntent().getStringExtra(KEY_TO_CHAT_USER_ID);

        initView();

        mConversation = PdIMClient.getInstance().getChatManager().getConversation(mToChatUserImId);
        loadMsgs(mPageNum, mPageSize);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PdIMClient.getInstance().getChatManager().addMessageListener(this);
        mConversation.markAllMessagesAsRead();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PdIMClient.getInstance().getChatManager().removeMessageListener(this);
    }

    private int mPageNum = 1;
    private int mPageSize = 10;

    private void initView() {
        mRvMsg = findViewById(R.id.rv_msg);
        mKvLayout = findViewById(R.id.kv_layout);
        mSendMsgLayout = findViewById(R.id.sendMsgLayout);
        mVoiceTextSwitchIv = findViewById(R.id.voice_text_switch_iv);
        mSendEdt = findViewById(R.id.send_edt);
        mSendVoiceBtn = findViewById(R.id.send_voice_btn);
        mPlusIv = findViewById(R.id.plus_iv);
        mSendBtn = findViewById(R.id.send_btn);
        mPanelRoot = findViewById(R.id.panel_root);
        mPanelContent = findViewById(R.id.panel_content);
        mSendImgTv = findViewById(R.id.send_img_tv);
        mSendPhotoTv = findViewById(R.id.send_photo_tv);

        final ClassicsFooter footer = findViewById(R.id.footer);
        View arrow = footer.findViewById(ClassicsFooter.ID_IMAGE_ARROW);
        arrow.setScaleY(-1);

        final SmartRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
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

        initListener(mSendVoiceBtn, mSendBtn, mVoiceTextSwitchIv, mSendImgTv, mSendPhotoTv);

        mSendEdt.addTextChangedListener(new TextWatcherHelper() {
            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    mPlusIv.setVisibility(View.VISIBLE);
                    mSendBtn.setVisibility(View.GONE);
                } else {
                    mPlusIv.setVisibility(View.GONE);
                    mSendBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        KeyboardUtil.attach(this, mPanelRoot, new KeyboardUtil.OnKeyboardShowingListener() {
            @Override
            public void onKeyboardShowing(boolean isShowing) {
                Log.d(TAG, String.format("Keyboard is %s", isShowing
                        ? "showing" : "hiding"));
                mRvMsg.scrollToPosition(mMsgsAdapter.getItemCount() - 1);
                mIsShowing = isShowing;
            }
        });

        KPSwitchConflictUtil.attach(mPanelRoot, mPlusIv, mSendEdt, new KPSwitchConflictUtil.SwitchClickListener() {
            @Override
            public void onClickSwitch(boolean switchToPanel) {
                if (switchToPanel) {
                    mSendEdt.clearFocus();
                } else {
                    mSendEdt.requestFocus();
                }
                mRvMsg.scrollToPosition(mMsgsAdapter.getItemCount() - 1);
            }
        });

        MsgHolderFactoryList holder = MsgHolderFactoryList.getInstance()
                .addTypeHolder(TextHolder.class, R.layout.item_text_receive_msg, R.layout.item_text_send_msg)
                .addTypeHolder(ImageHolder.class, R.layout.item_img_receive_msg, R.layout.item_img_send_msg);
        mMsgsAdapter = new MsgsAdapter(holder);
        mRvMsg.setAdapter(mMsgsAdapter);

        mRvMsg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelRoot);
                }
                return false;
            }
        });
    }

    private void loadMsgs(int pageNum, int pageSize) {
        List<PdMessage> pdMessages = mConversation.loadMsgs(pageNum, pageSize);
        if (pdMessages.isEmpty()) {
            Toast.makeText(this, "没有更多消息了~", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Message> mMessages = new ArrayList<>();
        for (PdMessage pdMessage : pdMessages) {
            mMessages.add(getMessage(pdMessage));
        }
        mMsgsAdapter.swapData(Utils.transferList(mMessages));
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
            case R.id.send_voice_btn:
                Toast.makeText(this, "录音", Toast.LENGTH_SHORT).show();
                break;
            case R.id.send_btn:
                String msg = mSendEdt.getText().toString().trim();
                //暂时发送文本
                PdMessage pdMessage = PdMessage.createPdMessage(mToChatUserImId);
                PdTextMsgBody pdTextMsgBody = new PdTextMsgBody();
                pdTextMsgBody.content = msg;
                pdMessage.addBody(pdTextMsgBody);
                pdMessage.msgContent = msg;
                sendMsg(pdMessage);
                mSendEdt.setText("");
                break;
            case R.id.voice_text_switch_iv:
                if (mSendVoiceBtn.getVisibility() == View.GONE) {
                    mSendVoiceBtn.setVisibility(View.VISIBLE);
                    mSendEdt.setVisibility(View.GONE);
                    KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelRoot);
                } else {
                    mSendVoiceBtn.setVisibility(View.GONE);
                    mSendEdt.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.send_img_tv:
                //点击打开相册
                requestStoragePerm();
                break;
            case R.id.send_photo_tv:
                //点击打开相机
                requestCameraPerm();
                break;
            default:
                break;
        }
    }

    /**
     * 存储权限和相机权限
     */
    String[] storagePerm = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] cameraPerm = {Manifest.permission.CAMERA};

    private void requestCameraPerm() {
        PermissionHelper.init(this)
                .permissions(cameraPerm)
                .rationale("拍照发送图片需要用到相机权限")
                .requestCode(1)
                .permissionListener(new PermissionHelper.PermissionListener() {
                    @Override
                    public void onPermissionsGranted(int requestCode, List<String> perms) {
//                        selectPicFromCamera();
                    }

                    @Override
                    public void onPermissionsDenied(int requestCode, List<String> perms) {
                        Toast.makeText(ImChatActivity.this, "应用缺少相机权限", Toast.LENGTH_SHORT).show();
                    }
                }).build().request();
    }

    private void requestStoragePerm() {
        PermissionHelper.init(this)
                .permissions(storagePerm)
                .rationale("打开图片文件需要用到存储权限")
                .requestCode(2)
                .permissionListener(new PermissionHelper.PermissionListener() {
                    @Override
                    public void onPermissionsGranted(int requestCode, List<String> perms) {
//                        selectPicFromLocal();
                    }

                    @Override
                    public void onPermissionsDenied(int requestCode, List<String> perms) {
                        Toast.makeText(ImChatActivity.this, "应用缺少存储权限", Toast.LENGTH_SHORT).show();
                    }
                }).build().request();
    }

    /**
     * select local image
     */
//    protected void selectPicFromLocal() {
//        PictureSelector.create(this)
//                .openGallery(PictureMimeType.ofImage())
//                .theme(R.style.selectPictureStyle)
//                .selectionMode(PictureConfig.SINGLE)
//                .previewImage(true)
//                .compress(true)
//                .compressSavePath(getCacheDir())
//                .isGif(false)
//                .isCamera(false)
//                .forResult(REQUEST_CODE_LOCAL);
//    }

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
        Log.e(TAG, "onFailedMessageReceived(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + "失败消息");
        //更新失败消息
        if (pdMessage.imMsgId.equals(mToChatUserImId) || pdMessage.msgReceiver.equals(mToChatUserImId)
                || pdMessage.msgSender.equals(mToChatUserImId)) {
            List<MultiItem> data = mMsgsAdapter.getData();
            List<Message> messages = Utils.transferMultiItem(data);
            int index = -1;
            Message updateMsg = null;
            for (Message message : messages) {
                if (message.imMsgId.equals(pdMessage.imMsgId)) {
                    index = messages.indexOf(message);
                    message.msgStatus = PdMessage.PDMessageStatus.FAIL;
                    updateMsg = message;
                    break;
                }
            }
            if (index == -1) {
                return;
            }
            mMsgsAdapter.updateItem(updateMsg, index);
        }
    }

    private Message getMessage(PdMessage pdMessage) {
        Message message = new Message();
        switch (pdMessage.msgType) {
            case PdMsgBody.PDMessageBodyType_TEXT:
                message.leftItemLayoutId = R.layout.item_text_receive_msg;
                message.rightItemLayoutId = R.layout.item_text_send_msg;
                break;
            case PdMsgBody.PDMessageBodyType_IMAGE:
                message.leftItemLayoutId = R.layout.item_img_receive_msg;
                message.rightItemLayoutId = R.layout.item_img_send_msg;
                break;
            default:
                message.leftItemLayoutId = R.layout.item_text_receive_msg;
                message.rightItemLayoutId = R.layout.item_text_send_msg;
                break;
        }

        message.msgDirection = pdMessage.msgDirection;
        message.msgStatus = pdMessage.msgStatus;
        message.pdMsgBody = pdMessage.pdMsgBody;
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && (mIsShowing || mPanelRoot.getVisibility() == View.VISIBLE)) {
            KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelRoot);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
