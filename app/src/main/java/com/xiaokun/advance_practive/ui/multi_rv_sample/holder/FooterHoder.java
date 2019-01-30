package com.xiaokun.advance_practive.ui.multi_rv_sample.holder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.ui.multi_rv_sample.MultiAdapter;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.MultiItem;

import static com.xiaokun.advance_practive.ui.multi_rv_sample.MultiAdapter.LOADING;
import static com.xiaokun.advance_practive.ui.multi_rv_sample.MultiAdapter.LOAD_COMPLETE;
import static com.xiaokun.advance_practive.ui.multi_rv_sample.MultiAdapter.LOAD_FAILED;


/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/29
 *      描述  ：足布局
 *      版本  ：1.0
 * </pre>
 */
public class FooterHoder extends BaseMultiHoder<MultiItem> {
    public static int TYPE_FOOTER = R.layout.footer_layout;

    private LinearLayout mLoading;
    private LinearLayout mComplete;
    private LinearLayout mFailed;
    private FrameLayout mFooterView;
    private MultiAdapter mMultiAdapter;

    public FooterHoder(View itemView, MultiAdapter multiAdapter) {
        super(itemView);
        initView(itemView);
        this.mMultiAdapter = multiAdapter;
    }

    @Override
    public void bind(MultiItem multiItem) {

    }

    public void bindFooterHolder(int currentState) {
        switch (currentState) {
            case LOADING:
                showLoad();
                break;
            case LOAD_COMPLETE:
                showComplete();
                break;
            case LOAD_FAILED:
                showFailed();
                break;
            default:

                break;
        }
    }

    public void showComplete() {
        mFooterView.setVisibility(View.VISIBLE);
        mComplete.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
        mFailed.setVisibility(View.GONE);
    }

    public void showLoad() {
        mFooterView.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.VISIBLE);
        mComplete.setVisibility(View.GONE);
        mFailed.setVisibility(View.GONE);
    }

    public void showFailed() {
        mFooterView.setVisibility(View.VISIBLE);
        mFailed.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
        mComplete.setVisibility(View.GONE);
    }

    private void initView(View itemView) {
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
