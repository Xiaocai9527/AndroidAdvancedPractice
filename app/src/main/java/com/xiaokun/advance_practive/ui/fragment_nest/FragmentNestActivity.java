package com.xiaokun.advance_practive.ui.fragment_nest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.xiaokun.advance_practive.R;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/17
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class FragmentNestActivity extends AppCompatActivity implements View.OnClickListener {
    public FrameLayout mContainerFl;

    public static void start(Context context) {
        Intent starter = new Intent(context, FragmentNestActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_nest);
        initView();
    }

    private void initView() {
        mContainerFl = findViewById(R.id.container_fl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }
}
