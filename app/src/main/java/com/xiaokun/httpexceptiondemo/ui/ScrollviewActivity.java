package com.xiaokun.httpexceptiondemo.ui;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.xiaokun.httpexceptiondemo.R;
import com.xiaokun.httpexceptiondemo.widget.ScrollTestView;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/12
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class ScrollviewActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);

        initView();
    }

    private void initView()
    {
        final int startX = 0;
        final int deltaX = 100;

        ValueAnimator animator = ValueAnimator.ofInt(0, 1).setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float fraction = animation.getAnimatedFraction();
            }
        });

        Handler handler = new Handler(getMainLooper());

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(ScrollviewActivity.this, "开始滑动", Toast.LENGTH_SHORT).show();
//                mScrollViewT.smoothScrollTo(300, 0);
            }
        }, 2000);
    }
}
