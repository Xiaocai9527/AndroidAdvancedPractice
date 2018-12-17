package com.xiaokun.advance_practive.ui.mvp;

import com.xiaokun.advance_practive.network.ResEntity1;
import com.xiaokun.advance_practive.network.entity.GankResEntity;
import com.xiaokun.advance_practive.network.entity.ServerResponse;
import com.xiaokun.advance_practive.network.entity.UniversalResEntity;
import com.xiaokun.advance_practive.network.entity.XmNeswResEntity;
import com.xiaokun.baselib.rx.BaseObserver;
import com.xiaokun.baselib.rx.download.DownLoadObserver;
import com.xiaokun.baselib.rx.download.DownloadEntity;
import com.xiaokun.baselib.rx.download.DownloadManager;
import com.xiaokun.baselib.rx.util.RxManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
    private WeakReference<BaseView> viewWeakRef;
    private RxManager rxManager;
    private MainModel mainModel;

    public MainPresenter(BaseView view, RxManager rxManager)
    {
        this.viewWeakRef = new WeakReference<BaseView>(view);
        this.rxManager = rxManager;
        mainModel = new MainModel();
    }

    public BaseView getView()
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
                ((MainView) getView()).getHttp1Failed(msg);
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean)
            {
                ((MainView) getView()).getHttp1Suc(dataBean);
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
                ((MainView) getView()).getHttp2Failed(msg);
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean)
            {
                ((MainView) getView()).getHttp2Suc(dataBean);
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
                ((MainView) getView()).getHttp3Failed(msg);
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean)
            {
                ((MainView) getView()).getHttp3Suc(dataBean);
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
                ((MainView) getView()).getExpiredFailed(msg);
            }

            @Override
            public void onNext(ResEntity1.DataBean dataBean)
            {
                ((MainView) getView()).getExpiredSuc(dataBean);
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
                ((MainView) getView()).downloadDisposable(d);
            }
        });
    }

    public void getGankData()
    {
        if (getView() instanceof UniversalView)
        {
            BaseObserver<List<GankResEntity.DataBean>> observer = new BaseObserver<List<GankResEntity.DataBean>>(rxManager)
            {
                @Override
                public void onErrorMsg(String msg)
                {
                    ((UniversalView) getView()).getUniversalFailed(msg);
                }

                @Override
                public void onNext(List<GankResEntity.DataBean> dataBeans)
                {
                    List<UniversalResEntity> universalResEntities = new ArrayList<>();
                    for (GankResEntity.DataBean dataBean : dataBeans)
                    {
                        universalResEntities.add(dataBean);
                    }
                    ((UniversalView) getView()).getUniversalSuc(universalResEntities);
                }
            };
            mainModel.getGankData().subscribe(observer);
        }
    }

    public void getXmNewsData()
    {
        if (getView() instanceof UniversalView)
        {
            BaseObserver<List<XmNeswResEntity.DataBean>> observer = new BaseObserver<List<XmNeswResEntity.DataBean>>(rxManager)
            {
                @Override
                public void onErrorMsg(String msg)
                {
                    ((UniversalView) getView()).getUniversalFailed(msg);
                }

                @Override
                public void onNext(List<XmNeswResEntity.DataBean> dataBeans)
                {
                    List<UniversalResEntity> universalResEntities = new ArrayList<>();
                    for (XmNeswResEntity.DataBean dataBean : dataBeans)
                    {
                        universalResEntities.add(dataBean);
                    }
                    ((UniversalView) getView()).getUniversalSuc(universalResEntities);
                }
            };
            mainModel.getXmData().subscribe(observer);
        }
    }

    public void upload(MultipartBody.Part file, RequestBody name)
    {
        mainModel.upload(file, name).subscribe(new BaseObserver<ServerResponse>(rxManager)
        {
            @Override
            public void onErrorMsg(String msg)
            {

            }

            @Override
            public void onNext(ServerResponse serverResponse)
            {

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
