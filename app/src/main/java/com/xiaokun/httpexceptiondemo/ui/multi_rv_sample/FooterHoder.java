package com.xiaokun.httpexceptiondemo.ui.multi_rv_sample;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.xiaokun.httpexceptiondemo.R;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/27
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class FooterHoder extends BaseMultiHoder
{
    private LinearLayout mLoading;
    private LinearLayout mComplete;
    private LinearLayout mFailed;

    public FooterHoder(View itemView)
    {
        super(itemView);
        initView(itemView);
    }

    @Override
    public void bindType(MultiItem multiItem)
    {

    }

    public void showComplete()
    {
        mComplete.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
        mFailed.setVisibility(View.GONE);
    }

    public void showLoad()
    {
        mLoading.setVisibility(View.VISIBLE);
        mComplete.setVisibility(View.GONE);
        mFailed.setVisibility(View.GONE);
    }

    public void showFailed()
    {
        mFailed.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
        mComplete.setVisibility(View.GONE);
    }

    private void initView(View itemView)
    {
        mLoading = itemView.findViewById(R.id.loading);
        mComplete = itemView.findViewById(R.id.complete);
        mFailed = itemView.findViewById(R.id.failed);
    }
}
