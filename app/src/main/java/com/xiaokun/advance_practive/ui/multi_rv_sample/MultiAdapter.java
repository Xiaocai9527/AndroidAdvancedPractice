package com.xiaokun.advance_practive.ui.multi_rv_sample;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.MultiItem;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.BaseMultiHoder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.FooterHoder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.utils.TypeFactory;

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
    public static final int LOADING = 110;
    public static final int LOAD_FAILED = 111;
    public static final int LOAD_COMPLETE = 112;
    public int currentState = LOADING;

    private List<MultiItem> mData;
    private BaseMultiHoder mHolder;
    private LoadFailedClickListener mLoadFailedClickListener;
    private TypeFactory mTypeFactory;

    private boolean mIsLoading = false;
    private int PRE_VISIBLE = 2;
    private boolean mIsShowFooter = true;

    public MultiAdapter(TypeFactory typeFactory)
    {
        mTypeFactory = typeFactory;
        mData = new ArrayList<>();
    }

    public MultiAdapter(TypeFactory typeFactory, List<MultiItem> multiItems)
    {
        mTypeFactory = typeFactory;
        mData = multiItems;
    }

    public void setLoadFailedClickListener(LoadFailedClickListener loadFailedClickListener)
    {
        this.mLoadFailedClickListener = loadFailedClickListener;
    }

    public LoadFailedClickListener getLoadFailedClickListener()
    {
        return mLoadFailedClickListener;
    }

    public void setLoadMoreListener(final LoadMoreListener loadMoreListener, RecyclerView recyclerView)
    {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int mTotalItemCount = layoutManager.getItemCount();
                int mLastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                boolean isBottom = mTotalItemCount <= (mLastVisibleItemPosition + PRE_VISIBLE) ? true : false;
                if (!mIsLoading && isBottom && getCurrentState() == LOADING)
                {
                    loadMoreListener.autoLoad();
                    mIsLoading = true;
                }
            }
        });
    }

    /**
     * 是否展示足布局
     *
     * @param isShowFooter
     */
    public void isShowFooterView(boolean isShowFooter)
    {
        this.mIsShowFooter = isShowFooter;
    }

    /**
     * 设置是否正在上拉加载
     *
     * @param isLoading
     */
    public void setLoading(boolean isLoading)
    {
        this.mIsLoading = isLoading;
    }

    @NonNull
    @Override
    public BaseMultiHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == FooterHoder.TYPE_FOOTER)
        {
            return new FooterHoder(itemView, this);
        }
        return mTypeFactory.createViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMultiHoder holder, int position)
    {
        mHolder = holder;
        if (position < mData.size())
        {
            holder.bind(mData.get(position));
        } else
        {
            bindFooterHolder();
        }
    }

    private void bindFooterHolder()
    {
        if (mHolder instanceof FooterHoder)
        {
            switch (currentState)
            {
                case LOADING:
                    ((FooterHoder) mHolder).showLoad();
                    break;
                case LOAD_COMPLETE:
                    ((FooterHoder) mHolder).showComplete();
                    break;
                case LOAD_FAILED:
                    ((FooterHoder) mHolder).showFailed();
                    break;
                default:

                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (mIsShowFooter)
        {
            if (position == mData.size())
            {
                return FooterHoder.TYPE_FOOTER;
            }
            return mData.get(position).getItemType(mTypeFactory);
        } else
        {
            return mData.get(position).getItemType(mTypeFactory);
        }
    }

    @Override
    public int getItemCount()
    {
        if (mIsShowFooter)
        {
            return mData == null || mData.isEmpty() ? 0 : mData.size() + 1;
        } else
        {
            return mData == null ? 0 : mData.size();
        }
    }

    public int getCurrentState()
    {
        return currentState;
    }

    /**
     * 显示正在加载
     */
    public void loading()
    {
        currentState = LOADING;
        if (mHolder instanceof FooterHoder)
        {
            ((FooterHoder) mHolder).showLoad();
        }
    }

    /**
     * 加载完成
     */
    public void loadComplete()
    {
        currentState = LOAD_COMPLETE;
        if (mHolder instanceof FooterHoder)
        {
            ((FooterHoder) mHolder).showComplete();
        }
    }

    /**
     * 加载失败,点击重试
     */
    public void loadFailed()
    {
        currentState = LOAD_FAILED;
        if (mHolder instanceof FooterHoder)
        {
            ((FooterHoder) mHolder).showFailed();
        }
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
            mIsLoading = false;
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
            mIsLoading = false;
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
        notifyDataSetChanged();
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


    public interface LoadFailedClickListener
    {
        void onClick();
    }

    public interface LoadMoreListener
    {
        void autoLoad();
    }

//    class FooterHoder extends BaseMultiHoder<MultiItem>
//    {
//        private LinearLayout mLoading;
//        private LinearLayout mComplete;
//        private LinearLayout mFailed;
//        private FrameLayout mFooterView;
//
//        public FooterHoder(View itemView)
//        {
//            super(itemView);
//            initView(itemView);
//        }
//
//        @Override
//        public void bind(MultiItem multiItem)
//        {
//
//        }
//
//        public void showComplete()
//        {
//            mFooterView.setVisibility(View.VISIBLE);
//            mComplete.setVisibility(View.VISIBLE);
//            mLoading.setVisibility(View.GONE);
//            mFailed.setVisibility(View.GONE);
//        }
//
//        public void showLoad()
//        {
//            mFooterView.setVisibility(View.VISIBLE);
//            mLoading.setVisibility(View.VISIBLE);
//            mComplete.setVisibility(View.GONE);
//            mFailed.setVisibility(View.GONE);
//        }
//
//        public void showFailed()
//        {
//            mFooterView.setVisibility(View.VISIBLE);
//            mFailed.setVisibility(View.VISIBLE);
//            mLoading.setVisibility(View.GONE);
//            mComplete.setVisibility(View.GONE);
//        }
//
//        private void initView(View itemView)
//        {
//            mLoading = itemView.findViewById(R.id.loading);
//            mComplete = itemView.findViewById(R.id.complete);
//            mFailed = itemView.findViewById(R.id.failed);
//            mFooterView = itemView.findViewById(R.id.footer_view);
//            mFailed.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    if (mLoadFailedClickListener != null)
//                    {
//                        showLoad();
//                        currentState = LOADING;
//                        mLoadFailedClickListener.onClick();
//                    }
//                }
//            });
//        }
//    }
}
