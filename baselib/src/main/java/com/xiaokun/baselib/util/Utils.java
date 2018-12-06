package com.xiaokun.baselib.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/09
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class Utils
{
    public static void close(Closeable closeable)
    {
        try
        {
            if (closeable != null)
            {
                closeable.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
