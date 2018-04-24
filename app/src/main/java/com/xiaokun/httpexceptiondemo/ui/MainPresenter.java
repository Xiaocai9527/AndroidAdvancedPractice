package com.xiaokun.httpexceptiondemo.ui;

import com.xiaokun.httpexceptiondemo.network.ResEntity1;
import com.xiaokun.httpexceptiondemo.rx.BaseObserver;
import com.xiaokun.httpexceptiondemo.rx.download.DownLoadObserver;
import com.xiaokun.httpexceptiondemo.rx.download.DownloadEntity;
import com.xiaokun.httpexceptiondemo.rx.download.DownloadManager;
import com.xiaokun.httpexceptiondemo.rx.util.RxManager;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/23
 *     描述   : P层 作用：连接V和M,逻辑操作处理
 *     版本   : 1.0
 * </pre>
 */
public class MainPresenter
{
    private WeakReference<MainView> viewWeakRef;
    private RxManager rxManager;
    private MainModel mainModel;

    public MainPresenter(MainView view, RxManager rxManager)
    {
        this.viewWeakRef = new WeakReference<MainView>(view);
        this.rxManager = rxManager;
        mainModel = new MainModel();
    }

    public MainView getView()
    {
        return viewWeakRef.get();
    }

    public void getHttp1()
    {
        mainModel.getHttpData1().subscribe(new BaseObserver<ResEntity1.DataBean>(rxManager)
        {
            @Override
            public void onErrorMsg(String msg)
            {
                getView().getHttp1Failed(msg);
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean)
            {
                getView().getHttp1Suc(dataBean);
            }
        });
    }

    public void getHttp2()
    {
        mainModel.getHttpData2().subscribe(new BaseObserver<ResEntity1.DataBean>(rxManager)
        {
            @Override
            public void onErrorMsg(String msg)
            {
                getView().getHttp2Failed(msg);
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean)
            {
                getView().getHttp2Suc(dataBean);
            }
        });
    }

    public void getHttp3()
    {
        mainModel.getHttpData3().subscribe(new BaseObserver<ResEntity1.DataBean>(rxManager)
        {
            @Override
            public void onErrorMsg(String msg)
            {
                getView().getHttp3Failed(msg);
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean)
            {
                getView().getHttp3Suc(dataBean);
            }
        });
    }

    public void getExpired()
    {
        mainModel.getExpiredHttp().subscribe(new BaseObserver<ResEntity1.DataBean>(rxManager)
        {
            @Override
            public void onErrorMsg(String msg)
            {
                getView().getExpiredFailed(msg);
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean)
            {
                getView().getExpiredSuc(dataBean);
            }
        });
    }

    //下载文件,这里不用rxmanager,这里对下载的网络请求的取消单独处理
    public void downloadFile(String url, DownloadEntity downloadEntity)
    {
        mainModel.downLoadFile(url, downloadEntity).subscribe(new DownLoadObserver()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
                getView().downloadDisposable(d);
            }
        });
    }

    //暂停下载
    public void pauseDownload(Disposable disposable, String fileName)
    {
        DownloadManager.pauseDownload(disposable, fileName);
    }

    //取消下载
    public void cancelDownload(Disposable disposable, String fileName)
    {
        DownloadManager.cancelDownload(disposable, fileName);
    }

    //解绑view，并取消网络
    public void detachView()
    {
        if (viewWeakRef != null)
        {
            viewWeakRef.clear();
            viewWeakRef = null;
        }
        if (rxManager != null)
        {
            rxManager.clear();
        }
    }
}
