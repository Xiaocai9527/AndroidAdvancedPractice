package com.xiaokun.httpexceptiondemo.ui.multi_rv_sample;

import android.support.annotation.LayoutRes;
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
public class TypeBHoder extends BaseMultiHoder<ItemB>
{

    @LayoutRes
    public static final int LAYOUT = R.layout.type_b_layout;

    public TypeBHoder(View itemView)
    {
        super(itemView);
    }

    @Override
    public void bind(ItemB multiItem)
    {
        ItemB itemB = (ItemB) multiItem;
        ((TextView) itemView.findViewById(R.id.sub_title_tv)).setText(itemB.getDetail());
        ((TextView) itemView.findViewById(R.id.author_tv)).setText(itemB.getAuthor());
        ((TextView) itemView.findViewById(R.id.date_tv)).setText(itemB.getDate());
    }
}
