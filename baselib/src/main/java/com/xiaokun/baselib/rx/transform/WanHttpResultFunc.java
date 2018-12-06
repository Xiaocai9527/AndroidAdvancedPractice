package com.xiaokun.baselib.rx.transform;

import com.xiaokun.baselib.config.Constants;
import com.xiaokun.baselib.network.WanBaseResponseEntity;
import com.xiaokun.baselib.rx.exception.ApiException;

import io.reactivex.functions.Function;


/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/06
 *      描述  ：wanandroid转换处理
 *      版本  ：1.0
 * </pre>
 */
public class WanHttpResultFunc<T> implements Function<WanBaseResponseEntity<T>, T> {

    @Override
    public T apply(WanBaseResponseEntity<T> response) throws Exception {
        //只有当返回的code==success时才成功，其余情况全部抛出错误
        if (response.getErrorCode() == Constants.WAN_HTTP_SUCCESS) {
            return response.getData();
        } else {
            //抛出异常,让rxjava捕获,便于统一处理
            throw new ApiException.ServerException(response.getErrorCode(), response.getErrorMsg());
        }
    }
}
