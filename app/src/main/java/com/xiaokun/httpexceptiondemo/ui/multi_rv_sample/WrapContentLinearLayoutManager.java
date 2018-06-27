package com.xiaokun.httpexceptiondemo.ui.multi_rv_sample;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/27
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class WrapContentLinearLayoutManager extends LinearLayoutManager
{
    public WrapContentLinearLayoutManager(Context context)
    {
        super(context);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state)
    {
        try
        {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e)
        {
            Log.e("probe", "meet a IOOBE in RecyclerView");
        }
    }
}
