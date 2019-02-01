package com.xiaokun.advance_practive.ui.multi_rv_sample.holder;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xiaokun.advance_practive.R;
import com.xiaokun.baselib.muti_rv.BaseFooterHodler;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/01/31
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class CustomFooterHolder extends BaseFooterHodler {
    private static final String TAG = "CustomFooterHolder";
    public static int TYPE_FOOTER = R.layout.footer_layout;

    private LinearLayout mLoading;
    private LinearLayout mComplete;
    private LinearLayout mFailed;
    private FrameLayout mFooterView;
    private FailedClickListener mFailedClickListener;


    public CustomFooterHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    public CustomFooterHolder(View itemView, FailedClickListener failedClickListener) {
        super(itemView);
        initView(itemView);
        mFailedClickListener = failedClickListener;
    }

    public void setFailedClickListener(FailedClickListener failedClickListener) {
        mFailedClickListener = failedClickListener;
    }

    protected void initView(View itemView) {
        Log.e(TAG, "initView(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")");
        mLoading = itemView.findViewById(com.xiaokun.baselib.R.id.loading);
        mComplete = itemView.findViewById(com.xiaokun.baselib.R.id.complete);
        mFailed = itemView.findViewById(com.xiaokun.baselib.R.id.failed);
        mFooterView = itemView.findViewById(com.xiaokun.baselib.R.id.footer_view);

        mFailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFailedClickListener != null) {
                    mFailedClickListener.onFailedClick();
                    showLoad();
                }
            }
        });
    }

    @Override
    public void showLoad() {
        mFooterView.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.VISIBLE);
        mComplete.setVisibility(View.GONE);
        mFailed.setVisibility(View.GONE);
    }

    @Override
    public void showComplete() {
        mFooterView.setVisibility(View.VISIBLE);
        mComplete.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
        mFailed.setVisibility(View.GONE);
    }

    @Override
    public void showFailed() {
        mFooterView.setVisibility(View.VISIBLE);
        mFailed.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
        mComplete.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutRes() {
        return TYPE_FOOTER;
    }

    public interface FailedClickListener {
        void onFailedClick();
    }

}
