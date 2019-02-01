package com.xiaokun.baselib.muti_rv;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class TypeNotSupportedException extends RuntimeException
{
    private TypeNotSupportedException(String message)
    {
        super(message);
    }

    public static TypeNotSupportedException create(String message)
    {
        return new TypeNotSupportedException(message);
    }
}
