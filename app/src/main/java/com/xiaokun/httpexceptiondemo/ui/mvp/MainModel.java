package com.xiaokun.httpexceptiondemo.ui.mvp;

import com.xiaokun.httpexceptiondemo.network.OkhttpHelper;
import com.xiaokun.httpexceptiondemo.network.ResEntity1;
import com.xiaokun.httpexceptiondemo.network.RetrofitHelper;
import com.xiaokun.httpexceptiondemo.network.api.ApiService;
import com.xiaokun.httpexceptiondemo.network.entity.GankResEntity;
import com.xiaokun.httpexceptiondemo.network.entity.ServerResponse;
import com.xiaokun.httpexceptiondemo.network.entity.XmNeswResEntity;
import com.xiaokun.httpexceptiondemo.network.interceptors.TokenInterceptor;
import com.xiaokun.httpexceptiondemo.rx.download.DownloadEntity;
import com.xiaokun.httpexceptiondemo.rx.transform.HttpResultFunc;
import com.xiaokun.httpexceptiondemo.rx.transform.RxSchedulers;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/23
 *     描述   : m层，里面主要提供对数据的操作方法
 *     版本   : 1.0
 * </pre>
 */
public class MainModel
{
    private ApiService apiService = RetrofitHelper.createService(ApiService.class, false);

    //获取"tools/mockapi/440/yx0419"接口数据
    public Observable<ResEntity1.DataBean> getHttpData1()
    {
        return apiService.getHttpData1()
                .map(new HttpResultFunc<ResEntity1.DataBean>())
                .compose(RxSchedulers.<ResEntity1.DataBean>io_main());
    }

    //获取"tools/mockapi/440/yx04191"接口数据
    public Observable<ResEntity1.DataBean> getHttpData2()
    {
        return apiService.getHttpData2()
                .map(new HttpResultFunc<ResEntity1.DataBean>())
                .compose(RxSchedulers.<ResEntity1.DataBean>io_main());
    }

    //获取"tools/mockapi/440/yx04192"接口数据
    public Observable<ResEntity1.DataBean> getHttpData3()
    {
        return apiService.getHttpData3()
                .map(new HttpResultFunc<ResEntity1.DataBean>())
                .compose(RxSchedulers.<ResEntity1.DataBean>io_main());
    }

    //获取"tools/mockapi/440/token_expired"接口数据
    public Observable<ResEntity1.DataBean> getExpiredHttp()
    {
        OkHttpClient okhttpClient = OkhttpHelper.getOkhttpClient(false, new TokenInterceptor());
        ApiService apiService = RetrofitHelper.createService(ApiService.class, RetrofitHelper.getRetrofit(okhttpClient,
                ApiService.baseUrl));
        return apiService.getExpiredHttp()
                .map(new HttpResultFunc<ResEntity1.DataBean>())
                .compose(RxSchedulers.<ResEntity1.DataBean>io_main());
    }

    //下载
    public Observable<ResponseBody> downLoadFile(String url, DownloadEntity downloadEntity)
    {
        ApiService apiService = RetrofitHelper.createService(ApiService.class,
                RetrofitHelper.getDownloadRetrofit(downloadEntity));
        return apiService.downLoadFile(url).subscribeOn(Schedulers.io());
    }

    //获取gank
    public Observable<List<GankResEntity.DataBean>> getGankData()
    {
        return apiService.getGankData()
                .map(new HttpResultFunc<List<GankResEntity.DataBean>>())
                .compose(RxSchedulers.<List<GankResEntity.DataBean>>io_main());
    }

    //获取小米新闻
    public Observable<List<XmNeswResEntity.DataBean>> getXmData()
    {
        return apiService.getXmNews()
                .map(new HttpResultFunc<List<XmNeswResEntity.DataBean>>())
                .compose(RxSchedulers.<List<XmNeswResEntity.DataBean>>io_main());
    }

    //上传图片
    public Observable<ServerResponse> upload(MultipartBody.Part file, RequestBody name)
    {
        apiService = RetrofitHelper.getRetrofit(OkhttpHelper.getDefaultClient(false), ApiService.baseUrl3).create(ApiService.class);
        return apiService.uploadFile(file, name)
                .compose(RxSchedulers.<ServerResponse>io_main());
    }
}

