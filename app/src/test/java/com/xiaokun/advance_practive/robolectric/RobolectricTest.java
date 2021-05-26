package com.xiaokun.advance_practive.robolectric;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xiaokun.advance_practive.BuildConfig;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.ui.HomeActivity;
import com.xiaokun.advance_practive.ui.MainActivity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowLog;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
@RunWith(RobolectricTestRunner.class)
public class RobolectricTest {

    private static final String TAG = "RobolectricTest";

    private HomeActivity mHomeActivity;
    private Button mButton10;
    private Button mButton11;
    private Button mButton12;
    private Button mButton14;
    private Button mButton15;
    private Button mButton19;
    private Button mButton20;
    private Button mButton21;
    private Button mButton22;
    private Button mButton23;
    private Button mButton24;
    private Button mButton25;
    private Button mButton26;
    private Button mButton30;
    private Button mButton31;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
        mHomeActivity = Robolectric.setupActivity(HomeActivity.class);
        mButton10 = mHomeActivity.findViewById(R.id.button10);
        mButton11 = mHomeActivity.findViewById(R.id.button11);
        mButton12 = mHomeActivity.findViewById(R.id.button12);
        mButton14 = mHomeActivity.findViewById(R.id.button14);
        mButton15 = mHomeActivity.findViewById(R.id.button15);
        mButton19 = mHomeActivity.findViewById(R.id.button19);
        mButton20 = mHomeActivity.findViewById(R.id.button20);
        mButton21 = mHomeActivity.findViewById(R.id.button21);
        mButton22 = mHomeActivity.findViewById(R.id.button22);
        mButton23 = mHomeActivity.findViewById(R.id.button23);
        mButton24 = mHomeActivity.findViewById(R.id.button24);
        mButton25 = mHomeActivity.findViewById(R.id.button25);
        mButton26 = mHomeActivity.findViewById(R.id.button26);
        mButton30 = mHomeActivity.findViewById(R.id.button30);
        mButton31 = mHomeActivity.findViewById(R.id.button31);
    }

    @Test
    public void testActivity() {
        Assert.assertNotNull(mHomeActivity);
        Log.e(TAG, "testActivity(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + "测试log");
    }

    @Test
    public void testButton10() {
        Assert.assertEquals(mButton10.getText().toString(), "Main");
        //触发按钮点击
        mButton10.performClick();
        //获取shadow类
        ShadowActivity shadowActivity = Shadows.shadowOf(mHomeActivity);
        //借助shadow类获取下一Activity的Intent
        Intent intent = shadowActivity.getNextStartedActivity();
        //验证intent是否和预期的一致
        Assert.assertEquals(intent.getComponent().getClassName(), MainActivity.class.getName());
    }


}
