package com.xiaokun.httpexceptiondemo.ui.multi_rv_sample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaokun.httpexceptiondemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/26
 *      描述  ：多种类型的adapter
 *      版本  ：1.0
 * </pre>
 */
public class MultiAdapter extends RecyclerView.Adapter<BaseMultiHoder>
{
    private List<MultiItem> mData;
    private final static int TYPE_FOOTER = 3;
    public static final int TYPE_NONE = 4;
    private BaseMultiHoder mHolder;


    public MultiAdapter()
    {
        mData = new ArrayList<>();
    }

    public MultiAdapter(List<MultiItem> multiItems)
    {
        mData = multiItems;
    }

    /**
     * 添加数据源
     *
     * @param multiItems
     */
    public void addItems(List<MultiItem> multiItems)
    {
        if (multiItems != null && !multiItems.isEmpty())
        {
            int oldSize = getData().size();
            mData.addAll(multiItems);
            notifyItemRangeChanged(oldSize, mData.size());
        }
    }

    /**
     * 添加单个指定位置数据源
     *
     * @param multiItem
     * @param position
     */
    public void addItem(MultiItem multiItem, int position)
    {
        if (position > 0 && position < mData.size())
        {
            mData.add(multiItem);
            notifyItemInserted(position);
        }
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public List<MultiItem> getData()
    {
        return mData;
    }

    /**
     * 移除某一position数据
     *
     * @param position
     */
    public void remove(int position)
    {
        if (position > 0 && position < mData.size())
        {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 清除数据源
     */
    public void clear()
    {
        mData.clear();
    }

    /**
     * 移除所有
     */
    public void removeALL()
    {
        int oldSize = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    @NonNull
    @Override
    public BaseMultiHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        switch (viewType)
        {
            case MultiItem.TYPE_A:
                View typeViewA = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_a_layout, parent,
                        false);
                return new TypeAHoder(typeViewA);
            case MultiItem.TYPE_B:
                View typeViewB = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_b_layout, parent,
                        false);
                return new TypeBHoder(typeViewB);
            case TYPE_FOOTER:
                View footerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_layout, parent,
                        false);
                return new FooterHoder(footerView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMultiHoder holder, int position)
    {
        mHolder = holder;
        if (position < mData.size())
        {
            holder.bindType(mData.get(position));
        } else
        {
            ((FooterHoder) holder).showLoad();
        }
    }

    /**
     * 加载完成
     */
    public void loadComplete()
    {
        if (mHolder instanceof FooterHoder)
        {
            ((FooterHoder) mHolder).showComplete();
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == mData.size())
        {
            return TYPE_FOOTER;
        }
        return mData.get(position).getItemType();
    }

    @Override
    public int getItemCount()
    {
        return mData == null || mData.isEmpty() ? 0 : mData.size() + 1;
    }
}
