package com.xiaokun.httpexceptiondemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xiaokun.httpexceptiondemo.network.ApiService;
import com.xiaokun.httpexceptiondemo.network.ResEntity1;
import com.xiaokun.httpexceptiondemo.rx.BaseObserver;
import com.xiaokun.httpexceptiondemo.rx.HttpResultFunc;
import com.xiaokun.httpexceptiondemo.rx.RxManager;
import com.xiaokun.httpexceptiondemo.rx.RxSchedulers;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "MainActivity";

    private RxManager rxManager;
    private ApiService apiService;
    private Button mButton;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        rxManager = new RxManager();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.baseUrl)
                //这里的client当然可以自己配置
                .client(new OkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    private void initView()
    {
        mButton = (Button) findViewById(R.id.button);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mButton4 = (Button) findViewById(R.id.button4);
        mTextView = (TextView) findViewById(R.id.textView);

        initListener(mButton, mButton2, mButton3, mButton4);
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
            case R.id.button:
                testSuccess();
                break;
            case R.id.button2:
                testFailed();
                break;
            case R.id.button3:
                //关闭网络测试输出
                //E/MainActivity: errorMsg:错误码：1000
                //    未知错误
                testFailed();
                break;
            case R.id.button4:
                testNoLogin();
                break;
            default:
                break;
        }
    }

    private void testSuccess()
    {
        Observable<ResEntity1.DataBean> compose = apiService.getHttpData1()
                .map(new HttpResultFunc<ResEntity1.DataBean>())
                .compose(RxSchedulers.<ResEntity1.DataBean>io_main());

        compose.subscribe(new BaseObserver<ResEntity1.DataBean>(rxManager)
        {
            @Override
            protected void onErrorMsg(String msg)
            {
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean)
            {
                //输出 E/MainActivity: onNext(MainActivity.java:53)返回成功
                mTextView.setText(dataBean.getRes());
                Toast.makeText(MainActivity.this, dataBean.getRes(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void testFailed()
    {
        Observable<ResEntity1.DataBean> compose = apiService.getHttpData2()
                .map(new HttpResultFunc<ResEntity1.DataBean>())
                .compose(RxSchedulers.<ResEntity1.DataBean>io_main());

        compose.subscribe(new BaseObserver<ResEntity1.DataBean>(rxManager)
        {
            @Override
            protected void onErrorMsg(String msg)
            {
                //输出 E/MainActivity: errorMsg:错误码：6
                //    服务器出错
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                mTextView.setText(msg);
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean)
            {

            }
        });
    }

    private void testNoLogin()
    {
        Observable<ResEntity1.DataBean> compose = apiService.getHttpData3()
                .map(new HttpResultFunc<ResEntity1.DataBean>())
                .compose(RxSchedulers.<ResEntity1.DataBean>io_main());

        compose.subscribe(new BaseObserver<ResEntity1.DataBean>(rxManager)
        {
            @Override
            protected void onErrorMsg(String msg)
            {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                mTextView.setText(msg);
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean)
            {
                mTextView.setText(dataBean.getRes());
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        rxManager.clear();
    }
}
