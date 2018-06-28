package com.xiaokun.httpexceptiondemo.ui.multi_rv_sample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
    public static final int LOADING = 110;
    public static final int LOAD_FAILED = 111;
    public static final int LOAD_COMPLETE = 112;
    private int currentState = LOADING;

    private List<MultiItem> mData;
    private final static int TYPE_FOOTER = R.layout.footer_layout;
    private BaseMultiHoder mHolder;
    private LoadFailedClickListener mLoadFailedClickListener;
    private TypeFactory mTypeFactory;

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

    @NonNull
    @Override
    public BaseMultiHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == TYPE_FOOTER)
        {
            return new FooterHoder(itemView);
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
            switch (currentState)
            {
                case LOADING:
                    ((FooterHoder) holder).showLoad();
                    break;
                case LOAD_COMPLETE:
                    ((FooterHoder) holder).showComplete();
                    break;
                case LOAD_FAILED:
                    ((FooterHoder) holder).showFailed();
                    break;
                default:

                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == mData.size())
        {
            return TYPE_FOOTER;
        }
        return mData.get(position).getItemType(mTypeFactory);
    }

    @Override
    public int getItemCount()
    {
        return mData == null || mData.isEmpty() ? 0 : mData.size() + 1;
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

    interface LoadFailedClickListener
    {
        void onClick();
    }

    class FooterHoder extends BaseMultiHoder<MultiItem>
    {
        private LinearLayout mLoading;
        private LinearLayout mComplete;
        private LinearLayout mFailed;

        public FooterHoder(View itemView)
        {
            super(itemView);
            initView(itemView);
        }

        @Override
        public void bind(MultiItem multiItem)
        {

        }

        public void showComplete()
        {
            mComplete.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.GONE);
            mFailed.setVisibility(View.GONE);
        }

        public void showLoad()
        {
            mLoading.setVisibility(View.VISIBLE);
            mComplete.setVisibility(View.GONE);
            mFailed.setVisibility(View.GONE);
        }

        public void showFailed()
        {
            mFailed.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.GONE);
            mComplete.setVisibility(View.GONE);
        }

        private void initView(View itemView)
        {
            mLoading = itemView.findViewById(R.id.loading);
            mComplete = itemView.findViewById(R.id.complete);
            mFailed = itemView.findViewById(R.id.failed);
            mFailed.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mLoadFailedClickListener != null)
                    {
                        showLoad();
                        currentState = LOADING;
                        mLoadFailedClickListener.onClick();
                    }
                }
            });
        }
    }
}
