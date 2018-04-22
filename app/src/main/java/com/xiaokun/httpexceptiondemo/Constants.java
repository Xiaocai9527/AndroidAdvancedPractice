package com.xiaokun.httpexceptiondemo;

import java.io.File;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/19
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class Constants
{
    //http请求成功
    public static final int HTTP_SUCCESS = 1;

    //未登录
    public static final int HTTP_NO_LOGIN = 2;

    //token过期了
    public static final int EXPIRED_TOKEN = 4;

    //================= PATH ====================
    public static final String PATH_DATA = App.getAppContext().getCacheDir().getAbsolutePath() + File.separator + "data";
    public static final String PATH_CACHE = PATH_DATA + File.separator + "NetCache";


    public static final int PAUSE_DOWNLOAD = 5;
    public static final int CANCEL_DOWNLOAD = 6;
}
