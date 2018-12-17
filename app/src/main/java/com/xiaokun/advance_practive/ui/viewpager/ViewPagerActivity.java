package com.xiaokun.advance_practive.ui.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.xiaokun.advance_practive.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 肖坤 on 2018/5/10.
 *
 * @author 肖坤
 * @date 2018/5/10
 */

public class ViewPagerActivity extends AppCompatActivity
{
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        initView();
        initVp();
    }

    private void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    private void initVp()
    {
        List<String> stringList = new ArrayList<>();
        stringList.add("标题1");
        stringList.add("标题2");
        stringList.add("标题3");

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new VpAdapter(getSupportFragmentManager(), stringList));
    }
}
