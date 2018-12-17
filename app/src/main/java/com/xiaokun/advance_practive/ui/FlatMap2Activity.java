package com.xiaokun.advance_practive.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.network.api.ApiService;
import com.xiaokun.baselib.network.BaseResponse;
import com.xiaokun.advance_practive.network.LoginEntity;
import com.xiaokun.baselib.network.OkhttpHelper;
import com.xiaokun.advance_practive.network.RegisterEntity;
import com.xiaokun.baselib.network.RetrofitHelper;
import com.xiaokun.advance_practive.network.wanAndroid.WanLoginEntityRes;
import com.xiaokun.baselib.rx.BaseObserver;
import com.xiaokun.baselib.rx.transform.HttpResultFunc;
import com.xiaokun.baselib.rx.transform.RxSchedulers;
import com.xiaokun.baselib.rx.transform.WanHttpResFunc;
import com.xiaokun.baselib.rx.util.RxManager;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/24
 *     描述   : flatMap实战2
 *     版本   : 1.0
 * </pre>
 */
public class FlatMap2Activity extends AppCompatActivity implements View.OnClickListener
{
    private Button mRegister;
    private TextView mTextView;
    private RxManager rxManager;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_map_2);
        rxManager = new RxManager();
        OkHttpClient okhttp = OkhttpHelper.getDefaultClient();
        RetrofitHelper.getInstance().getRetrofit(okhttp, ApiService.baseUrl);
        apiService = RetrofitHelper.getInstance().createService(ApiService.class);
        initView();
    }

    private void initView()
    {
        mRegister = (Button) findViewById(R.id.register);
        mTextView = (TextView) findViewById(R.id.textView);
        initListener(mRegister);
    }

    private void initListener(View... views)
    {
        for (View view : views)
        {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.register:
//                registerAndLogin();
                login("abc123456789", "123456");
                break;
            default:
                break;
        }
    }

    //玩Android测试登录
    private void login(String username, String password)
    {
        apiService.login(username, password)
                .map(new WanHttpResFunc<WanLoginEntityRes.DataBean>())
                .compose(RxSchedulers.<WanLoginEntityRes.DataBean>io_main())
                .subscribe(new BaseObserver<WanLoginEntityRes.DataBean>(rxManager)
                {
                    @Override
                    public void onErrorMsg(String msg)
                    {

                    }

                    @Override
                    public void onNext(WanLoginEntityRes.DataBean dataBean)
                    {

                    }
                });
    }


    private String process = "登录flatMap流程:\n";
    private Handler mHandler = new Handler(Looper.getMainLooper());

    //注册成功后自动登录
    private void registerAndLogin()
    {

        mRegister.setText("注册中...");
        mRegister.setEnabled(false);
        process = process + "\n生产注册";
        mTextView.setText(process);
        apiService.register()
                //将注册成功后将注册的生产环境转换成登录的生产环境
                .flatMap(new Function<BaseResponse<RegisterEntity.DataBean>, ObservableSource<BaseResponse<LoginEntity.DataBean>>>()
                {
                    @Override
                    public ObservableSource<BaseResponse<LoginEntity.DataBean>> apply(BaseResponse<RegisterEntity.DataBean> dataBeanBaseResponse) throws Exception
                    {
                        process = process + "\n根据注册生产登录";
                        mHandler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mRegister.setText("注册成功，登录中...");
                                mTextView.setText(process);
                            }
                        });
                        return apiService.login();
                    }
                })
                //最后将BaseResponse<T>转换成T
                .map(new HttpResultFunc<LoginEntity.DataBean>())
                .compose(RxSchedulers.<LoginEntity.DataBean>io_main())
                .subscribe(new BaseObserver<LoginEntity.DataBean>(rxManager)
                {
                    @Override
                    public void onErrorMsg(String msg)
                    {
                        Toast.makeText(FlatMap2Activity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(LoginEntity.DataBean dataBean)
                    {
                        process = process + "\n消费登录";
                        mRegister.setEnabled(true);
                        mRegister.setText("注册");
                        mTextView.setText(process);
                        Toast.makeText(FlatMap2Activity.this, dataBean.getLogin(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
