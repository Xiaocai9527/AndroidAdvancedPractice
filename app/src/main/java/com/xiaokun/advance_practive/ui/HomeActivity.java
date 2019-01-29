package com.xiaokun.advance_practive.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.ui.big_mvp.BigMvpActivity;
import com.xiaokun.advance_practive.ui.fragment_nest.FragmentNestActivity;
import com.xiaokun.advance_practive.ui.multi_rv_sample.MultiRvActivity;
import com.xiaokun.advance_practive.ui.mvp.MvpMainActivity;
import com.xiaokun.advance_practive.ui.rxbus.ScrollviewActivity;
import com.xiaokun.advance_practive.ui.rxjava.MergeArrayActivity;
import com.xiaokun.advance_practive.ui.rxjava.RxjavaActivity;
import com.xiaokun.advance_practive.ui.view.AllViewsActivity;
import com.xiaokun.wanandroid.WanLoginActivity;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/24
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButton10;
    private Button mButton11;
    private Button mButton12;
    private Button mButton14;
    private Button mButton15;
    private Button mButton19;
    private Button mButton20;
    private Button mButton21;
    private Button mButton22;
    private Button mButton23;
    private Button mButton24;
    private Button mButton25;
    private Button mButton26;
    private Button mButton30;
    private Button mButton31;
    private Button mButton32;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;

        initView();
    }

    private void initView() {
        mButton10 = findViewById(R.id.button10);
        mButton11 = findViewById(R.id.button11);
        mButton12 = findViewById(R.id.button12);
        mButton14 = findViewById(R.id.button14);
        mButton15 = findViewById(R.id.button15);
        mButton19 = findViewById(R.id.button19);
        mButton20 = findViewById(R.id.button20);
        mButton21 = findViewById(R.id.button21);
        mButton22 = findViewById(R.id.button22);
        mButton23 = findViewById(R.id.button23);
        mButton24 = findViewById(R.id.button24);
        mButton25 = findViewById(R.id.button25);
        mButton26 = findViewById(R.id.button26);
        mButton30 = findViewById(R.id.button30);
        mButton31 = findViewById(R.id.button31);
        mButton32 = findViewById(R.id.button32);


        initListener(mButton10, mButton11, mButton12, mButton14, mButton15, mButton19, mButton20,
                mButton21, mButton22, mButton23, mButton24, mButton25, mButton26, mButton30, mButton31
                , mButton32);
    }

    private void initListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.button19:
                startActivity(new Intent(mContext, BigMvpActivity.class));
                break;
            case R.id.button20:
                startActivity(new Intent(mContext, ScrollviewActivity.class));
                break;
            case R.id.button21:
                startActivity(new Intent(mContext, PermissionTestActivity.class));
                break;
            case R.id.button22:
                MultiRvActivity.start(this);
                break;
            case R.id.button23:
                MergeArrayActivity.start(this);
                break;
            case R.id.button24:
                ToolbarActivity.start(this);
                break;
            case R.id.button25:
                StateListAnimatorActivity.start(this);
                break;
            case R.id.button26:
                RxjavaActivity.start(this);
                break;
            case R.id.button30:
                FragmentNestActivity.start(this);
                break;
            case R.id.button31:
                WanLoginActivity.start(this);
                break;
            case R.id.button32:
                AllViewsActivity.start(this);
                break;
            default:
                break;
        }
    }

}
