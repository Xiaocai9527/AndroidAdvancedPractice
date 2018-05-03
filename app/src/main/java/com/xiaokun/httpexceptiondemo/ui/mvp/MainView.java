package com.xiaokun.httpexceptiondemo.ui.mvp;

import com.xiaokun.httpexceptiondemo.network.ResEntity1;

import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/23
 *     描述   : V层
 *     版本   : 1.0
 * </pre>
 */
public interface MainView extends BaseView
{
    void getHttp1Suc(ResEntity1.DataBean dataBean);

    void getHttp1Failed(String errorMsg);

    void getHttp2Suc(ResEntity1.DataBean dataBean);

    void getHttp2Failed(String errorMsg);

    void getHttp3Suc(ResEntity1.DataBean dataBean);

    void getHttp3Failed(String errorMsg);

    void getExpiredSuc(ResEntity1.DataBean dataBean);

    void getExpiredFailed(String errorMsg);

    void downloadDisposable(Disposable disposable);
}
