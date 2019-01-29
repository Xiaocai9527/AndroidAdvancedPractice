package com.example.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * Created by 肖坤 on 2019/1/29.
 *
 * @author 肖坤
 * @date 2019/1/29
 */

public class ProgressWebView extends FrameLayout {

    private Context mContext;
    private int DEFAULT_HEIGHT = dpToPxInt(2);
    private float mProgressBarHeight;
    private int mProgressBarColor;
    private WebView mWebView;
    private ProgressBar mProgressBar;

    public ProgressWebView(@NonNull Context context) {
        this(context, null);
    }

    public ProgressWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressWebView);
            if (array != null) {
                mProgressBarHeight = array.getDimension(R.styleable.ProgressWebView_progressbar_height, DEFAULT_HEIGHT);
                mProgressBarColor = array.getColor(R.styleable.ProgressWebView_progressbar_color,
                        ContextCompat.getColor(mContext, R.color.color_ff4081));
                array.recycle();
            }
        }
        LayoutInflater.from(context).inflate(R.layout.progress_webview, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mWebView = findViewById(R.id.progress_webview);
        mProgressBar = findViewById(R.id.progress_bar);
        //设置bar color
        mProgressBar.getIndeterminateDrawable().setColorFilter(mProgressBarColor, PorterDuff.Mode.SRC_IN);
        mProgressBar.getProgressDrawable().setColorFilter(mProgressBarColor, PorterDuff.Mode.SRC_IN);
        initWvDefault();
    }

    private void initWvDefault() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                if (progress < 100 && mProgressBar.getVisibility() == ProgressBar.GONE) {
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }

                mProgressBar.setProgress(progress);
                if (progress == 100) {
                    mProgressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });
    }

    /**
     * 如果有对webview更多的设置,调用此方法进行设置
     *
     * @return
     */
    public WebView getWebView() {
        return mWebView;
    }

    public void load(String url) {
        if (!TextUtils.isEmpty(url) && mWebView != null) {
            mWebView.loadUrl(url);
        }
    }

    private float dpToPx(float dp) {
        if (mContext != null) {
            return dp * mContext.getResources().getDisplayMetrics().density;
        }
        return 0;
    }

    private int dpToPxInt(float dp) {
        if (mContext != null) {
            return (int) (dpToPx(dp) + 0.5f);
        }
        return 0;
    }

}
