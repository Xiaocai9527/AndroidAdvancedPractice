# Android Espresso自动化测试记录

## 软件环境

**Android Studio 3.5**

**gradle插件版本 3.5.0**

**gradle版本 gradle-5.4.1-all**

gradle依赖

```groovy
// Testing-only dependencies
androidTestImplementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.10"
androidTestImplementation 'androidx.test:core:1.2.1-alpha02'
androidTestImplementation 'androidx.test:core-ktx:1.2.1-alpha02'
androidTestImplementation 'androidx.test.ext:junit:1.1.2-alpha02'
androidTestImplementation 'androidx.test.ext:junit-ktx:1.1.2-alpha02'
androidTestImplementation 'androidx.test:runner:1.3.0-alpha02'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0-alpha02'

testImplementation 'androidx.test:core:1.2.1-alpha02'
testImplementation 'androidx.test.ext:junit:1.1.2-alpha02'
testImplementation 'junit:junit:4.12'
testImplementation 'org.robolectric:robolectric:4.3'
testImplementation 'androidx.test.espresso:espresso-core:3.3.0-alpha02'
testImplementation 'androidx.test.espresso:espresso-intents:3.3.0-alpha02'
testImplementation 'androidx.test.ext:truth:1.3.0-alpha02'
```

## 测试代码

找到src目录下的androidTest文件，新建一个Java Class文件命令为AutomationJavaTest，代码如下：

```java
package com.example.santidemo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

/**
 * Created by xiaokun on 2019/11/11.
 *
 * @author xiaokun
 * @date 2019/11/11
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AutomationJavaTest {

    public static final String account = "88";
    public static final String invitedAccount = "258";

    @Rule
    public ActivityScenarioRule<FirstActivity> mScenarioRule = new ActivityScenarioRule<FirstActivity>(FirstActivity.class);

    @Test
    public void joinConference() throws InterruptedException {
        autoLogin();

        Espresso.onView(ViewMatchers.withId(R.id.btn_meeting))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.btn_join))
                .perform(ViewActions.click());

        Thread.sleep(10000 * 1000);
    }

    @Test
    public void createAndJoinConference() throws InterruptedException {
        autoLogin();

        Espresso.onView(ViewMatchers.withId(R.id.btn_meeting))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.btn_create))
                .perform(ViewActions.click());

        Thread.sleep(2 * 1000);

        Espresso.onView(ViewMatchers.withId(R.id.btn_join))
                .perform(ViewActions.click());

        Thread.sleep(10000 * 1000);
    }

    @Test
    public void autoVoipVideo() throws InterruptedException {
        autoLogin();

        Espresso.onView(ViewMatchers.withId(R.id.btn_voip))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.et_invite_id))
                .perform(ViewActions.typeText(invitedAccount), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.btn_vedio))
                .perform(ViewActions.click());

        Thread.sleep(10000 * 1000);
    }

    @Test
    public void autoVoipVoice() throws InterruptedException {
        autoLogin();

        Espresso.onView(ViewMatchers.withId(R.id.btn_voip))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.et_invite_id))
                .perform(ViewActions.typeText(invitedAccount), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.btn_voice))
                .perform(ViewActions.click());

        Thread.sleep(10000 * 1000);
    }

    @Test
    public void autoLogin() throws InterruptedException {
        Espresso.onView(ViewMatchers.withId(R.id.et_account))
                .perform(ViewActions.typeText(account), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.btn_login)).perform(ViewActions.click());

        Thread.sleep(2 * 1000);
    }

}

```

运行安装app，会让我们安装一个原包名.test的新apk，点击同意后，可以在As中直接点击有@Test注释的方法左侧的绿色三角。

当然这个方法是可以验证程序的正确性，但是另一方面可以让一些UI操作自动化，不需要人工手动去点击。这是初级用法，所以在碰到一些耗时异步操作直接使用的是Thread.sleep方法。其次某些场景中就是为了这个代替人工，为了保证测试完成后保持程序不被finish，Thread.sleep方法的参数可以设置无穷大。