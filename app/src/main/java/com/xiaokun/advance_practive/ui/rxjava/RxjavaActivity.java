package com.xiaokun.advance_practive.ui.rxjava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xiaokun.advance_practive.App;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.network.LoginEntity;
import com.xiaokun.advance_practive.network.RegisterEntity;
import com.xiaokun.advance_practive.network.api.ApiService;
import com.xiaokun.advance_practive.network.entity.GankResEntity;
import com.xiaokun.baselib.network.BaseResponse;
import com.xiaokun.baselib.network.OkhttpHelper;
import com.xiaokun.baselib.network.RetrofitHelper;
import com.xiaokun.baselib.rx.ErrorConsumer;
import com.xiaokun.baselib.rx.transform.HttpResultFunc;
import com.xiaokun.baselib.rx.transform.RxSchedulers;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.Retrofit;

import static com.xiaokun.baselib.config.Constants.GANK_DATA;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/10/25
 *      描述  ：rxjava常见应用场景
 *      版本  ：1.0
 * </pre>
 */
public class RxjavaActivity extends AppCompatActivity implements View.OnClickListener {


    private Button mButton27;
    private Button mButton28;
    private Button mButton29;
    private NetworkRepository mNetworkRepository;
    private CacheRepository mCacheRepository;
    private ApiService mService;
    private RealGankApiService mRealGankApiService;

    public static void start(Context context) {
        Intent starter = new Intent(context, RxjavaActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rxjava);
        initView();
    }

    private void initView() {
        mButton27 = findViewById(R.id.button27);
        mButton28 = findViewById(R.id.button28);
        mButton29 = findViewById(R.id.button29);

        initListener(mButton27, mButton28, mButton29);

        mNetworkRepository = new NetworkRepository(RetrofitHelper.getInstance()
                .createService(FakeGankApiService.class));
        mCacheRepository = new CacheRepository();
        mService = RetrofitHelper.getInstance().createService(ApiService.class);

        RetrofitHelper.getInstance().createService(FakeGankApiService.class);

        Retrofit retrofit = RetrofitHelper.getInstance()
                .getRetrofit(OkhttpHelper.getDefaultClient(), "http://gank.io/api/");

        mRealGankApiService = RetrofitHelper.getInstance()
                .createService(RealGankApiService.class, retrofit);
    }

    private void initListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button27:
                getGankData();
                break;
            case R.id.button28:
                registerAndLogin();
                break;
            case R.id.button29:
                rxjavaZip();
                break;
            default:
                break;
        }
    }

    /**
     * 获取数据源的判定,这里是优先从cache里面获取数据
     */
    private void getGankData() {
        Single<List<GankResEntity.DataBean>> networkRepositoryGankData = mNetworkRepository.getGankData();
        Maybe<List<GankResEntity.DataBean>> cacheRepositoryGankData = mCacheRepository.getGankData();
        Maybe<List<GankResEntity.DataBean>> listMaybe = Maybe.concat(cacheRepositoryGankData, networkRepositoryGankData.toMaybe()).firstElement();

        listMaybe.subscribe(new Consumer<List<GankResEntity.DataBean>>() {
            @Override
            public void accept(List<GankResEntity.DataBean> dataBeans) throws Exception {
                Toast.makeText(RxjavaActivity.this, "成功", Toast.LENGTH_SHORT).show();
                App.getCache().put(GANK_DATA, (Serializable) dataBeans);
            }
        }, new ErrorConsumer() {
            @Override
            public void onErrorMsg(String errorMsg) {

            }
        });
    }

    /**
     * 第二个请求依赖第一个请求的结果
     */
    private void registerAndLogin() {
        mService.register()
                //将注册成功后将注册的生产环境转换成登录的生产环境
                .flatMap(new Function<BaseResponse<RegisterEntity.DataBean>, ObservableSource<BaseResponse<LoginEntity.DataBean>>>() {
                    @Override
                    public ObservableSource<BaseResponse<LoginEntity.DataBean>> apply(BaseResponse<RegisterEntity.DataBean> dataBeanBaseResponse) throws Exception {
                        //获取到注册成功返回的账号和密码
                        //下面模拟是模拟登陆,其实应该包含有账号名和密码等参数
                        return mService.login();
                    }
                })
                //最后将BaseResponse<T>转换成T
                .map(new HttpResultFunc<LoginEntity.DataBean>())
                .compose(RxSchedulers.<LoginEntity.DataBean>io_main())
                .subscribe(new Consumer<LoginEntity.DataBean>() {
                    @Override
                    public void accept(LoginEntity.DataBean dataBean) throws Exception {

                    }
                }, new ErrorConsumer() {
                    @Override
                    public void onErrorMsg(String errorMsg) {

                    }
                });
    }

    /**
     * 并行请求多个网络请求,在数据全部获取成功后合并数据并将其返回
     */
    private void rxjavaZip() {

    }

    private static final String TAG = "RxjavaActivity";
    private int num = 0;

    public void retryWhen(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Log.e(TAG, "subscribing");
                if (num > 2) {
                    e.onNext(num + "");
                } else {
                    e.onError(new RuntimeException("always fails"));
                }
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable.zipWith(Observable.range(1, 3), new BiFunction<Throwable, Integer, Integer>() {
                    @Override
                    public Integer apply(Throwable n, Integer i) throws Exception {
                        num = i;
                        return i;
                    }
                }).flatMap(i -> {
                    Log.e(TAG, "delay retry by " + i + " second(s)");
                    return Observable.timer(i, TimeUnit.SECONDS);
                });
            }
        }).blockingForEach(s -> Log.e(TAG, s));
    }


    public void retryWhen2(View view) {
        final boolean[] isValid = {false};
        Observable.just(false)
                .map(new Function<Boolean, Object>() {
                    @Override
                    public Object apply(Boolean aBoolean) throws Exception {
                        Log.e(TAG, "isValid:" + isValid[0]);
                        if (isValid[0]) {
                            return "hello world";
                        } else {
                            throw new IllegalArgumentException("参数错误");
                        }
                    }
                })
                //携带错误异常,the same values as the source ObservableSource
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                Log.e(TAG, throwable.getMessage());
                                if (throwable instanceof IllegalArgumentException) {
                                    //这里随便传什么参数都是重新执行,但是不能传null,否则直接走onError
                                    // 原Observable source,false Observable.just(false)
                                    isValid[0] = true;
                                    return Observable.just(1);
                                } else {
                                    return Observable.error(throwable);
                                }
                            }
                        });
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.e(TAG, o.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.getMessage());
                    }
                });
    }

    public void sync(View view) {
        //不切换thread
        Observable.create((ObservableOnSubscribe<String>) e -> {
            //阻塞2s
            Thread.sleep(2000);
            //先执行
            e.onNext("hello world");
        }).subscribe(s -> Log.e(TAG, s));
        //后执行
        Log.e(TAG, "hello xiaocai");
    }

}
