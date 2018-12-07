package com.xiaokun.baselib.rx;

import android.content.Intent;

import com.xiaokun.baselib.config.Constants;
import com.xiaokun.baselib.rx.exception.ApiException;

import io.reactivex.functions.Consumer;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/10/25
 *      描述  ：错误的consumer抽象类统一处理
 *      版本  ：1.0
 * </pre>
 */
public abstract class ErrorConsumer implements Consumer<Throwable> {
    @Override
    public void accept(Throwable throwable) throws Exception {
        //统一处理错误
        String msg = ApiException.handlerException(throwable).getMsg();
        int errorCode = ApiException.handlerException(throwable).getErrorCode();
        if (msg.length() > 64) {
            msg = msg.substring(0, 64);
        }
        if (errorCode == Constants.HTTP_NO_LOGIN) {
            //跳转至登录页面
//            Intent intent = new Intent(App.getAppContext(), LoginActivity.class);
//            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
//            App.getAppContext().startActivity(intent);
        }
        onErrorMsg("错误码：" + errorCode + "\n" + msg);
    }

    public abstract void onErrorMsg(String errorMsg);
}
