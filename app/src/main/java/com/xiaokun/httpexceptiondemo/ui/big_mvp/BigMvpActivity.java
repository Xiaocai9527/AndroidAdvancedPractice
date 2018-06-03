package com.xiaokun.httpexceptiondemo.ui.big_mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.xiaokun.httpexceptiondemo.R;
import com.xiaokun.httpexceptiondemo.ui.big_mvp.detail.DetailFragment;
import com.xiaokun.httpexceptiondemo.ui.big_mvp.detail.DetailPresenter;
import com.xiaokun.httpexceptiondemo.ui.big_mvp.list.ListFragment;
import com.xiaokun.httpexceptiondemo.ui.big_mvp.list.ListPresenter;
import com.xiaokun.httpexceptiondemo.ui.big_mvp.task.TasksRepository;

/**
 * Created by 肖坤 on 2018/6/3.
 *
 * @author 肖坤
 * @date 2018/6/3
 */

public class BigMvpActivity extends AppCompatActivity
{
    private FrameLayout mListFl;
    private FrameLayout mDetailFl;
    private ListPresenter mListPresenter;
    private DetailFragment mDetailFragment;
    private TasksRepository mTasksRepository;
    private DetailPresenter mDetailPresenter;
    private ListFragment mListFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_mvp);
        initView();
    }

    private void initView()
    {
        mListFl = (FrameLayout) findViewById(R.id.list_fl);
        mDetailFl = (FrameLayout) findViewById(R.id.detail_fl);

        mListFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.list_fl);
        mDetailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fl);
        showListFg();
        showDetailFg("");
        mDetailPresenter = new DetailPresenter(mDetailFragment);

        mTasksRepository = TasksRepository.getInstance();
        mListPresenter = new ListPresenter(mTasksRepository, mListFragment);
    }

    private void showDetailFg(String url)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mDetailFragment == null)
        {
            mDetailFragment = DetailFragment.newInstance(url);
        }
        ft.add(R.id.detail_fl, mDetailFragment).show(mDetailFragment).commit();
    }

    private void showListFg()
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mListFragment == null)
        {
            mListFragment = ListFragment.newInstance();
        }
        ft.add(R.id.list_fl, mListFragment).show(mListFragment).commit();
    }

    public void showWebview(String url)
    {
        mDetailPresenter.loadWebview(url);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }
}
