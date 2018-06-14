package com.xiaokun.httpexceptiondemo.ui.big_mvp.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaokun.httpexceptiondemo.Constants;
import com.xiaokun.httpexceptiondemo.R;
import com.xiaokun.httpexceptiondemo.network.entity.ListResEntity;
import com.xiaokun.httpexceptiondemo.rx.util.RxBus;
import com.xiaokun.httpexceptiondemo.ui.big_mvp.BigMvpActivity;
import com.xiaokun.httpexceptiondemo.util.Preconditions;

import java.util.List;


/**
 * Created by 肖坤 on 2018/6/3.
 *
 * @author 肖坤
 * @date 2018/6/3
 */

public class ListFragment extends Fragment implements ListContract.View
{
    private ListContract.Presenter listPresenter;
    private ListAdapter mListAdapter;
    private RecyclerView mListRv;
    private ProgressBar mListPb;

    public ListFragment()
    {
    }

    public static ListFragment newInstance()
    {
        Bundle args = new Bundle();
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mListAdapter = new ListAdapter(R.layout.item_list_fg);
        mListAdapter.setListItemListener(mListItemListener);
    }

    ListItemListener mListItemListener = new ListItemListener()
    {
        @Override
        public void onItemClick(String url)
        {
//            ((BigMvpActivity) getActivity()).showWebview(url);
            RxBus.getInstance().post(Constants.SHOW_WEBVIEW, url);
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
        listPresenter.loadGankList();
    }

    @Override
    public void setPresenter(ListContract.Presenter listPresenter)
    {
        this.listPresenter = Preconditions.checkNotNull(listPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState)
    {
        View contentView = inflater.inflate(R.layout.fragment_list, container, false);
        mListRv = contentView.findViewById(R.id.list_rv);
        mListPb = contentView.findViewById(R.id.list_pb);
        mListPb.setVisibility(View.VISIBLE);
        mListRv.setVisibility(View.GONE);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mListRv.setLayoutManager(manager);
        mListRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mListRv.setAdapter(mListAdapter);
        return contentView;
    }

    @Override
    public void showList(List<ListResEntity.ResultsBean> entity)
    {
        mListPb.setVisibility(View.GONE);
        mListRv.setVisibility(View.VISIBLE);
        RxBus.getInstance().post(Constants.SHOW_WEBVIEW, entity.get(0).getUrl());
//        ((BigMvpActivity) getActivity()).showWebview(entity.get(0).getUrl());
        mListAdapter.setNewData(entity);
    }

    @Override
    public void showListError(String errorMsg)
    {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    private static class ListAdapter extends BaseQuickAdapter<ListResEntity.ResultsBean, BaseViewHolder>
    {
        private ListItemListener mListItemListener;

        public ListAdapter(int layoutResId)
        {
            super(layoutResId);
        }

        private void setListItemListener(ListItemListener listener)
        {
            this.mListItemListener = listener;
        }

        @Override
        protected void convert(BaseViewHolder helper, final ListResEntity.ResultsBean item)
        {
            TextView titleTv = helper.getView(R.id.title_tv);
            titleTv.setText(item.getDesc());
            titleTv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mListItemListener.onItemClick(item.getUrl());
                }
            });
        }
    }

    public interface ListItemListener
    {
        void onItemClick(String url);
    }
}
