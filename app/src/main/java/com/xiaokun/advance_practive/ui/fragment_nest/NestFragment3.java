package com.xiaokun.advance_practive.ui.fragment_nest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.network.api.ApiService;
import com.xiaokun.advance_practive.network.entity.ListResEntity;
import com.xiaokun.baselib.config.Constants;
import com.xiaokun.baselib.network.OkhttpHelper;
import com.xiaokun.baselib.network.RetrofitHelper;
import com.xiaokun.baselib.rx.transform.RxSchedulers;
import com.xiaokun.baselib.rx.util.RxBus3;

;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/17
 *      描述  ：list列表
 *      版本  ：1.0
 * </pre>
 */
public class NestFragment3 extends Fragment implements View.OnClickListener {
    public ProgressBar mListPb;
    public RecyclerView mListRv;
    private ListAdapter mListAdapter;

    public static NestFragment3 newInstance() {
        Bundle args = new Bundle();
        NestFragment3 fragment = new NestFragment3();
        fragment.setArguments(args);
        return fragment;
    }

    ListItemListener mListItemListener = url -> RxBus3.getInstance().post(Constants.SHOW_WEBVIEW, url);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new ListAdapter(R.layout.item_list_fg);
        mListAdapter.setListItemListener(mListItemListener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initView(view);
        loadData();
        return view;
    }

    private void initView(View view) {
        mListPb = view.findViewById(R.id.list_pb);
        mListRv = view.findViewById(R.id.list_rv);
        mListPb.setVisibility(View.VISIBLE);
        mListRv.setVisibility(View.GONE);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mListRv.setLayoutManager(manager);
        mListRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mListRv.setAdapter(mListAdapter);
    }

    private void loadData() {
        ApiService service = RetrofitHelper.getInstance().createService(ApiService.class,
                RetrofitHelper.getInstance().getRetrofit(OkhttpHelper.getDefaultClient(), ApiService.baseUrl2));
        service.loadListData("Android", 20, 1)
                .compose(RxSchedulers.io_main())
                .subscribe(listResEntity -> {
                    mListPb.setVisibility(View.GONE);
                    mListRv.setVisibility(View.VISIBLE);
                    mListAdapter.setNewData(listResEntity.getResults());
                }, throwable -> {
                    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private static class ListAdapter extends BaseQuickAdapter<ListResEntity.ResultsBean, BaseViewHolder> {
        private ListItemListener mListItemListener;

        public ListAdapter(int layoutResId) {
            super(layoutResId);
        }

        private void setListItemListener(ListItemListener listener) {
            this.mListItemListener = listener;
        }

        @Override
        protected void convert(BaseViewHolder helper, final ListResEntity.ResultsBean item) {
            TextView titleTv = helper.getView(R.id.title_tv);
            titleTv.setText(item.getDesc());
            titleTv.setOnClickListener(v -> mListItemListener.onItemClick(item.getUrl()));
        }
    }

    private static final String TAG = "NestFragment1";

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")");
    }

    public interface ListItemListener {
        void onItemClick(String url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }
}
