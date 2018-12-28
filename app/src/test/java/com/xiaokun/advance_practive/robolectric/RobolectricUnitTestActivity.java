package com.xiaokun.advance_practive.robolectric;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.xiaokun.advance_practive.BuildConfig;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.ui.unit_test.MyReceiver;
import com.xiaokun.advance_practive.ui.unit_test.MyService;
import com.xiaokun.advance_practive.ui.unit_test.UnitTestActivity;
import com.xiaokun.wanandroid.WanLoginActivity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ServiceController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowToast;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class RobolectricUnitTestActivity {

    private static final String TAG = "RobolectricUnitTestActi";

    public Button mbutton36;
    public Button mbutton38;
    public Button mbutton37;
    public Button mbutton43;
    public Button mbutton42;
    public Button mbutton45;
    public Button mbutton44;
    public Button mbutton41;
    public Button mbutton40;
    public Button mbutton39;
    private UnitTestActivity mUnitTestActivity;
    private ServiceController<MyService> mController;
    private MyService mService;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
        mUnitTestActivity = Robolectric.setupActivity(UnitTestActivity.class);
        mController = Robolectric.buildService(MyService.class);
        mService = mController.get();

        mbutton36 = mUnitTestActivity.findViewById(R.id.button36);
        mbutton38 = mUnitTestActivity.findViewById(R.id.button38);
        mbutton37 = mUnitTestActivity.findViewById(R.id.button37);
        mbutton43 = mUnitTestActivity.findViewById(R.id.button43);
        mbutton42 = mUnitTestActivity.findViewById(R.id.button42);
        mbutton45 = mUnitTestActivity.findViewById(R.id.button45);
        mbutton44 = mUnitTestActivity.findViewById(R.id.button44);
        mbutton41 = mUnitTestActivity.findViewById(R.id.button41);
        mbutton40 = mUnitTestActivity.findViewById(R.id.button40);
        mbutton39 = mUnitTestActivity.findViewById(R.id.button39);
    }

    @Test
    public void testActivity() {
        Assert.assertNotNull(mUnitTestActivity);
        Log.e(TAG, "testActivity(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + "测试log");
    }

    @Test
    public void testActivityJump() {
        mbutton36.performClick();

        ShadowActivity shadowActivity = Shadows.shadowOf(mUnitTestActivity);
        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();
        Assert.assertEquals(nextStartedActivity.getComponent().getClassName(), WanLoginActivity.class.getName());
    }

    @Test
    public void testToast() {
        Toast latestToast = ShadowToast.getLatestToast();
        Assert.assertNull(latestToast);

        mbutton37.performClick();

        latestToast = ShadowToast.getLatestToast();
        Assert.assertNotNull(latestToast);
        ShadowToast shadowToast = Shadows.shadowOf(latestToast);
        Assert.assertEquals("哈皮", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void testDialog() {
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Assert.assertNull(dialog);

        mbutton38.performClick();

        dialog = ShadowAlertDialog.getLatestAlertDialog();
        Assert.assertNotNull(dialog);

        ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(dialog);
        Assert.assertEquals("标题", shadowAlertDialog.getTitle());
        Assert.assertEquals("这是一个dialog", shadowAlertDialog.getMessage());
    }

    @Test
    public void testUiState() {
        boolean enabled = mbutton39.isEnabled();
        mbutton39.performClick();

        enabled = mbutton39.isEnabled();

        Assert.assertFalse(enabled);
    }

    @Test
    public void testNewFragment() {

    }

    @Test
    public void testResource() {
        Application application = RuntimeEnvironment.application;
        String string = application.getString(R.string.app_name);
        Assert.assertEquals("android汇总", string);
    }

    private String action = "xiaokun.hahaha";

    /**
     * 验证是否有广播
     */
    @Test
    public void testRegister() {
        ShadowApplication instance = ShadowApplication.getInstance();
        //也可以通过这个方法拿到application
        String string = instance.getApplicationContext().getString(R.string.app_name);
        Assert.assertEquals("android汇总", string);

        Intent intent = new Intent(action);
        Assert.assertTrue(instance.hasReceiverForIntent(intent));
    }

    /**
     * 发送广播,验证广播
     */
    @Test
    public void testReceive() {
        Application application = RuntimeEnvironment.application;
        Intent intent = new Intent(action);
        intent.putExtra(MyReceiver.NAME, "肖坤是个大帅哥");
        MyReceiver receiver = new MyReceiver();
        receiver.onReceive(application, intent);

        //验证逻辑
        SharedPreferences sp = application.getSharedPreferences(MyReceiver.FILE_NAME, Context.MODE_PRIVATE);
        String string = sp.getString(MyReceiver.NAME, "");
        Assert.assertEquals("肖坤是个大帅哥", string);
    }

    @Test
    public void testService() {
        mController.create();
        mController.startCommand(0, 0);
        mController.bind();
        mController.unbind();
        mController.destroy();
    }

}
