package com.xiaokun.advance_practive.ui.rxbus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.view.ProgressWebView;
import com.xiaokun.advance_practive.R;
import com.xiaokun.baselib.util.RefInvoke;

/**
 * Created by 肖坤 on 2019/1/29.
 *
 * @author 肖坤
 * @date 2019/1/29
 */

public class ProgressWvActivity extends AppCompatActivity {

    private static final String TAG = "ProgressWvActivity";

    private ProgressWebView progressWv;

    public static void start(Context context) {
        Intent starter = new Intent(context, ProgressWvActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_wv);
        progressWv = findViewById(R.id.progress_wv);

        progressWv.loadUrl("https://www.aerwaoern234.com/");

        progressWv.getWebView().setWebViewClient(new WebViewClient() {
            @SuppressLint("AddJavascriptInterface")
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //第一种方式获取webview的高度
//                int height = (int) RefInvoke.invokeInstanceMethod(view, "computeVerticalScrollRange");
//                Log.e(TAG, "height:" + height);
                //第二种方式，利用js和android之间的相互调用
//                //第一个参数是对象，第二个参数是名字
//                WebAppInterface webAppInterface = new WebAppInterface(view);
//                webAppInterface.setMeasureListener(new HeightMeasureListener() {
//                    @Override
//                    public void invoke(Float height) {
//                        Log.e(TAG, "height:" + height);
//                    }
//                });
//                view.addJavascriptInterface(webAppInterface, "AndroidGetHeightFunction");
//                view.loadUrlUrl("javascript:AndroidGetHeightFunction.resize(document.body.scrollHeight)");
            }
        });
        progressWv.getWebViewClient();
    }


    public class WebAppInterface {
        private WebView mWebview;
        private HeightMeasureListener measureListener;

        public WebAppInterface(WebView webView) {
            mWebview = webView;
        }

        public void setMeasureListener(HeightMeasureListener measureListener) {
            this.measureListener = measureListener;
        }

        @JavascriptInterface
        public void resize(Float height) {
            mWebview.post(new Runnable() {
                @Override
                public void run() {
                    measureListener.invoke(height);
                }
            });
        }

    }

    interface HeightMeasureListener {

        void invoke(Float height);
    }

}
