package com.xiaokun.httpexceptiondemo.network.interceptors;

import com.google.gson.Gson;
import com.xiaokun.httpexceptiondemo.App;
import com.xiaokun.httpexceptiondemo.Constants;
import com.xiaokun.httpexceptiondemo.network.api.ApiService;
import com.xiaokun.httpexceptiondemo.network.BaseResponse;
import com.xiaokun.httpexceptiondemo.network.ResEntity1;
import com.xiaokun.httpexceptiondemo.network.RetrofitHelper;
import com.xiaokun.httpexceptiondemo.rx.exception.ApiException;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/24
 *     描述   : token拦截器
 *     版本   : 1.0
 * </pre>
 */
public class TokenInterceptor implements Interceptor
{
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        Response response = chain.proceed(request);
        //判断token是否过期
        if (isTokenExpired(response))
        {
            //同步请求方式,获取新token
            ApiService service = RetrofitHelper.createService(ApiService.class, false);
            Call<BaseResponse<ResEntity1.DataBean>> call = service.getNewToken();
            retrofit2.Response<BaseResponse<ResEntity1.DataBean>> tokenRes = call.execute();
            String newToken = "";
            //这里做下补充处理，这就很舒服了。
            if (tokenRes != null && tokenRes.isSuccessful())
            {
                BaseResponse<ResEntity1.DataBean> body = tokenRes.body();
                int code = body.getCode();
                if (code == Constants.HTTP_SUCCESS)
                {
                    newToken = body.getData().getRes();
                } else
                {
                    throw new ApiException.ServerException(Constants.HTTP_NO_LOGIN, body.getMessage());
                }
            } else
            {
                throw new ApiException.ServerException(Constants.HTTP_NO_LOGIN, "未登录");
            }
            //然后把这个新token存到sp中
            App.getSp().edit().putString("token", newToken).commit();
            Request newRequest = chain.request()
                    .newBuilder()
                    .header("token", newToken)
                    .build();
            response.body().close();//释放资源
            //重新请求
            return chain.proceed(newRequest);
        }
        //若没有过期,直接返回response
        return response;
    }

    /**
     * 判断token是否过期
     *
     * @param response
     * @return
     */
    private static boolean isTokenExpired(Response response)
    {
        try
        {
            String bodyString = getBodyString(response);
            BaseResponse tokenExpiredData = new Gson().fromJson(bodyString, BaseResponse.class);
            int retCode = tokenExpiredData.getCode();
            if (retCode == Constants.EXPIRED_TOKEN)
            {
                return true;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将response转换为json字符串
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static String getBodyString(Response response) throws IOException
    {
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null)
        {
            contentType.charset(charset);
        }
        //注意这里的方式,是仿写的HttpLoggingInterceptor
        //在okhttp中buffer只能被read一次,所以只能先clone然后在read
        //否则会报错
        return buffer.clone().readString(charset);
    }
}
