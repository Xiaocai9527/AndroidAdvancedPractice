package com.xiaokun.httpexceptiondemo.rx;

import com.xiaokun.httpexceptiondemo.Constants;
import com.xiaokun.httpexceptiondemo.network.BaseResponse;

import io.reactivex.functions.Function;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/19
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class HttpResultFunc<T> implements Function<BaseResponse<T>, T>
{
    @Override
    public T apply(BaseResponse<T> response) throws Exception
    {
        //只有当返回的code==success时才成功，其余情况全部抛出错误
        if (response.getCode() == Constants.HTTP_SUCCESS)
        {
            return response.getData();
        } else
        {
            //抛出异常,让rxjava捕获,便于统一处理
            throw new ApiException.ServerException(response.getCode(), response.getMessage());
        }
    }
}
