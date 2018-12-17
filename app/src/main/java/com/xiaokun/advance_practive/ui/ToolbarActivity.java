package com.xiaokun.advance_practive.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.xiaokun.advance_practive.R;

/**
 * @author 肖坤
 * @date 2018/07/11
 */
public class ToolbarActivity extends AppCompatActivity
{

    private Toolbar mToolbar;


    public static void start(Context context)
    {
        Intent starter = new Intent(context, ToolbarActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        initView();
    }

    private void initView()
    {
        mToolbar = findViewById(R.id.toolbar);

        mToolbar.inflateMenu(R.menu.tool_bar_menu2);

    }
}
