package com.xiaokun.httpexceptiondemo.ui.rxjava;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/10/25
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public interface RealGankApiService {


    @GET("data/{category}/{count}/{page}")
    Observable<GankCategoryEntity> getCategoryData(@Path("category") String category,
                                                   @Path("count") int count, @Path("page") int page);

}
