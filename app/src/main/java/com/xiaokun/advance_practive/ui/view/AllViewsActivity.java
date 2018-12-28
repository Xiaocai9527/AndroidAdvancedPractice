package com.xiaokun.advance_practive.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xiaokun.advance_practive.R;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/27
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class AllViewsActivity extends AppCompatActivity implements View.OnClickListener {
    public Button mbutton35;

    public static void start(Context context) {
        Intent starter = new Intent(context, AllViewsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        initView();
    }

    private void initView() {
        mbutton35 = findViewById(R.id.button35);
        mbutton35.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button35:
                ScrollViewActivity.start(this);
                break;
            default:
                break;
        }
    }
}
