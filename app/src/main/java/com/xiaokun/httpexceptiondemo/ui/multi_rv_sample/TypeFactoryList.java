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
public class TypeFactoryList implements TypeFactory
{
    @Override
    public int type(ItemA itemA)
    {
        return TypeAHoder.LAYOUT;
    }

    @Override
    public int type(ItemB itemB)
    {
        return TypeBHoder.LAYOUT;
    }

    @Override
    public int type(ItemC itemC)
    {
        return TypeCHolder.LAYOUT;
    }

    @Override
    public int type(ItemD itemD)
    {
        return TypeDHolder.LAYOUT;
    }

    @Override
    public BaseMultiHoder createViewHolder(View parent, int type)
    {
        BaseMultiHoder baseMultiHoder = null;
        switch (type)
        {
            case TypeAHoder.LAYOUT:
                baseMultiHoder = new TypeAHoder(parent);
                break;
            case TypeBHoder.LAYOUT:
                baseMultiHoder = new TypeBHoder(parent);
                break;
            case TypeCHolder.LAYOUT:
                baseMultiHoder = new TypeCHolder(parent);
                break;
            case TypeDHolder.LAYOUT:
                baseMultiHoder = new TypeDHolder(parent);
                break;
            default:
                throw TypeNotSupportedException.create(String.format("LayoutType: %d", type));
        }
        return baseMultiHoder;
    }
}
