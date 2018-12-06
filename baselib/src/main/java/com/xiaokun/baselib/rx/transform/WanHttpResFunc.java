package com.xiaokun.baselib.rx.transform;

import com.xiaokun.baselib.network.WanBaseResponseEntity;
import com.xiaokun.baselib.rx.exception.ApiException;

import io.reactivex.functions.Function;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/24
 *     描述   : 玩Android的统一处理
 *     版本   : 1.0
 * </pre>
 */
public class WanHttpResFunc<T> implements Function<WanBaseResponseEntity<T>, T>
{

    @Override
    public T apply(WanBaseResponseEntity<T> responseEntity) throws Exception
    {
        if (responseEntity.getErrorCode() > 0)
        {
            return responseEntity.getData();
        } else
        {
            throw new ApiException.ServerException(responseEntity.getErrorCode(), responseEntity.getErrorMsg());
        }
    }
}
