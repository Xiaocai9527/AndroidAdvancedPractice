package com.xiaokun.advance_practive.im.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.entity.MsgItem;
import com.xiaokun.baselib.muti_rv.BaseMultiHodler;
import com.xiaokun.baselib.muti_rv.HolderFactory;
import com.xiaokun.baselib.muti_rv.MultiAdapter;
import com.xiaokun.baselib.muti_rv.MultiItem;

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
}
