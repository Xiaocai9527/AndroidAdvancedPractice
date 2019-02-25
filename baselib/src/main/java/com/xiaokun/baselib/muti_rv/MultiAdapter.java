package com.xiaokun.baselib.muti_rv;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaokun.baselib.R;

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
public class MultiAdapter extends RecyclerView.Adapter<BaseMultiHodler> implements StickHeaderItemDecoration.StickyHeaderInterface {

    private static final String TAG = "MultiAdapter";
    public static final int LOADING = 110;
    public static final int LOAD_FAILED = 111;
    public static final int LOAD_COMPLETE = 112;
    public int currentState = LOADING;

    protected Context mContext;
    protected RecyclerView mRecyclerView;
    protected List<MultiItem> mData;
    protected BaseMultiHodler mHolder;
    protected LoadFailedClickListener mLoadFailedClickListener;
    protected HolderFactory mHolderFactory;
    /**
     * 默认没有足布局
     */
    private boolean mIsShowFooter = false;
    private BaseFooterHodler mFooterHolder;

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

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public BaseMultiHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(viewType, parent, false);

        if (mFooterHolder != null && mFooterHolder.getLayoutRes() == viewType) {
            return mFooterHolder;
        } else if (mFooterHolder == null && mIsShowFooter) {
            mFooterHolder = new FooterHodler(itemView, this);
            if (FooterHodler.TYPE_FOOTER == viewType) {
                return mFooterHolder;
            }
        }
        return mHolderFactory.createViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMultiHodler holder, int position) {
        mHolder = holder;
        if (position < mData.size()) {
            holder.bind(mData.get(position));
        } else if (position == mData.size() && holder instanceof FooterHodler) {
            mFooterHolder.bindFooterHolder(currentState);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsShowFooter && position == mData.size()) {
            return mFooterHolder.getLayoutRes();
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

    public void setFooterHolder(BaseFooterHodler footerHolder) {
        mFooterHolder = footerHolder;
    }

    public int getCurrentState() {
        return currentState;
    }

    public Context getContext() {
        return mContext;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 显示正在加载
     */
    public void loading() {
        currentState = LOADING;
        if (mFooterHolder != null) {
            mFooterHolder.showLoad();
        }
    }

    /**
     * 加载完成
     */
    public void loadComplete() {
        currentState = LOAD_COMPLETE;
        if (mFooterHolder != null) {
            mFooterHolder.showComplete();
        }
    }

    /**
     * 加载失败,点击重试
     */
    public void loadFailed() {
        currentState = LOAD_FAILED;
        if (mFooterHolder != null) {
            mFooterHolder.showFailed();
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
        }
    }

    /**
     * 添加数据源,使用场景下拉加载
     *
     * @param multiItems
     */
    public void addStartItems(List<MultiItem> multiItems) {
        if (multiItems != null && !multiItems.isEmpty()) {
            int oldSize = getData().size();
            mData.addAll(0, multiItems);
            notifyItemRangeInserted(0, multiItems.size() - 1);
            //notifyItemRangeChanged(0, multiItems.size() - 1);
            //notifyDataSetChanged();
        }
    }

    /**
     * 设置新数据源
     *
     * @param multiItems
     */
    public void setNewItems(List<MultiItem> multiItems) {
        if (multiItems != null && !multiItems.isEmpty()) {
            mData.clear();
            mData.addAll(multiItems);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加单个指定位置数据源
     *
     * @param multiItem
     * @param position
     */
    public void addItem(MultiItem multiItem, int position) {
        if (position > 0 && position <= mData.size()) {
            mData.add(position, multiItem);
            notifyItemInserted(position);
        }
    }

    /**
     * 直接往最后添加一条数据
     *
     * @param multiItem
     */
    public void addItem(MultiItem multiItem) {
        mData.add(getItemCount(), multiItem);
        notifyItemInserted(getItemCount());
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

    @LayoutRes
    private int mHeaderLayoutId;

    public void setHeaderLayout(@LayoutRes int headerLayoutId) {
        mHeaderLayoutId = headerLayoutId;
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = 0;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    @Override
    public int getHeaderLayout(int headerPosition) {
        return mData.get(headerPosition).getItemType();
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {

    }

    @Override
    public boolean isHeader(int itemPosition) {
        if (mData.get(itemPosition).getItemType() == mHeaderLayoutId) {
            return true;
        } else {
            return false;
        }
    }

    public interface LoadFailedClickListener {
        void onClick();
    }

    public interface LoadMoreListener {
        void autoLoad();
    }
}
