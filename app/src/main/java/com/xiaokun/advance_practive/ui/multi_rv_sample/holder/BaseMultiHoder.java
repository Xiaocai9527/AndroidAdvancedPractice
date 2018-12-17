package com.xiaokun.advance_practive.ui.multi_rv_sample.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/27
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public abstract class BaseMultiHoder<T> extends RecyclerView.ViewHolder
{
    public BaseMultiHoder(View itemView)
    {
        super(itemView);
    }

    public abstract void bind(T multiItem);

}
