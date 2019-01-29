package com.xiaokun.advance_practive.ui.rxbus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.view.ProgressWebView;
import com.xiaokun.advance_practive.R;

/**
 * Created by 肖坤 on 2019/1/29.
 *
 * @author 肖坤
 * @date 2019/1/29
 */

public class ProgressWvActivity extends AppCompatActivity {

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

        progressWv.load("https://www.baidu.com/");
    }
}
