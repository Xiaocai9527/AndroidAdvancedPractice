package com.xiaokun.httpexceptiondemo.network.entity;

/**
 * Created by 肖坤 on 2018/5/21.
 *
 * @author 肖坤
 * @date 2018/5/21
 */

public class ServerResponse
{
    private boolean success;
    private String message;

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
