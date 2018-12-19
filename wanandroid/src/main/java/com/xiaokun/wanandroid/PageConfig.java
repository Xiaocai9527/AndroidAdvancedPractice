package com.xiaokun.wanandroid;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 肖坤 on 2018/12/17.
 *
 * @author 肖坤
 * @date 2018/12/17
 */
public class PageConfig {

    public static List<String> pageTitles = new ArrayList<>();

    public static List<String> getPageTitles(Context context) {
        pageTitles.clear();
        pageTitles.add(context.getString(R.string.login));
        pageTitles.add(context.getString(R.string.register));
        return pageTitles;
    }

    public static final String LoginFragment = "com.xiaokun.wanandroid.LoginFragment";
    public static final String RegisterFragment = "com.xiaokun.wanandroid.RegisterFragment";

    public static String[] fragmentNames = {LoginFragment, RegisterFragment};


}
