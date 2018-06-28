package com.xiaokun.httpexceptiondemo.ui.multi_rv_sample;


import android.view.View;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public interface TypeFactory
{
    int type(ItemA itemA);

    int type(ItemB itemB);

    int type(ItemC itemC);

    int type(ItemD itemD);

    BaseMultiHoder createViewHolder(View parent, int type);
}
