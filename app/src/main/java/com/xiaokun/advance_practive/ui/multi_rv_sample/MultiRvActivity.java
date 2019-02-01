package com.xiaokun.advance_practive.ui.multi_rv_sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaokun.advance_practive.App;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemA;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemB;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemC;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemD;
import com.xiaokun.advance_practive.ui.multi_rv_sample.entity.ItemE;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.CustomFooterHolder;
import com.xiaokun.baselib.muti_rv.MultiItem;
import com.xiaokun.baselib.muti_rv.BaseMultiHodler;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.TypeAHolder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.TypeBHolder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.TypeCHolder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.TypeDHolder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.TypeEHolder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.utils.DiffCallback;
import com.xiaokun.baselib.muti_rv.HolderFactory;
import com.xiaokun.baselib.muti_rv.HolderFactoryList;
import com.xiaokun.baselib.muti_rv.MultiAdapter;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.HashMap;
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
 *              参考：https://medium.com/@ruut_j/a-recyclerview-with-multiple-item-types-bce7fbd1d30e
 *                    https://proandroiddev.com/writing-better-adapters-1b09758407d2
 *                    https://medium.com/mindorks/implement-pagination-in-recyclerview-using-rxjava-operators
 *                    -686fb202b9dc
 *      版本  ：1.0
 * </pre>
 */
public class MultiRvActivity extends AppCompatActivity {
    private static final String TAG = "MultiRvActivity";
    private RecyclerView mRecyvlerView;
    private GridLayoutManager mManager;
    private int pageNumber = 1;
    private PublishProcessor<Integer> paginator = PublishProcessor.create();
    private MultiAdapter mMultiAdapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mRefreshComTv;
    private boolean firstFlag = false;
    private HolderFactory mHolderFactory;
    private EndlessRecyclerViewScrollListener scrollListener;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MultiRvActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_rv);
        initView();
        initData();
        subscribeForData();
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            pageNumber = 1;
            paginator.onNext(pageNumber);
            scrollListener.resetState();
        });
    }

    private void initView() {
        mRecyvlerView = findViewById(R.id.recyvler_view);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mRefreshComTv = findViewById(R.id.refresh_com_tv);
    }

    private void initData() {
        mHolderFactory = HolderFactoryList.getInstance();
        HashMap<Integer, Class<? extends BaseMultiHodler>> hashMap = new HashMap<>();
        hashMap.put(TypeAHolder.LAYOUT, TypeAHolder.class);
        hashMap.put(TypeBHolder.LAYOUT, TypeBHolder.class);
        hashMap.put(TypeCHolder.LAYOUT, TypeCHolder.class);
        hashMap.put(TypeDHolder.LAYOUT, TypeDHolder.class);
        hashMap.put(TypeEHolder.LAYOUT, TypeEHolder.class);
        ((HolderFactoryList) mHolderFactory).addTypeHolders(hashMap);
        mMultiAdapter = new MultiAdapter(mHolderFactory);
        mMultiAdapter.isShowFooterView(true);
        mMultiAdapter.setLoadFailedClickListener(() -> paginator.onNext(pageNumber));

        mManager = new GridLayoutManager(this, 2);
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int size = mMultiAdapter.getData().size();
                MultiItem multiItem = null;
                if (position < size) {
                    multiItem = mMultiAdapter.getData().get(position);
                }

                return multiItem instanceof ItemE ? 1 : mManager.getSpanCount();
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(mManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                pageNumber = page;
                paginator.onNext(pageNumber);
            }
        };
        mRecyvlerView.addOnScrollListener(scrollListener);

        mRecyvlerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyvlerView.setLayoutManager(mManager);

        View item = LayoutInflater.from(this).inflate(R.layout.footer_layout, mRecyvlerView, false);
        mMultiAdapter.setFooterHolder(new CustomFooterHolder(item, new CustomFooterHolder.FailedClickListener() {
            @Override
            public void onFailedClick() {
                paginator.onNext(pageNumber);
                mMultiAdapter.loading();
            }
        }));
        mRecyvlerView.setAdapter(mMultiAdapter);
    }

    private void subscribeForData() {
        mSwipeRefreshLayout.setRefreshing(true);
        Disposable disposable = paginator
                //如果消费者无法处理数据，则 onBackpressureDrop 就把该数据丢弃了。
                //Read more: http://blog.chengyunfeng.com/?p=981#ixzz5Jd08zeDx
                .onBackpressureDrop()
                .concatMap(new Function<Integer, Publisher<List<MultiItem>>>() {
                    @Override
                    public Publisher<List<MultiItem>> apply(Integer page) throws Exception {
                        return dataFromNetword(page);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MultiItem>>() {
                    @Override
                    public void accept(final List<MultiItem> multiItems) throws Exception {
                        if (pageNumber == 1) {
                            mMultiAdapter.clear();
                            refreshTvAnim();
                        }
                        mMultiAdapter.addItems(multiItems);
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //注意当发生onError时,onNext就不在起作用了。这个时候需要重新subscribe
                        Toast.makeText(App.getAppContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
        paginator.onNext(pageNumber);
    }

    private Flowable<List<MultiItem>> dataFromNetword(final int page) {
        mMultiAdapter.loading();
        return Flowable.just(true)
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(aBoolean -> {
                    List<MultiItem> multiItems = new ArrayList<>();

                    multiItems.add(new ItemA("小米"));
                    for (int i = 0; i < 2; i++) {
                        multiItems.add(new ItemB("小米向港交所申请上市：估值700亿美元", "观察者网", "2018-05-03 10:51:05"));
                    }
                    multiItems.add(new ItemA("Android"));
                    for (int i = 0; i < 2; i++) {
                        multiItems.add(new ItemB("MusicLibrary-一个丰富的音频播放SDK", "lizixian", "2018-03-12 08:44:50"));
                    }
                    multiItems.add(new ItemA("一张图片"));
                    multiItems.add(new ItemC("Android 仿丁丁、微信 群聊组合头像", "作者：艾米", "http://ww1.sinaimg" +
                            ".cn/large/0065oQSqly1fsq9iq8ttrj30k80q9wi4.jpg"));
                    multiItems.add(new ItemC("Android新福利：调试神奇Pandora了解下哇", "作者：whatta", "http://ww1.sinaimg" +
                            ".cn/large/0065oQSqly1frsllc19gfj30k80tfah5.jpg"));
                    multiItems.add(new ItemA("三张图片"));
                    multiItems.add(new ItemD("http://ww1.sinaimg.cn/large/0065oQSqly1fsp4iok6o4j30j60optbl" +
                            ".jpg", "http://ww1.sinaimg.cn/large/0065oQSqly1fsoe3k2gkkj30g50niwla.jpg",
                            "http://ww1.sinaimg.cn/large/0065oQSqly1fsmis4zbe7j30sg16fq9o.jpg"));
                    multiItems.add(new ItemD("http://ww1.sinaimg.cn/large/0065oQSqly1fsb0lh7vl0j30go0ligni.jpg",
                            "http://ww1.sinaimg.cn/large/0065oQSqly1fsfq2pwt72j30qo0yg78u.jpg",
                            "http://ww1.sinaimg.cn/large/0065oQSqly1fsfq1ykabxj30k00pracv.jpg"));
                    multiItems.add(new ItemA("两张图片,一行两个item"));
                    multiItems.add(new ItemE("http://ww1.sinaimg.cn/large/0065oQSqly1fsb0lh7vl0j30go0ligni.jpg"));
                    multiItems.add(new ItemE("http://ww1.sinaimg.cn/large/0065oQSqly1fsfq2pwt72j30qo0yg78u.jpg"));

                    if (page == 2 && !firstFlag) {
                        multiItems.clear();
                        mMultiAdapter.loadFailed();
                        firstFlag = true;
                    }

                    if (page == 3) {
                        multiItems.clear();
                        mMultiAdapter.loadComplete();
                    }
                    return multiItems;
                }).subscribeOn(Schedulers.io());
    }

    /**
     * 刷新完成动画
     */
    private void refreshTvAnim() {
        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mRefreshComTv.getLayoutParams();

        //展开动画
        ValueAnimator animator = ValueAnimator.ofInt(0, 105).setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                layoutParams.height = animatedValue;
                mRefreshComTv.setLayoutParams(layoutParams);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Flowable.just(1).delay(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                //收缩动画
                                ValueAnimator shrinkAnim = ValueAnimator.ofInt(105, 0).setDuration(500);
                                shrinkAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        int animatedValue = (int) animation.getAnimatedValue();
                                        layoutParams.height = animatedValue;
                                        mRefreshComTv.setLayoutParams(layoutParams);
                                    }
                                });
                                shrinkAnim.start();
                            }
                        });
            }
        });
        animator.start();
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
    private void updateData(final List<MultiItem> multiItems) {
        Flowable.just(true).map(new Function<Boolean, DiffUtil.DiffResult>() {
            @Override
            public DiffUtil.DiffResult apply(Boolean o) throws Exception {
                if (pageNumber == 1) {
                    mMultiAdapter.clear();
                }
                List<MultiItem> oldData = new ArrayList<>();
                oldData.addAll(mMultiAdapter.getData());
                mMultiAdapter.addItems(multiItems);
                List<MultiItem> newData = mMultiAdapter.getData();

                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(oldData, newData,
                        mHolderFactory), true);
                return diffResult;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DiffUtil.DiffResult>() {
                    @Override
                    public void accept(DiffUtil.DiffResult diffResult) throws Exception {
                        diffResult.dispatchUpdatesTo(mMultiAdapter);

                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
    }

}
