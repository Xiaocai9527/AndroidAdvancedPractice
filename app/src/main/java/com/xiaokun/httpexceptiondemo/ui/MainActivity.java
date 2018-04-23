package com.xiaokun.httpexceptiondemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaokun.httpexceptiondemo.App;
import com.xiaokun.httpexceptiondemo.R;
import com.xiaokun.httpexceptiondemo.network.ApiService;
import com.xiaokun.httpexceptiondemo.network.ResEntity1;
import com.xiaokun.httpexceptiondemo.network.RetrofitHelper;
import com.xiaokun.httpexceptiondemo.rx.BaseObserver;
import com.xiaokun.httpexceptiondemo.rx.download.DownLoadListener;
import com.xiaokun.httpexceptiondemo.rx.download.DownLoadObserver;
import com.xiaokun.httpexceptiondemo.rx.download.DownloadEntity;
import com.xiaokun.httpexceptiondemo.rx.download.DownloadManager;
import com.xiaokun.httpexceptiondemo.rx.transform.HttpResultFunc;
import com.xiaokun.httpexceptiondemo.rx.transform.RxSchedulers;
import com.xiaokun.httpexceptiondemo.rx.util.RxManager;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "MainActivity";

    private RxManager rxManager;
    private ApiService apiService;
    private Button mButton;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button mButton9;
    private TextView mTextView;
    private DownloadEntity downloadEntity;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        DownloadManager.initDownManager(this);
        rxManager = new RxManager();
        apiService = RetrofitHelper.createService(ApiService.class, false);
    }

    private void initView()
    {
        mButton = (Button) findViewById(R.id.button);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mButton4 = (Button) findViewById(R.id.button4);
        mButton5 = (Button) findViewById(R.id.button5);
        mButton6 = (Button) findViewById(R.id.button6);
        mButton7 = (Button) findViewById(R.id.button7);
        mButton8 = (Button) findViewById(R.id.button8);
        mButton9 = (Button) findViewById(R.id.button9);
        mTextView = (TextView) findViewById(R.id.textView);

        initListener(mButton, mButton2, mButton3, mButton4, mButton5, mButton6
                , mButton7, mButton8, mButton9);
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
            case R.id.button5:
                testToken();
                break;
            case R.id.button6:
                //开始下载
                downloadFile();
                break;
            case R.id.button7:
                //暂停下载
                DownloadManager.pauseDownload(disposable, fileName);
                break;
            case R.id.button8:
                //继续下载
                downloadFile();
                break;
            case R.id.button9:
                //取消下载
                DownloadManager.cancelDownload(disposable, fileName);
                mTextView.setText("下载已取消");
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

    //测试过期token的接口
    private void testToken()
    {
        apiService = RetrofitHelper.createService(ApiService.class, RetrofitHelper.getRetrofit2());
        Observable<ResEntity1.DataBean> compose = apiService.getExpiredHttp()
                .map(new HttpResultFunc<ResEntity1.DataBean>())
                .compose(RxSchedulers.<ResEntity1.DataBean>io_main());

        compose.subscribe(new BaseObserver<ResEntity1.DataBean>(rxManager)
        {
            @Override
            protected void onErrorMsg(String msg)
            {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                mTextView.setText(msg + "\n 刷新得到的新token: " + App.getSp().getString("token", ""));
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean)
            {

            }
        });
    }

    String url = "http://imtt.dd.qq.com/16891/8EE1D586937A31F6E0B14DA48F8D362E.apk?fsname=com.dewmobile.kuaiya_5.4.2(CN)_216.apk&csr=1bbd";
    Disposable disposable;

    //测试下载文件哦
    private void downloadFile()
    {
        fileName = "httpTest.apk";
        downloadEntity = new DownloadEntity(loadListener, fileName);
        ApiService apiService = RetrofitHelper.createService(ApiService.class,
                RetrofitHelper.getDownloadRetrofit(downloadEntity));

        Observable<ResponseBody> observable = apiService.downLoadFile(url)
                .subscribeOn(Schedulers.io());
        observable.subscribe(new DownLoadObserver()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
                disposable = d;
            }
        });
    }

    DownLoadListener loadListener = new DownLoadListener()
    {
        @Override
        public void onProgress(final int progress, boolean downSuc, boolean downFailed)
        {
            if (downFailed)
            {
                mTextView.setText("下载失败");
            } else
            {
                if (!downSuc)
                {
                    mTextView.setText("下载进度：" + progress + "%");
                } else
                {
                    mTextView.setText("下载已完成");
                }
            }

        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        rxManager.clear();
    }
}
