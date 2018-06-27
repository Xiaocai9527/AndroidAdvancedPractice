package com.xiaokun.httpexceptiondemo.ui.multi_rv_sample;

import android.view.View;
import android.widget.TextView;

import com.xiaokun.httpexceptiondemo.R;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/27
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class TypeAHoder extends BaseMultiHoder
{
    public TypeAHoder(View itemView)
    {
        super(itemView);
    }

    @Override
    public void bindType(MultiItem multiItem)
    {
        ((TextView) itemView.findViewById(R.id.title_tv)).setText(((Item1) multiItem).getTitle());
    }
}
