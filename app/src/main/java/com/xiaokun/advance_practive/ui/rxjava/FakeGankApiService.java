package com.xiaokun.advance_practive.ui.rxjava;

import com.xiaokun.baselib.network.BaseResponse;
import com.xiaokun.advance_practive.network.entity.GankResEntity;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/10/25
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public interface FakeGankApiService {


    //gson并不能解析一个接口，所以必须采用明确的实体类
    @GET("tools/mockapi/440/fake_gank")
    Single<BaseResponse<List<GankResEntity.DataBean>>> getGankData();
}
