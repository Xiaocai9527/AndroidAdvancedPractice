package com.xiaokun.advance_practive.im.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.litho.Diff;
import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.entity.Message;
import com.xiaokun.advance_practive.im.entity.MsgItem;
import com.xiaokun.baselib.muti_rv.BaseMultiHodler;
import com.xiaokun.baselib.muti_rv.HolderFactory;
import com.xiaokun.baselib.muti_rv.MultiAdapter;
import com.xiaokun.baselib.muti_rv.MultiItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/25
 *      描述  ：会话窗口的adapter
 *      版本  ：1.0
 * </pre>
 */
public class MsgsAdapter extends MultiAdapter {

    public MsgsAdapter(HolderFactory holderFactory) {
        super(holderFactory);
    }

    public MsgsAdapter(HolderFactory holderFactory, List<MultiItem> multiItems) {
        super(holderFactory, multiItems);
    }

    @NonNull
    @Override
    public BaseMultiHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(viewType, parent, false);
        return mHolderFactory.createViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMultiHodler holder, int position) {
        mHolder = holder;
        if (position < mData.size()) {
            holder.bind(mData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof MsgItem) {
            MsgItem msgItem = (MsgItem) mData.get(position);
            return ((MsgItem) mData.get(position)).getDirection() == PdMessage.PDDirection.SEND ?
                    msgItem.getRightItemType() : msgItem.getLeftItemType();
        } else {
            throw new IllegalArgumentException("数据源需要实现MsgItem接口");
        }
    }

    public void swapData(List<MultiItem> items) {
        if (mData.isEmpty()) {
            mData.addAll(items);
            notifyDataSetChanged();
        } else {
            List<MultiItem> oldItems = new ArrayList<>(mData);
            mData.addAll(0, items);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return oldItems.size();
                }

                @Override
                public int getNewListSize() {
                    return mData.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Message message = (Message) oldItems.get(oldItemPosition);
                    Message message1 = (Message) mData.get(newItemPosition);
                    if (TextUtils.isEmpty(message.imMsgId)) {
                        return false;
                    }
                    return message.imMsgId.equals(message1.imMsgId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Message message = (Message) oldItems.get(oldItemPosition);
                    Message message1 = (Message) mData.get(newItemPosition);
                    if (TextUtils.isEmpty(message.msgContent)) {
                        return false;
                    }
                    return message.msgContent.equals(message1.msgContent);
                }
            });
            diffResult.dispatchUpdatesTo(this);
        }
    }
}
