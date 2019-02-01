package com.xiaokun.baselib.muti_rv;

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
public abstract class BaseMultiHodler<T> extends RecyclerView.ViewHolder {

    public BaseMultiHodler(View itemView) {
        super(itemView);
    }

    public abstract void bind(T multiItem);

}
