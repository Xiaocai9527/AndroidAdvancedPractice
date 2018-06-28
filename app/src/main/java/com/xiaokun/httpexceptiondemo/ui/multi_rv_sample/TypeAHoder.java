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
public class TypeAHoder extends BaseMultiHoder<ItemA>
{
    @LayoutRes
    public static final int LAYOUT = R.layout.type_a_layout;

    public TypeAHoder(View itemView)
    {
        super(itemView);
    }

    @Override
    public void bind(ItemA multiItem)
    {
        ((TextView) itemView.findViewById(R.id.title_tv)).setText((multiItem).getTitle());
    }
}
