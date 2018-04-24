package com.xiaokun.httpexceptiondemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiaokun.httpexceptiondemo.App;
import com.xiaokun.httpexceptiondemo.R;
import com.xiaokun.httpexceptiondemo.network.ResEntity1;
import com.xiaokun.httpexceptiondemo.rx.download.DownLoadListener;
import com.xiaokun.httpexceptiondemo.rx.download.DownloadEntity;
import com.xiaokun.httpexceptiondemo.rx.download.DownloadManager;
import com.xiaokun.httpexceptiondemo.rx.util.RxManager;

import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/23
 *     描述   : MVP模式
 *     版本   : 1.0
 * </pre>
 */
public class MvpMainActivity extends AppCompatActivity implements View.OnClickListener, MainView
{
    private static final String TAG = "MainActivity";

    String url = "http://imtt.dd.qq.com/16891/8EE1D586937A31F6E0B14DA48F8D362E.apk?fsname=com.dewmobile.kuaiya_5.4.2(CN)_216.apk&csr=1bbd";
    Disposable disposable;

    private RxManager rxManager;
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
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        fileName = "httpTest.apk";
        DownloadManager.initDownManager(this);
        rxManager = new RxManager();
        mainPresenter = new MainPresenter(this, rxManager);
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
                mainPresenter.getHttp1();
                break;
            case R.id.button2:
                mainPresenter.getHttp2();
                break;
            case R.id.button3:
                mainPresenter.getHttp2();
                break;
            case R.id.button4:
                mainPresenter.getHttp3();
                break;
            case R.id.button5:
                mainPresenter.getExpired();
                break;
            case R.id.button6:
                //开始下载
                downloadEntity = new DownloadEntity(loadListener, fileName);
                mainPresenter.downloadFile(url, downloadEntity);
                break;
            case R.id.button7:
                //暂停下载
                mainPresenter.pauseDownload(disposable, fileName);
                break;
            case R.id.button8:
                //继续下载
                if (downloadEntity == null)
                {
                    return;
                }
                mainPresenter.downloadFile(url, downloadEntity);
                break;
            case R.id.button9:
                //取消下载
                mainPresenter.cancelDownload(disposable, fileName);
                mTextView.setText("下载已取消");
                break;
            default:
                break;
        }
    }

    @Override
    public void getHttp1Suc(ResEntity1.DataBean dataBean)
    {
        mTextView.setText(dataBean.getRes());
    }

    @Override
    public void getHttp1Failed(String errorMsg)
    {
        mTextView.setText(errorMsg);
    }

    @Override
    public void getHttp2Suc(ResEntity1.DataBean dataBean)
    {
        mTextView.setText(dataBean.getRes());
    }

    @Override
    public void getHttp2Failed(String errorMsg)
    {
        mTextView.setText(errorMsg);
    }

    @Override
    public void getHttp3Suc(ResEntity1.DataBean dataBean)
    {
        mTextView.setText(dataBean.getRes());
    }

    @Override
    public void getHttp3Failed(String errorMsg)
    {
        mTextView.setText(errorMsg);
    }

    @Override
    public void getExpiredSuc(ResEntity1.DataBean dataBean)
    {
        mTextView.setText(dataBean.getRes());
    }

    @Override
    public void getExpiredFailed(String errorMsg)
    {
        mTextView.setText(errorMsg + "\n刷新得到的新token: " + App.getSp().getString("token", ""));
    }

    @Override
    public void downloadDisposable(Disposable disposable)
    {
        this.disposable = disposable;
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
        mainPresenter.detachView();
    }
}
