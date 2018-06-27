package com.xiaokun.httpexceptiondemo.ui.multi_rv_sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaokun.httpexceptiondemo.R;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/27
 *      描述  ：多item 列表写法
 *      版本  ：1.0
 * </pre>
 */
public class MultiRvActivity extends AppCompatActivity
{

    private RecyclerView mRecyvlerView;
    private LinearLayoutManager mManager;
    private int mTotalItemCount;
    private int mLastVisibleItemPosition;
    private boolean loading = false;
    private int PRE_VISIBLE = 2;
    private int pageNumber = 1;
    private PublishProcessor<Integer> paginator = PublishProcessor.create();
    private MultiAdapter mMultiAdapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static void start(Activity activity)
    {
        Intent intent = new Intent(activity, MultiRvActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_rv);
        initView();
        initData();
        setUpLoadMoreListener();
        subscribeForData();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                pageNumber = 1;
                paginator.onNext(pageNumber);
            }
        });
    }

    private void initView()
    {
        mRecyvlerView = findViewById(R.id.recyvler_view);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
    }

    private void initData()
    {
        mMultiAdapter = new MultiAdapter();
        mManager = new LinearLayoutManager(this);
        mRecyvlerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyvlerView.setLayoutManager(mManager);
        mRecyvlerView.setAdapter(mMultiAdapter);
    }

    private void setUpLoadMoreListener()
    {
        mRecyvlerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                mTotalItemCount = mManager.getItemCount();
                mLastVisibleItemPosition = mManager.findLastVisibleItemPosition();

                if (!loading && mTotalItemCount <= (mLastVisibleItemPosition + PRE_VISIBLE))
                {
                    pageNumber++;
                    //加载数据
                    paginator.onNext(pageNumber);
                    loading = true;
                }
            }
        });
    }

    private void subscribeForData()
    {
        mSwipeRefreshLayout.setRefreshing(true);
        Disposable disposable = paginator
                .onBackpressureDrop()
                .concatMap(new Function<Integer, Publisher<List<MultiItem>>>()
                {
                    @Override
                    public Publisher<List<MultiItem>> apply(Integer page) throws Exception
                    {
                        loading = true;
                        return dataFromNetword(page);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MultiItem>>()
                {
                    @Override
                    public void accept(final List<MultiItem> multiItems) throws Exception
                    {
                        if (pageNumber == 1)
                        {
                            mMultiAdapter.clear();
                        }
                        loading = false;
                        mMultiAdapter.addItems(multiItems);
                        if (mSwipeRefreshLayout.isRefreshing())
                        {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
        compositeDisposable.add(disposable);
        paginator.onNext(pageNumber);
    }

    private Flowable<List<MultiItem>> dataFromNetword(int page)
    {
        return Flowable.just(true)
                .delay(2, TimeUnit.SECONDS)
                .map(new Function<Boolean, List<MultiItem>>()
                {
                    @Override
                    public List<MultiItem> apply(Boolean aBoolean) throws Exception
                    {
                        List<MultiItem> multiItems = new ArrayList<>();

                        multiItems.add(new Item1("小米"));
                        for (int i = 0; i < 5; i++)
                        {
                            multiItems.add(new Item2("小米向港交所申请上市：估值700亿美元", "观察者网", "2018-05-03 10:51:05"));
                        }
                        multiItems.add(new Item1("Android"));
                        for (int i = 0; i < 10; i++)
                        {
                            multiItems.add(new Item2("MusicLibrary-一个丰富的音频播放SDK", "lizixian", "2018-03-12 08:44:50"));
                        }
                        return multiItems;
                    }
                }).subscribeOn(Schedulers.io());
    }

    /**
     * Diff写法老是在clear的时候报如下错误：
     * java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder
     * 有解决方法但是都不太好
     * https://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception
     * -inconsistency-detected-in
     *
     * @param multiItems
     */
    private void updateData(final List<MultiItem> multiItems)
    {
        Flowable.just(true).map(new Function<Boolean, DiffUtil.DiffResult>()
        {
            @Override
            public DiffUtil.DiffResult apply(Boolean o) throws Exception
            {
                if (pageNumber == 1)
                {
                    mMultiAdapter.clear();
                }
                List<MultiItem> oldData = new ArrayList<>();
                oldData.addAll(mMultiAdapter.getData());
                mMultiAdapter.addItems(multiItems);
                List<MultiItem> newData = mMultiAdapter.getData();

                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(oldData, newData), true);
                return diffResult;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DiffUtil.DiffResult>()
                {
                    @Override
                    public void accept(DiffUtil.DiffResult diffResult) throws Exception
                    {
                        loading = false;
                        diffResult.dispatchUpdatesTo(mMultiAdapter);

                        if (mSwipeRefreshLayout.isRefreshing())
                        {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, new Consumer<Throwable>()
                {
                    @Override
                    public void accept(Throwable throwable) throws Exception
                    {

                    }
                });
    }

}
