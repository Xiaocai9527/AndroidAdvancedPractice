package com.xiaokun.advance_practive.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.xiaokun.advance_practive.R;
import com.xiaokun.baselib.util.ActivityUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

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

    private FrameLayout mContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);

        initView();

        Fragment webFragment = getSupportFragmentManager().findFragmentById(R.id.content_view);
        if (webFragment == null)
        {
            webFragment = WebFragment.newInstance("https://developer.android.com/guide/components/fragments");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), webFragment, R.id.content_view);
        }

        final Fragment finalWebFragment = webFragment;
        FragmentActivity activity = finalWebFragment.getActivity();
        Observable.timer(50, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>()
        {
            @Override
            public void accept(Long aLong) throws Exception
            {
                FragmentActivity activity = finalWebFragment.getActivity();

                String s = activity.getClass().toString();
            }
        });
    }

    private void initView()
    {
        mContentView = (FrameLayout) findViewById(R.id.content_view);
    }
}
