package com.xiaokun.advance_practive.ui.unit_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class MyReceiver extends BroadcastReceiver {

    public static final String NAME = "name";
    public static final String FILE_NAME = "unit_test";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences unitTestSp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = unitTestSp.edit();
        String name = intent.getStringExtra(NAME);
        edit.putString(NAME, name);
        edit.apply();
    }
}
