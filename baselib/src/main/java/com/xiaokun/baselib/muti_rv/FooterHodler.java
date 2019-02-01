package com.xiaokun.baselib.muti_rv;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.xiaokun.baselib.R;

import static com.xiaokun.baselib.muti_rv.MultiAdapter.LOADING;
import static com.xiaokun.baselib.muti_rv.MultiAdapter.LOAD_COMPLETE;
import static com.xiaokun.baselib.muti_rv.MultiAdapter.LOAD_FAILED;


/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/29
 *      描述  ：足布局
 *      版本  ：1.0
 * </pre>
 */
public class FooterHodler extends BaseFooterHodler{
    public static int TYPE_FOOTER = R.layout.footer_layout;

    private LinearLayout mLoading;
    private LinearLayout mComplete;
    private LinearLayout mFailed;
    private FrameLayout mFooterView;
    private MultiAdapter mMultiAdapter;

    public FooterHodler(View itemView, MultiAdapter multiAdapter) {
        super(itemView);
        initView(itemView);
        this.mMultiAdapter = multiAdapter;
    }

    @Override
    public void showComplete() {
        mFooterView.setVisibility(View.VISIBLE);
        mComplete.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
        mFailed.setVisibility(View.GONE);
    }

    @Override
    public void showLoad() {
        mFooterView.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.VISIBLE);
        mComplete.setVisibility(View.GONE);
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
        return R.layout.footer_layout;
    }

    protected void initView(View itemView) {
        mLoading = itemView.findViewById(R.id.loading);
        mComplete = itemView.findViewById(R.id.complete);
        mFailed = itemView.findViewById(R.id.failed);
        mFooterView = itemView.findViewById(R.id.footer_view);
        mFailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiAdapter.LoadFailedClickListener loadFailedClickListener = mMultiAdapter
                        .getLoadFailedClickListener();
                if (loadFailedClickListener != null) {
                    showLoad();
                    mMultiAdapter.currentState = LOADING;
                    loadFailedClickListener.onClick();
                }
            }
        });
    }
}
