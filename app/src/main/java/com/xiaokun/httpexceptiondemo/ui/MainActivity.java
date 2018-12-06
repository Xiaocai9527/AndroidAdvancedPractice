package com.xiaokun.httpexceptiondemo.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaokun.httpexceptiondemo.App;
import com.xiaokun.baselib.config.Constants;
import com.xiaokun.httpexceptiondemo.R;
import com.xiaokun.httpexceptiondemo.network.api.ApiService;
import com.xiaokun.baselib.network.OkhttpHelper;
import com.xiaokun.httpexceptiondemo.network.ResEntity1;
import com.xiaokun.baselib.network.RetrofitHelper;
import com.xiaokun.baselib.network.interceptors.AppCacheInterceptor;
import com.xiaokun.baselib.network.interceptors.HeaderInterceptor;
import com.xiaokun.baselib.network.interceptors.TokenInterceptor;
import com.xiaokun.baselib.rx.BaseObserver;
import com.xiaokun.baselib.rx.ErrorConsumer;
import com.xiaokun.baselib.rx.download.DownLoadListener;
import com.xiaokun.baselib.rx.download.DownLoadObserver;
import com.xiaokun.baselib.rx.download.DownloadEntity;
import com.xiaokun.baselib.rx.download.DownloadManager;
import com.xiaokun.baselib.rx.transform.HttpResultFunc;
import com.xiaokun.baselib.rx.transform.RxSchedulers;
import com.xiaokun.baselib.rx.util.RxManager;
import com.xiaokun.baselib.util.PermissionUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        DownloadManager.initDownManager(this);
        rxManager = new RxManager();
        apiService = RetrofitHelper.getInstance().createService(ApiService.class);
    }

    private void initView() {
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

    private void initListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
                requestWriteFilePermission();
                //开始下载
//                downloadFile();
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

    //请求写文件权限
    //注意本方法返回类型必须是void, 而且是无参数
    @AfterPermissionGranted(Constants.WRITE_REQUEST_CODE)
    private void requestWriteFilePermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            downloadFile();
        } else {
            EasyPermissions.requestPermissions(this, "下载文件需要文件存储",
                    Constants.WRITE_REQUEST_CODE, perms);
        }
    }

    private void testSuccess() {
        Observable<ResEntity1.DataBean> compose = apiService.getHttpData1()
                .map(new HttpResultFunc<ResEntity1.DataBean>())
                .compose(RxSchedulers.<ResEntity1.DataBean>io_main());


        //这种方式可以更加灵活地拿到每个网络请求的disposable,更加方便地对网络请求进行处理
        Disposable disposable = compose.subscribe(new Consumer<ResEntity1.DataBean>() {
            @Override
            public void accept(ResEntity1.DataBean dataBean) throws Exception {

            }
        }, new ErrorConsumer() {
            @Override
            public void onErrorMsg(String errorMsg) {

            }
        });

        compose.subscribe(new BaseObserver<ResEntity1.DataBean>(rxManager) {
            @Override
            public void onErrorMsg(String msg) {
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean) {
                //输出 E/MainActivity: onNext(MainActivity.java:53)返回成功
                mTextView.setText(dataBean.getRes());
                Toast.makeText(MainActivity.this, dataBean.getRes(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void testFailed() {
        Observable<ResEntity1.DataBean> compose = apiService.getHttpData2()
                .map(new HttpResultFunc<ResEntity1.DataBean>())
                .compose(RxSchedulers.<ResEntity1.DataBean>io_main());

        compose.subscribe(new BaseObserver<ResEntity1.DataBean>(rxManager) {
            @Override
            public void onErrorMsg(String msg) {
                //输出 E/MainActivity: errorMsg:错误码：6
                //    服务器出错
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                mTextView.setText(msg);
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean) {

            }
        });
    }

    private void testNoLogin() {
        Observable<ResEntity1.DataBean> compose = apiService.getHttpData3()
                .map(new HttpResultFunc<ResEntity1.DataBean>())
                .compose(RxSchedulers.<ResEntity1.DataBean>io_main());

        compose.subscribe(new BaseObserver<ResEntity1.DataBean>(rxManager) {
            @Override
            public void onErrorMsg(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                mTextView.setText(msg);
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean) {
                mTextView.setText(dataBean.getRes());
            }
        });
    }

    //测试过期token的接口
    private void testToken() {
        OkHttpClient okhttpClient = OkhttpHelper.getOkhttpClient(new HeaderInterceptor(), new
                        AppCacheInterceptor(),
                new TokenInterceptor());
        ApiService apiService = RetrofitHelper.getInstance().createService(ApiService.class,
                RetrofitHelper.getInstance().getRetrofit(okhttpClient, ApiService.baseUrl));
        Observable<ResEntity1.DataBean> compose = apiService.getExpiredHttp()
                .map(new HttpResultFunc<ResEntity1.DataBean>())
                .compose(RxSchedulers.<ResEntity1.DataBean>io_main());

        compose.subscribe(new BaseObserver<ResEntity1.DataBean>(rxManager) {
            @Override
            public void onErrorMsg(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                mTextView.setText(msg + "\n刷新得到的新token: " + App.getSp().getString("token", ""));
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean) {

            }
        });
    }

    String url = "http://imtt.dd.qq.com/16891/8EE1D586937A31F6E0B14DA48F8D362E.apk?fsname=com.dewmobile.kuaiya_5.4.2" +
            "(CN)_216.apk&csr=1bbd";
    Disposable disposable;

    //测试下载文件哦
    private void downloadFile() {
        fileName = "httpTest.apk";
        downloadEntity = new DownloadEntity(loadListener, fileName);
        ApiService apiService = RetrofitHelper.getInstance().createService(ApiService.class,
                RetrofitHelper.getInstance().getDownloadRetrofit(downloadEntity));

        Observable<ResponseBody> observable = apiService.downLoadFile(url)
                .subscribeOn(Schedulers.io());
        observable.subscribe(new DownLoadObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }
        });
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
        rxManager.clear();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.WRITE_REQUEST_CODE:
                //说白了就是用EasyPermissions来接管Activity中的此方法
                EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
                break;
            default:

                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case Constants.WRITE_REQUEST_CODE:
                //这里如果调用saveImgToPhone，writePer中还会执行一次
                //如果说此次动态申请的权限全部成功，没有一个拒绝，那么会执行writePer方法
//                saveImgToPhone();
                break;
            default:

                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case Constants.WRITE_REQUEST_CODE:
                Toast.makeText(MainActivity.this, "缺少文件存储，图片保存失败", Toast.LENGTH_SHORT).show();
                //在拒绝的这个地方来进行终极处理, 这里防止有人点击了不再提醒的选项
                App.getSp().edit().putInt(Constants.REQUEST_CODE_PERMISSION, Constants.WRITE_REQUEST_CODE).commit();
                PermissionUtil.showMissingPermissionDialog(this, "存储");
                break;
            default:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.WRITE_REQUEST_CODE:
                //页面返回后再次执行此方法
                requestWriteFilePermission();
                break;
            default:
                break;
        }
    }

}
