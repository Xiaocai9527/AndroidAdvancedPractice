package com.xiaokun.advance_practive.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.xiaokun.advance_practive.R;

/**
 * Created by 肖坤 on 2018/9/25.
 *
 * @author 肖坤
 * @date 2018/9/25
 */

public class H5AndroidActivity extends AppCompatActivity {


    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5_android);

        mWebView = (WebView) findViewById(R.id.webview);

    }
}
