package com.xiaokun.advance_practive.ui.big_mvp.detail;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static com.xiaokun.baselib.util.Preconditions.checkNotNull;

/**
 * Created by 肖坤 on 2018/6/3.
 *
 * @author 肖坤
 * @date 2018/6/3
 */

public class DetailPresenter implements DetailContract.Presenter
{
    @NonNull
    private DetailContract.View mView;

    public DetailPresenter(@NonNull DetailContract.View view)
    {
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }

    @Override
    public void subscribe()
    {

    }

    @Override
    public void initWebview()
    {
        WebSettings webSettings = mView.getWebview().getSettings();
        //允许js代码
        webSettings.setJavaScriptEnabled(true);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);
        //禁用放缩
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        //禁用文字缩放
        webSettings.setTextZoom(100);
        //10M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize(10 * 1024 * 1024);
        //允许缓存，设置缓存位置
        webSettings.setAppCacheEnabled(true);
    }

    @Override
    public void loadWebview(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            return;
        }
        WebView webview = mView.getWebview();
        webview.setWebViewClient(new WebViewClient()
        {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                mView.onPageStart();
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                mView.onPageFinished();
            }
        });
        webview.loadUrl(url);
    }

    @Override
    public void unsubscribe()
    {
    }
}
