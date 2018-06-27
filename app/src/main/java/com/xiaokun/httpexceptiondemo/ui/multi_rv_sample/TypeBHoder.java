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
public class TypeBHoder extends BaseMultiHoder
{
    public TypeBHoder(View itemView)
    {
        super(itemView);
    }

    @Override
    public void bindType(MultiItem multiItem)
    {
        Item2 item2 = (Item2) multiItem;
        ((TextView) itemView.findViewById(R.id.sub_title_tv)).setText(item2.getDetail());
        ((TextView) itemView.findViewById(R.id.author_tv)).setText(item2.getAuthor());
        ((TextView) itemView.findViewById(R.id.date_tv)).setText(item2.getDate());
    }
}
