package com.xiaokun.advance_practive.ui.rxjava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.xiaokun.advance_practive.R;
import com.xiaokun.baselib.network.OkhttpHelper;
import com.xiaokun.baselib.network.RetrofitHelper;
import com.xiaokun.advance_practive.network.api.ApiService;
import com.xiaokun.advance_practive.network.meizi.CategoryResEntity;
import com.xiaokun.advance_practive.ui.adapter.NightModeAdapter;
import com.xiaokun.baselib.util.OffsetDecoration;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

/**
 * Created by 肖坤 on 2018/7/8.
 *
 * @author 肖坤
 * @date 2018/7/8
 */

public class MergeArrayActivity extends AppCompatActivity
{

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;

    public static void start(Context context)
    {
        Intent starter = new Intent(context, MergeArrayActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        setTheme(R.style.DayTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_array);

        initView();
    }

    private void initView()
    {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRefreshLayout.setRefreshing(true);

        final int spacing = getResources().getDimensionPixelSize(R.dimen.dimen_2dp);
        mRecyclerView.addItemDecoration(new OffsetDecoration(spacing));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        final NightModeAdapter nightModeAdapter = new NightModeAdapter(R.layout.item_other);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(nightModeAdapter);

        OkHttpClient client = OkhttpHelper.getDefaultClient();
        ApiService service = RetrofitHelper.getInstance().createService(ApiService.class,
                RetrofitHelper.getInstance().getRetrofit(client, ApiService.baseUrl2));

        List<Observable<CategoryResEntity>> datas = new ArrayList<>();
        Observable<CategoryResEntity>[] observables = new Observable[6];
        for (int i = 1; i < 7; i++)
        {
            Observable<CategoryResEntity> data = service
                    .getCategoryData("Android", 10, i)
                    .subscribeOn(Schedulers.newThread());
            datas.add(data);
            observables[i - 1] = data;
        }

//        Observable<CategoryResEntity>[] observables = (Observable<CategoryResEntity>[]) datas.toArray();
        //rxjava并行处理
        Observable.mergeArray(observables)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CategoryResEntity>()
                {
                    @Override
                    public void accept(CategoryResEntity categoryResEntity) throws Exception
                    {
                        if (mRefreshLayout.isRefreshing())
                        {
                            mRefreshLayout.setRefreshing(false);
                        }
                        nightModeAdapter.addData(categoryResEntity.getResults());
                    }
                }, new Consumer<Throwable>()
                {
                    @Override
                    public void accept(Throwable throwable) throws Exception
                    {
                        Toast.makeText(MergeArrayActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

//        Observable.mergeArray(service.getCategoryData("Android", 10, 1),
//                service.getCategoryData("Android", 10, 2),
//                service.getCategoryData("Android", 10, 3),
//                service.getCategoryData("Android", 10, 4),
//                service.getCategoryData("Android", 10, 5),
//                service.getCategoryData("Android", 10, 6))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<CategoryResEntity>()
//                {
//                    @Override
//                    public void accept(CategoryResEntity categoryResEntity) throws Exception
//                    {
//                        if (mRefreshLayout.isRefreshing())
//                        {
//                            mRefreshLayout.setRefreshing(false);
//                        }
//                        nightModeAdapter.addData(categoryResEntity.getResults());
//                    }
//                }, new Consumer<Throwable>()
//                {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception
//                    {
//                        Toast.makeText(MergeArrayActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }
}
