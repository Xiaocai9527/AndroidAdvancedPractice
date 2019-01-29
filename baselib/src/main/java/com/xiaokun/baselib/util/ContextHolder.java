package com.xiaokun.baselib.util;

import android.content.Context;

/**
 * Created by 肖坤 on 2018/12/29.
 *
 * @author 肖坤
 * @date 2018/12/29
 */

public class ContextHolder {

    private static Context sContext;

    public static void setContext(Context context) {
        sContext = context;
    }

    public static Context getContext() {
        return sContext;
    }
}
