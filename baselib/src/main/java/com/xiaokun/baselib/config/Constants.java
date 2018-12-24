package com.xiaokun.baselib.config;

import com.xiaokun.baselib.BaseApplication;

import java.io.File;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/19
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class Constants {

    //wanandroid 请求成功码
    public static final int WAN_HTTP_SUCCESS = 0;

    //http请求成功
    public static final int HTTP_SUCCESS = 1;

    //未登录
    public static final int HTTP_NO_LOGIN = 2;

    //token过期了
    public static final int EXPIRED_TOKEN = 4;

    //================= PATH ====================
    public static final String PATH_DATA = BaseApplication.getAppContext().getCacheDir().getAbsolutePath() + File.separator +
            "data";
    public static final String PATH_CACHE = PATH_DATA + File.separator + "NetCache";


    public static final int PAUSE_DOWNLOAD = 5;
    public static final int CANCEL_DOWNLOAD = 6;

    //-------------------------------ACache缓存框架key----------------------------------------------
    //cookie
    public static final String COOKIES = "cookies";
    //================= gank数据key ====================
    public static final String GANK_DATA = "gank_data";

    //存储权限
    public static final int WRITE_REQUEST_CODE = 1001;
    /**
     * 申请权限 sp 的key
     */
    public static final String REQUEST_CODE_PERMISSION = "request_code_permission";

    public static final String SHOW_WEBVIEW = "show_webview";
    public static final String ADD_NEST_FRAGMENT1 = "add_nest_fragment1";

    //粘性测试
    public static final String STICK_TEST = "stick_test";
}
