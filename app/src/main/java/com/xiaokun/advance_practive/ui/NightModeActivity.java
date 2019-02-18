package com.xiaokun.advance_practive.ui;

import android.animation.Animator;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jaeger.library.StatusBarUtil;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.colorful.Colorful;
import com.xiaokun.advance_practive.colorful.setter.ViewGroupSetter;
import com.xiaokun.advance_practive.ui.adapter.NightModeHolder;
import com.xiaokun.advance_practive.ui.multi_rv_sample.EndlessRecyclerViewScrollListener;
import com.xiaokun.baselib.muti_rv.BaseMultiHodler;
import com.xiaokun.baselib.muti_rv.HolderFactoryList;
import com.xiaokun.baselib.muti_rv.MultiAdapter;
import com.xiaokun.baselib.muti_rv.MultiItem;
import com.xiaokun.baselib.muti_rv.MultiUtils;
import com.xiaokun.baselib.network.OkhttpHelper;
import com.xiaokun.baselib.network.RetrofitHelper;
import com.xiaokun.advance_practive.network.api.ApiService;
import com.xiaokun.advance_practive.network.meizi.CategoryResEntity;
import com.xiaokun.baselib.rx.BaseObserver;
import com.xiaokun.baselib.rx.transform.RxSchedulers;
import com.xiaokun.baselib.rx.util.RxManager;
import com.xiaokun.advance_practive.ui.adapter.NightModeAdapter;
import com.xiaokun.baselib.util.OffsetDecoration;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by 肖坤 on 2018/4/30.
 *
 * @author 肖坤
 * @date 2018/4/30
 */

public class NightModeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mNightRv;
    //    private NightModeAdapter mAdapter;
    private RxManager rxManager;
    private Colorful colorful;
    private SharedPreferences mPref;
    public static final String NIGHT = "night";
    public static final String NIGHT_MODE = "data_day_night_mode";
    private MultiAdapter mMultiAdapter;
    private ApiService mService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night_mode);
        rxManager = new RxManager();
        initView();
        initRv();
        setupColorful();
        OkHttpClient client = OkhttpHelper.getDefaultClient();
        mService = RetrofitHelper.getInstance().createService(ApiService.class,
                RetrofitHelper.getInstance().getRetrofit(client, ApiService.baseUrl2));
        getHttpData(pageNum);
    }

    private void initTheme() {
        mPref = getSharedPreferences(NIGHT_MODE, MODE_PRIVATE);
        boolean flag = mPref.getBoolean(NIGHT, false);
        if (!flag) {
            setTheme(R.style.DayTheme);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.day_toolbar_bg), 0);
        } else {
            setTheme(R.style.NightTheme);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.night_toolbar_bg), 0);
        }
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNightRv = (RecyclerView) findViewById(R.id.night_rv);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setContentInsetStartWithNavigation(0);
        mToolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.more));
        mToolbar.inflateMenu(R.menu.menu_gank);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.switch_theme_mode:
                        switchMode();
                        break;
                }
                return false;
            }
        });
    }

    private void switchMode() {
        if (!mPref.getBoolean(NIGHT, false)) {
            animChangeColor(R.style.NightTheme);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.night_toolbar_bg), 0);
            mPref.edit().putBoolean(NIGHT, true).commit();
        } else {
            animChangeColor(R.style.DayTheme);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.day_toolbar_bg), 0);
            mPref.edit().putBoolean(NIGHT, false).commit();
        }
    }

    private void initRv() {
        final int spacing = getResources().getDimensionPixelSize(R.dimen.dimen_2dp);
        mNightRv.addItemDecoration(new OffsetDecoration(spacing));

        LinearLayoutManager manager = new LinearLayoutManager(this);

//        mAdapter = new NightModeAdapter(R.layout.item_other);
        mNightRv.setLayoutManager(manager);


        HolderFactoryList instance = HolderFactoryList.getInstance();
        new BaseMultiHodler<CategoryResEntity.ResultsBean>(LayoutInflater.from(this).inflate(NightModeHolder.TYPE_FOOTER, mNightRv, false)) {

            @Override
            public void bind(CategoryResEntity.ResultsBean multiItem) {

            }
        };
        instance.addTypeHolder(NightModeHolder.class, R.layout.item_other);
        mMultiAdapter = new MultiAdapter(instance);
        mMultiAdapter.isShowFooterView(false);
        mNightRv.setAdapter(mMultiAdapter);
        mNightRv.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                pageNum = page;
                getHttpData(pageNum);
            }
        });
    }

    private void setupColorful() {
        ViewGroupSetter toolbarSetter = new ViewGroupSetter(mToolbar, R.attr.toolbar_bg);
        ViewGroupSetter rvSetter = new ViewGroupSetter(mNightRv, R.attr.root_view_bg);
        rvSetter.childViewTextColor(R.id.category_desc, R.attr.one_text_bg);
        rvSetter.childViewTextColor(R.id.category_author, R.attr.two_text_bg);
        rvSetter.childViewTextColor(R.id.category_date, R.attr.two_text_bg);
        rvSetter.childViewBgColor(R.id.night_rl, R.attr.cardview_bg);

        colorful = new Colorful.Builder(this)
                .setter(toolbarSetter)
                .setter(rvSetter)
                .create();

    }

    private int pageNum = 1;

    private void getHttpData(int pageNum) {
        mService.getCategoryData("Android", 20, pageNum)
                .compose(RxSchedulers.<CategoryResEntity>io_main()).subscribe(new BaseObserver<CategoryResEntity>(rxManager) {
            @Override
            public void onErrorMsg(String msg) {

            }

            @Override
            public void onNext(CategoryResEntity categoryResEntity) {
                //mAdapter.setNewData(categoryResEntity.getResults());
                mMultiAdapter.addItems(MultiUtils.transforToMulItem(categoryResEntity.getResults()));
            }
        });
    }

    /**
     * 给夜间模式增加一个动画,颜色渐变
     *
     * @param newTheme
     */
    private void animChangeColor(final int newTheme) {
        final View rootView = getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache(true);

        final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        if (null != localBitmap && rootView instanceof ViewGroup) {
            final View tmpView = new View(this);
            tmpView.setBackgroundDrawable(new BitmapDrawable(getResources(), localBitmap));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                    .LayoutParams.MATCH_PARENT);
            ((ViewGroup) rootView).addView(tmpView, params);
            tmpView.animate().alpha(0).setDuration(400).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    colorful.setTheme(newTheme);
                    System.gc();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ((ViewGroup) rootView).removeView(tmpView);
                    localBitmap.recycle();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }
    }

}
