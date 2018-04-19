package com.xiaokun.httpexceptiondemo.network;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/19
 *     描述   : 这个需要注意的是，Code Message Data必须和后台定义的属性名称一致
 *     版本   : 1.0
 * </pre>
 */
public class BaseResponse<T>
{
    private int Code;
    private String Message;
    private T Data;

    public int getCode()
    {
        return Code;
    }

    public void setCode(int code)
    {
        Code = code;
    }

    public String getMessage()
    {
        return Message;
    }

    public void setMessage(String message)
    {
        Message = message;
    }

    public T getData()
    {
        return Data;
    }

    public void setData(T data)
    {
        Data = data;
    }
}
