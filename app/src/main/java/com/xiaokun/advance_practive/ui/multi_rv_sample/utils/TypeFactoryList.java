package com.xiaokun.advance_practive.ui.multi_rv_sample.utils;

import android.view.View;

import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemA;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemB;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemC;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemD;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemE;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.BaseMultiHoder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.TypeAHolder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.TypeBHolder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.TypeCHolder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.TypeDHolder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.TypeEHolder;

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
        return TypeAHolder.LAYOUT;
    }

    @Override
    public int type(ItemB itemB)
    {
        return TypeBHolder.LAYOUT;
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
    public int type(ItemE itemE)
    {
        return TypeEHolder.LAYOUT;
    }

    @Override
    public BaseMultiHoder createViewHolder(View parent, int type)
    {
        BaseMultiHoder baseMultiHoder = null;
        switch (type)
        {
            case TypeAHolder.LAYOUT:
                baseMultiHoder = new TypeAHolder(parent);
                break;
            case TypeBHolder.LAYOUT:
                baseMultiHoder = new TypeBHolder(parent);
                break;
            case TypeCHolder.LAYOUT:
                baseMultiHoder = new TypeCHolder(parent);
                break;
            case TypeDHolder.LAYOUT:
                baseMultiHoder = new TypeDHolder(parent);
                break;
            case TypeEHolder.LAYOUT:
                baseMultiHoder = new TypeEHolder(parent);
                break;
            default:
                throw TypeNotSupportedException.create(String.format("LayoutType: %d", type));
        }
        return baseMultiHoder;
    }
}
