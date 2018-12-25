package com.xiaokun.advance_practive.ui.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiaokun.advance_practive.App;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.network.ResEntity1;
import com.xiaokun.advance_practive.network.entity.UniversalResEntity;
import com.xiaokun.advance_practive.ui.viewpager.ViewPagerActivity;
import com.xiaokun.baselib.rx.download.DownloadManager;
import com.xiaokun.baselib.rx.upload.UploadManager;
import com.xiaokun.baselib.rx.util.RxManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.xiaokun.baselib.rx.download.ProgressResponseBody.DownloadEntity;
import com.xiaokun.baselib.rx.download.ProgressResponseBody.DownLoadListener;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/23
 *     描述   : MVP模式
 *     版本   : 1.0
 * </pre>
 */
public class MvpMainActivity extends AppCompatActivity implements View.OnClickListener, MainView, UniversalView {
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
    private Button mButton16;
    private Button mButton17;
    private Button mButton18;
    private TextView mTextView;
    private DownloadEntity downloadEntity;
    private String fileName;
    private String filePath;
    private File mDownloadFile;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        fileName = "httpTest.apk";
        filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + fileName;
        mDownloadFile = new File(filePath);
        if (!mDownloadFile.exists()) {
            try {
                mDownloadFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        DownloadManager.init(this);
        UploadManager.init(this);
        rxManager = new RxManager();
        mainPresenter = new MainPresenter(this, rxManager);
    }

    private void initView() {
        mButton = findViewById(R.id.button);
        mButton2 = findViewById(R.id.button2);
        mButton3 = findViewById(R.id.button3);
        mButton4 = findViewById(R.id.button4);
        mButton5 = findViewById(R.id.button5);
        mButton6 = findViewById(R.id.button6);
        mButton7 = findViewById(R.id.button7);
        mButton8 = findViewById(R.id.button8);
        mButton9 = findViewById(R.id.button9);
        mButton16 = findViewById(R.id.button16);
        mButton17 = findViewById(R.id.button17);
        mButton18 = findViewById(R.id.button18);
        mTextView = findViewById(R.id.textView);

        initListener(mButton, mButton2, mButton3, mButton4, mButton5, mButton6
                , mButton7, mButton8, mButton9, mButton16, mButton17, mButton18);
    }

    private void initListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                mainPresenter.getHttp1();
                break;
            case R.id.button2:
                mainPresenter.getHttp2();
                break;
            case R.id.button3:
                Intent intent = new Intent(this, ViewPagerActivity.class);
                startActivity(intent);
                Observable.just(1)
                        .subscribe(integer -> Log.d(TAG, "onNext: " + Thread.currentThread()));

                Observable.timer(2, TimeUnit.SECONDS)
                        .subscribe(aLong -> Log.d(TAG, "onNext: " + Thread.currentThread()));
                break;
            case R.id.button4:
                mainPresenter.getHttp3();
                break;
            case R.id.button5:
                mainPresenter.getExpired();
                break;
            case R.id.button6:
                //开始下载
                if (downloadEntity == null) {
                    downloadEntity = new DownloadEntity(loadListener, mDownloadFile);
                }
                mainPresenter.downloadFile(url, downloadEntity);
                break;
            case R.id.button7:
                //暂停下载
                mainPresenter.pauseDownload(disposable, mDownloadFile);
                break;
            case R.id.button8:
                //继续下载
                if (downloadEntity == null) {
                    return;
                }
                mainPresenter.downloadFile(url, downloadEntity);
                break;
            case R.id.button9:
                //取消下载
                mainPresenter.cancelDownload(disposable, mDownloadFile);
                mTextView.setText("下载已取消");
                break;
            case R.id.button16:
                UniversalActivity.startUniversalActivity(this, 1);
                break;
            case R.id.button17:
                UniversalActivity.startUniversalActivity(this, 2);
                break;
            case R.id.button18:
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(),
                        "http_exception");
                String fileName = "meizi.jpg";
                File imgFile = new File(file, fileName);
                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), imgFile);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("header", imgFile.getName(), requestBody);
                RequestBody fileName1 = RequestBody.create(MediaType.parse("text/plain"), imgFile.getName());
                mainPresenter.upload(fileToUpload, fileName1);
                break;
            default:
                break;
        }
    }

    @Override
    public void getHttp1Suc(ResEntity1.DataBean dataBean) {
        mTextView.setText(dataBean.getRes());
    }

    @Override
    public void getHttp1Failed(String errorMsg) {
        mTextView.setText(errorMsg);
    }

    @Override
    public void getHttp2Suc(ResEntity1.DataBean dataBean) {
        mTextView.setText(dataBean.getRes());
    }

    @Override
    public void getHttp2Failed(String errorMsg) {
        mTextView.setText(errorMsg);
    }

    @Override
    public void getHttp3Suc(ResEntity1.DataBean dataBean) {
        mTextView.setText(dataBean.getRes());
    }

    @Override
    public void getHttp3Failed(String errorMsg) {
        mTextView.setText(errorMsg);
    }

    @Override
    public void getExpiredSuc(ResEntity1.DataBean dataBean) {
        mTextView.setText(dataBean.getRes());
    }

    @Override
    public void getExpiredFailed(String errorMsg) {
        mTextView.setText(errorMsg + "\n刷新得到的新token: " + App.getSp().getString("token", ""));
    }

    @Override
    public void downloadDisposable(Disposable disposable) {
        this.disposable = disposable;
    }

    DownLoadListener loadListener = new DownLoadListener() {
        @Override
        public void onProgress(final int progress, boolean downSuc, boolean downFailed) {
            if (downFailed) {
                mTextView.setText("下载失败");
            } else {
                if (!downSuc) {
                    mTextView.setText("下载进度：" + progress + "%");
                } else {
                    mTextView.setText("下载已完成");
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
    }

    @Override
    public void getUniversalSuc(List<UniversalResEntity> entity) {
        String text1 = entity.get(0).getText1();
    }

    @Override
    public void getUniversalFailed(String errorMsg) {

    }
}
