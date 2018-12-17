package com.xiaokun.advance_practive.ui.big_mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.xiaokun.baselib.config.Constants;
import com.xiaokun.advance_practive.R;
import com.xiaokun.baselib.rx.util.RxBus;
import com.xiaokun.baselib.rx.util.RxBus2;
import com.xiaokun.advance_practive.ui.big_mvp.detail.DetailFragment;
import com.xiaokun.advance_practive.ui.big_mvp.detail.DetailPresenter;
import com.xiaokun.advance_practive.ui.big_mvp.list.ListFragment;
import com.xiaokun.advance_practive.ui.big_mvp.list.ListPresenter;
import com.xiaokun.advance_practive.ui.big_mvp.task.TasksRepository;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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
    private Observable<String> mObservableWebview;
    private Flowable<String> mRegister;
    private Disposable mDisposable;

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

        //利用RxJava来实现组件间的通信
        //主要原理是暴露出一个Observable,利用同一个Observable,上游和下游事件串联
        mObservableWebview = RxBus.getInstance().register(Constants.SHOW_WEBVIEW);
        mObservableWebview.subscribe(new Consumer<String>()
        {
            @Override
            public void accept(String url) throws Exception
            {
                mDetailPresenter.loadWebview(url);
            }
        });

        mRegister = RxBus2.getInstance().register(Constants.SHOW_WEBVIEW);
        mDisposable = mRegister.subscribe(new Consumer<String>()
        {
            @Override
            public void accept(String url) throws Exception
            {
                mDetailPresenter.loadWebview(url);
            }
        });
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

    //Activity和Fragment的通信方式
    public void showWebview(String url)
    {
        mDetailPresenter.loadWebview(url);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        RxBus.getInstance().unregister(Constants.SHOW_WEBVIEW);
        RxBus2.getInstance().unregister(Constants.SHOW_WEBVIEW);
        mDisposable.dispose();
    }
}
