package com.xiaokun.baselib.rx;

import android.content.Intent;

import com.xiaokun.baselib.BaseApplication;
import com.xiaokun.baselib.config.Constants;
import com.xiaokun.baselib.rx.exception.ApiException;
import com.xiaokun.baselib.rx.util.RxManager;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/19
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public abstract class BaseObserver<T> implements Observer<T>
{
    protected RxManager rxManager;

    public BaseObserver(RxManager rxManager)
    {
        this.rxManager = rxManager;
    }

    @Override
    public void onSubscribe(Disposable d)
    {
        rxManager.add(d);
    }

    @Override
    public void onComplete()
    {

    }

    @Override
    public void onError(Throwable e)
    {
        //统一处理错误
        String msg = ApiException.handlerException(e).getMsg();
        int errorCode = ApiException.handlerException(e).getErrorCode();
        if (msg.length() > 64)
        {
            msg = msg.substring(0, 64);
        }
        if (errorCode == Constants.HTTP_NO_LOGIN)
        {
            //跳转至登录页面
//            Intent intent = new Intent(BaseApplication.getAppContext(), LoginActivity.class);
//            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
//            BaseApplication.getAppContext().startActivity(intent);
        }
        onErrorMsg("错误码：" + errorCode + "\n" + msg);
    }

    /**
     * 返回错误字符串
     *
     * @param msg
     */
    public abstract void onErrorMsg(String msg);

    @Override
    public abstract void onNext(T t);

}
