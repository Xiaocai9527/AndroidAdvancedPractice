package com.xiaokun.httpexceptiondemo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xiaokun.httpexceptiondemo.R;
import com.xiaokun.httpexceptiondemo.ui.mvp.MvpMainActivity;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/24
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener
{

    private Button mButton10;
    private Button mButton11;
    private Button mButton12;
    private Button mButton14;
    private Button mButton15;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;

        initView();
    }

    private void initView()
    {
        mButton10 = (Button) findViewById(R.id.button10);
        mButton11 = (Button) findViewById(R.id.button11);
        mButton12 = (Button) findViewById(R.id.button12);
        mButton14 = (Button) findViewById(R.id.button14);
        mButton15 = (Button) findViewById(R.id.button15);
        initListener(mButton10, mButton11, mButton12, mButton14, mButton15);
    }

    private void initListener(View... views)
    {
        for (View view : views)
        {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button10:
                startActivity(new Intent(mContext, MainActivity.class));
                break;
            case R.id.button11:
                startActivity(new Intent(mContext, MvpMainActivity.class));
                break;
            case R.id.button12:
                startActivity(new Intent(mContext, FlatMap1Activity.class));
                break;
            case R.id.button14:
                startActivity(new Intent(mContext, FlatMap2Activity.class));
                break;
            case R.id.button15:
                startActivity(new Intent(mContext, NightModeActivity.class));
                break;
            default:
                break;
        }
    }

}
