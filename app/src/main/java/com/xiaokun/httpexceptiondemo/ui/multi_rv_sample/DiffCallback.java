package com.xiaokun.httpexceptiondemo.ui.multi_rv_sample;

import android.support.v7.util.DiffUtil;

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

    public DiffCallback(List<MultiItem> oldDatas, List<MultiItem> newDatas)
    {
        mOldDatas = oldDatas;
        mNewDatas = newDatas;
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
        if (oldItem instanceof Item1 && newItem instanceof Item1)
        {
            int itemType = ((Item1) oldItem).getItemType();
            String title = ((Item1) oldItem).getTitle();
            int itemType1 = newItem.getItemType();
            String title1 = ((Item1) newItem).getTitle();

            if (!title.equals(title1) || itemType != itemType1)
            {
                return false;
            }
        }
        if (oldItem instanceof Item2 && newItem instanceof Item2)
        {
            int itemType = ((Item2) oldItem).getItemType();
            String detail = ((Item2) oldItem).getDetail();
            String author = ((Item2) oldItem).getAuthor();
            String date = ((Item2) oldItem).getDate();

            int itemType1 = newItem.getItemType();
            String detail1 = ((Item2) newItem).getDetail();
            String author1 = ((Item2) newItem).getAuthor();
            String date1 = ((Item2) newItem).getDate();

            if (!detail.equals(detail1) || itemType != itemType1 || !author.equals(author1) || !date.equals(date1))
            {
                return false;
            }
        }
        return true;
    }

}
