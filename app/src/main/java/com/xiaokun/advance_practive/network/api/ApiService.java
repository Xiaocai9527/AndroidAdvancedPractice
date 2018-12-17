package com.xiaokun.advance_practive.network.api;

import com.xiaokun.baselib.network.BaseResponse;
import com.xiaokun.advance_practive.network.LoginEntity;
import com.xiaokun.advance_practive.network.RegisterEntity;
import com.xiaokun.advance_practive.network.ResEntity1;
import com.xiaokun.advance_practive.network.entity.GankResEntity;
import com.xiaokun.advance_practive.network.entity.ListResEntity;
import com.xiaokun.advance_practive.network.entity.ServerResponse;
import com.xiaokun.advance_practive.network.entity.XmNeswResEntity;
import com.xiaokun.advance_practive.network.meizi.CategoryResEntity;
import com.xiaokun.baselib.network.WanBaseResponseEntity;
import com.xiaokun.advance_practive.network.wanAndroid.WanLoginEntityRes;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

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

    String baseUrl2 = "http://gank.io/";

    String baseUrl3 = "http://mushtaq.16mb.com/";

    //测试服务端返回成功
    @GET("tools/mockapi/440/yx0419")
    Observable<BaseResponse<ResEntity1.DataBean>> getHttpData1();

    //测试服务端返回错误码
    @GET("tools/mockapi/440/yx04191")
    Observable<BaseResponse<ResEntity1.DataBean>> getHttpData2();

    //测试未登录
    @GET("tools/mockapi/440/yx04192")
    Observable<BaseResponse<ResEntity1.DataBean>> getHttpData3();

    //获取token过期的http
    @GET("tools/mockapi/440/token_expired")
    Observable<BaseResponse<ResEntity1.DataBean>> getExpiredHttp();

    //获取新token
    @GET("tools/mockapi/440/yx04193")
    Call<BaseResponse<ResEntity1.DataBean>> getNewToken();

    //下载文件
    //如果文件非常大，必须要使用Streaming注解。否则retrofit会默认将整个文件读取到内存中
    //造成OOM
    @Streaming
    @GET
    Observable<ResponseBody> downLoadFile(@Url String fileUrl);

    @Streaming
    @GET("tools/test.apk")
    Observable<ResponseBody> downLoadFile();

    @GET("tools/mockapi/440/register")
    Observable<BaseResponse<RegisterEntity.DataBean>> register();

    @GET("tools/mockapi/440/login")
    Observable<BaseResponse<LoginEntity.DataBean>> login();

    @POST("/user/login")
    @FormUrlEncoded
    Observable<WanBaseResponseEntity<WanLoginEntityRes.DataBean>> login(@Field("username") String username,
                                                                        @Field("password") String password);

    @GET("api/data/{category}/{count}/{page}")
    Observable<CategoryResEntity> getCategoryData(@Path("category") String category,
                                                  @Path("count") int count, @Path("page") int page);

    //gson并不能解析一个接口，所以必须采用明确的实体类
    @GET("tools/mockapi/440/fake_gank")
    Observable<BaseResponse<List<GankResEntity.DataBean>>> getGankData();

    @GET("tools/mockapi/440/fake_xiaomi")
    Observable<BaseResponse<List<XmNeswResEntity.DataBean>>> getXmNews();

    @Multipart
    @POST("retrofit_example/upload_image.php")
    Observable<ServerResponse> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);

    @GET("api/data/{category}/{count}/{page}")
    Observable<ListResEntity> loadListData(@Path("category") String category,
                                           @Path("count") int count, @Path("page") int page);
}

