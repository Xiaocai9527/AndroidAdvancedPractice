package com.xiaokun.baselib.network.interceptors;

import com.xiaokun.baselib.rx.upload.ProgressRequestBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/25
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class UploadInterceptor implements Interceptor {

    private ProgressRequestBody.Listener mListener;

    public UploadInterceptor(ProgressRequestBody.Listener listener) {
        mListener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .post(new ProgressRequestBody(request.body(), mListener))
                .build();
        return chain.proceed(newRequest);
    }
}
