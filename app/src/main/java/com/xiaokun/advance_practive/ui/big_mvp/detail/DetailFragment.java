package com.xiaokun.advance_practive.ui.big_mvp.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.xiaokun.advance_practive.R;
import com.xiaokun.baselib.util.Preconditions;

/**
 * Created by 肖坤 on 2018/6/3.
 *
 * @author 肖坤
 * @date 2018/6/3
 */

public class DetailFragment extends Fragment implements DetailContract.View
{
    private static final String KEY_URL = "URL";

    private WebView mDetailWv;
    private DetailContract.Presenter mDetailPresenter;
    private ProgressBar mDetailPb;

    public DetailFragment()
    {

    }

    public static DetailFragment newInstance(String url)
    {
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View contentView = inflater.inflate(R.layout.fragment_detail, container, false);
        mDetailWv = contentView.findViewById(R.id.detail_wv);
        mDetailPb = contentView.findViewById(R.id.detail_pb);
        mDetailPb.setVisibility(View.VISIBLE);
        mDetailWv.setVisibility(View.GONE);
        mDetailPresenter.initWebview();
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        String url = arguments.getString(KEY_URL, "");
        mDetailPresenter.loadWebview(url);
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter)
    {
        mDetailPresenter = Preconditions.checkNotNull(presenter);
    }

    @Override
    public WebView getWebview()
    {
        return mDetailWv;
    }

    @Override
    public void onPageStart()
    {
        mDetailPb.setVisibility(View.VISIBLE);
        mDetailWv.setVisibility(View.GONE);
    }

    @Override
    public void onPageFinished()
    {
        mDetailPb.setVisibility(View.GONE);
        mDetailWv.setVisibility(View.VISIBLE);
    }
}
