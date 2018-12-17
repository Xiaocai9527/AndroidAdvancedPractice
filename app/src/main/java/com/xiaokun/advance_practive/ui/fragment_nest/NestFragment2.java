package com.xiaokun.advance_practive.ui.fragment_nest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;;import com.xiaokun.advance_practive.R;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/17
 *      描述  ：详情
 *      版本  ：1.0
 * </pre>
 */
public class NestFragment2 extends Fragment implements View.OnClickListener {

    private static final String KEY_URL = "URL";

    public ProgressBar mDetailPb;
    public WebView mDetailWv;

    public static NestFragment2 newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        NestFragment2 fragment = new NestFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mDetailPb = view.findViewById(R.id.detail_pb);
        mDetailWv = view.findViewById(R.id.detail_wv);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDetailWv.getSettings().setJavaScriptEnabled(true);
        Bundle arguments = getArguments();
        String url = arguments.getString(KEY_URL, "");
        loadWebview(url);
    }

    private void loadWebview(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        mDetailWv.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mDetailPb.setVisibility(View.VISIBLE);
                mDetailWv.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mDetailPb.setVisibility(View.GONE);
                mDetailWv.setVisibility(View.VISIBLE);
            }
        });
        mDetailWv.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }
}
