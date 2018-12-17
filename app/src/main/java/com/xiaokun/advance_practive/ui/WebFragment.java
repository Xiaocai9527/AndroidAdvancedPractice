package com.xiaokun.advance_practive.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.xiaokun.advance_practive.R;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/15
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class WebFragment extends Fragment
{

    private static final String KEY_URL = "URL";
    private WebView mWebview;

    public static WebFragment newInstance(String url)
    {
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState)
    {
        View inflate = inflater.inflate(R.layout.fragment_webview, container, false);
        mWebview = (WebView) inflate.findViewById(R.id.webview);
        mWebview.getSettings().setJavaScriptEnabled(true);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        String url = arguments.getString(KEY_URL);
        mWebview.loadUrl(url);
    }
}
