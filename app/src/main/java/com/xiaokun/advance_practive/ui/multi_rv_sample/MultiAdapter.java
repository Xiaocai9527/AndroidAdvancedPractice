package com.xiaokun.advance_practive.ui.multi_rv_sample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.MultiItem;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.BaseMultiHoder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.FooterHoder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.utils.HolderFactory;

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
public class MultiAdapter extends RecyclerView.Adapter<BaseMultiHoder> {

    private static final String TAG = "MultiAdapter";
    public static final int LOADING = 110;
    public static final int LOAD_FAILED = 111;
    public static final int LOAD_COMPLETE = 112;
    public int currentState = LOADING;

    private List<MultiItem> mData;
    private BaseMultiHoder mHolder;
    private LoadFailedClickListener mLoadFailedClickListener;
    private HolderFactory mHolderFactory;

    //private boolean mIsLoading = false;
    private int PRE_VISIBLE = 2;
    private boolean mIsShowFooter = true;

    public MultiAdapter(HolderFactory holderFactory) {
        mHolderFactory = holderFactory;
        mData = new ArrayList<>();
    }

    public MultiAdapter(HolderFactory holderFactory, List<MultiItem> multiItems) {
        mHolderFactory = holderFactory;
        mData = multiItems;
    }

    public void setLoadFailedClickListener(LoadFailedClickListener loadFailedClickListener) {
        this.mLoadFailedClickListener = loadFailedClickListener;
    }

    public LoadFailedClickListener getLoadFailedClickListener() {
        return mLoadFailedClickListener;
    }

    /**
     * 是否展示足布局
     *
     * @param isShowFooter
     */
    public void isShowFooterView(boolean isShowFooter) {
        this.mIsShowFooter = isShowFooter;
    }

//    /**
//     * 设置是否正在上拉加载
//     *
//     * @param isLoading
//     */
//    public void setLoading(boolean isLoading) {
//        this.mIsLoading = isLoading;
//    }

    @NonNull
    @Override
    public BaseMultiHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == FooterHoder.TYPE_FOOTER) {
            return new FooterHoder(itemView, this);
        }
        return mHolderFactory.createViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMultiHoder holder, int position) {
        mHolder = holder;
        if (position < mData.size()) {
            holder.bind(mData.get(position));
        } else if (position == mData.size() && holder instanceof FooterHoder) {
            ((FooterHoder) holder).bindFooterHolder(currentState);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsShowFooter && position == mData.size()) {
            return FooterHoder.TYPE_FOOTER;
        } else {
            return mData.get(position).getItemType();
        }
    }

    @Override
    public int getItemCount() {
        if (mIsShowFooter) {
            return mData == null || mData.isEmpty() ? 0 : mData.size() + 1;
        } else {
            return mData == null ? 0 : mData.size();
        }
    }

    public int getCurrentState() {
        return currentState;
    }

    /**
     * 显示正在加载
     */
    public void loading() {
        currentState = LOADING;
        if (mHolder instanceof FooterHoder) {
            ((FooterHoder) mHolder).showLoad();
        }
    }

    /**
     * 加载完成
     */
    public void loadComplete() {
        currentState = LOAD_COMPLETE;
        if (mHolder instanceof FooterHoder) {
            ((FooterHoder) mHolder).showComplete();
        }
    }

    /**
     * 加载失败,点击重试
     */
    public void loadFailed() {
        currentState = LOAD_FAILED;
        if (mHolder instanceof FooterHoder) {
            ((FooterHoder) mHolder).showFailed();
        }
    }

    /**
     * 添加数据源
     *
     * @param multiItems
     */
    public void addItems(List<MultiItem> multiItems) {
        if (multiItems != null && !multiItems.isEmpty()) {
            int oldSize = getData().size();
            mData.addAll(multiItems);
            notifyItemRangeChanged(oldSize, mData.size());
            //mIsLoading = false;
        }
    }

    /**
     * 添加单个指定位置数据源
     *
     * @param multiItem
     * @param position
     */
    public void addItem(MultiItem multiItem, int position) {
        if (position > 0 && position < mData.size()) {
            mData.add(multiItem);
            notifyItemInserted(position);
            //mIsLoading = false;
        }
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public List<MultiItem> getData() {
        return mData;
    }

    /**
     * 移除某一position数据
     *
     * @param position
     */
    public void remove(int position) {
        if (position > 0 && position < mData.size()) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 清除数据源
     */
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * 移除所有
     */
    public void removeALL() {
        int oldSize = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    public interface LoadFailedClickListener {
        void onClick();
    }

    public interface LoadMoreListener {
        void autoLoad();
    }
}
