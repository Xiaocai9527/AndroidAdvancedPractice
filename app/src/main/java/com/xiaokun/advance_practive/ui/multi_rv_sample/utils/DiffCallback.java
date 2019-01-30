package com.xiaokun.advance_practive.ui.multi_rv_sample.utils;

import android.support.v7.util.DiffUtil;

import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemA;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemB;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.MultiItem;

import java.util.List;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/27
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class DiffCallback extends DiffUtil.Callback
{

    private List<MultiItem> mOldDatas, mNewDatas;
    private HolderFactory mHolderFactory;

    public DiffCallback(List<MultiItem> oldDatas, List<MultiItem> newDatas, HolderFactory holderFactory)
    {
        mOldDatas = oldDatas;
        mNewDatas = newDatas;
        mHolderFactory = holderFactory;
    }

    @Override
    public int getOldListSize()
    {
        return mOldDatas == null ? 0 : mOldDatas.size();
    }

    @Override
    public int getNewListSize()
    {
        return mNewDatas == null ? 0 : mNewDatas.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition)
    {
        return mOldDatas.get(oldItemPosition).equals(mNewDatas.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition)
    {
        MultiItem oldItem = mOldDatas.get(oldItemPosition);
        MultiItem newItem = mNewDatas.get(newItemPosition);
        if (oldItem instanceof ItemA && newItem instanceof ItemA)
        {
            int itemType = ((ItemA) oldItem).getItemType();
            String title = ((ItemA) oldItem).getTitle();
            int itemType1 = newItem.getItemType();
            String title1 = ((ItemA) newItem).getTitle();

            if (!title.equals(title1) || itemType != itemType1)
            {
                return false;
            }
        }
        if (oldItem instanceof ItemB && newItem instanceof ItemB)
        {
            int itemType = ((ItemB) oldItem).getItemType();
            String detail = ((ItemB) oldItem).getDetail();
            String author = ((ItemB) oldItem).getAuthor();
            String date = ((ItemB) oldItem).getDate();

            int itemType1 = newItem.getItemType();
            String detail1 = ((ItemB) newItem).getDetail();
            String author1 = ((ItemB) newItem).getAuthor();
            String date1 = ((ItemB) newItem).getDate();

            if (!detail.equals(detail1) || itemType != itemType1 || !author.equals(author1) || !date.equals(date1))
            {
                return false;
            }
        }
        return true;
    }

}
