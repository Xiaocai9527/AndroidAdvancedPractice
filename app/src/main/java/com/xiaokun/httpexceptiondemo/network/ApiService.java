package com.xiaokun.httpexceptiondemo.network;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/19
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public interface ApiService
{
    String baseUrl = "http://www.wanandroid.com/";

    //测试服务端返回成功
    @GET("tools/mockapi/440/yx0419")
    Observable<BaseResponse<ResEntity1.DataBean>> getHttpData1();

    //测试服务端返回错误码
    @GET("tools/mockapi/440/yx04191")
    Observable<BaseResponse<ResEntity1.DataBean>> getHttpData2();

    //测试未登录
    @GET("tools/mockapi/440/yx04192")
    Observable<BaseResponse<ResEntity1.DataBean>> getHttpData3();
}
