package com.xiaokun.httpexceptiondemo.ui.mvp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.xiaokun.httpexceptiondemo.R;
import com.xiaokun.httpexceptiondemo.network.entity.UniversalResEntity;
import com.xiaokun.baselib.rx.util.RxManager;
import com.xiaokun.httpexceptiondemo.ui.adapter.UniversalRvAdapter;
import com.xiaokun.baselib.util.OffsetDecoration;

import java.util.List;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/03
 *     描述   : 数据流符合UniveralResEntity的通用页面
 *     版本   : 1.0
 * </pre>
 */
public class UniversalActivity extends AppCompatActivity implements UniversalView
{

    private RecyclerView mUniversalRv;
    private UniversalRvAdapter mAdapter;
    private RxManager rxManager;
    private MainPresenter mainPresenter;

    private static final String KEY_PAGE_TYPE = "PAGE_TYPE";
    //1表示请求gank-Android文章 2表示请求小米新闻
    private int pageType;

    public static void startUniversalActivity(Activity activity, int pageType)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PAGE_TYPE, pageType);
        Intent intent = new Intent(activity, UniversalActivity.class);
        intent.putExtras(bundle);
        ActivityCompat.startActivity(activity, intent, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        setTheme(R.style.DayTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank);

        initData();
        initView();
        initRv();
        getHttpData();
    }

    private void initData()
    {
        Bundle bundle = getIntent().getExtras();
        pageType = bundle.getInt(KEY_PAGE_TYPE, 0);
    }

    private void initView()
    {
        mUniversalRv = (RecyclerView) findViewById(R.id.universal_rv);
        rxManager = new RxManager();
        mainPresenter = new MainPresenter(this, rxManager);
    }

    private void initRv()
    {
        final int spacing = getResources().getDimensionPixelSize(R.dimen.dimen_2dp);
        mUniversalRv.addItemDecoration(new OffsetDecoration(spacing));

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mAdapter = new UniversalRvAdapter(R.layout.item_other);
        mUniversalRv.setLayoutManager(manager);
        mUniversalRv.setAdapter(mAdapter);
    }

    private void getHttpData()
    {
        switch (pageType)
        {
            case 1:
                mainPresenter.getGankData();
                break;
            case 2:
                mainPresenter.getXmNewsData();
                break;
            default:

                break;
        }
    }

    @Override
    public void getUniversalSuc(List<UniversalResEntity> entity)
    {
        mAdapter.setNewData(entity);
    }

    @Override
    public void getUniversalFailed(String errorMsg)
    {
        Toast.makeText(UniversalActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
