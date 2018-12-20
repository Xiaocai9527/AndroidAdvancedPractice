package com.xiaokun.advance_practive.ui.fragment_nest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jakewharton.rxrelay2.Relay;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.ui.big_mvp.detail.DetailFragment;
import com.xiaokun.advance_practive.ui.big_mvp.list.ListFragment;
import com.xiaokun.baselib.config.Constants;
import com.xiaokun.baselib.rx.util.RxBus3;

import java.util.Stack;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/17
 *      描述  ：嵌套fragment的处理方法
 *      版本  ：1.0
 * </pre>
 */
public class FragmentNestActivity extends AppCompatActivity implements View.OnClickListener {

    public FrameLayout mContainerFl;
    private Stack<Fragment> mFragmentStack = new Stack<>();

    /**
     * 当前fragment
     */
    private Fragment currentFragment;

    public static void start(Context context) {
        Intent starter = new Intent(context, FragmentNestActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_nest);
        initView();
        addFragment(NestFragment1.newInstance());

        Relay<String> register = RxBus3.getInstance().register(Constants.SHOW_WEBVIEW);
        register.subscribe(s -> addFragment(NestFragment2.newInstance(s)));

        RxBus3.getInstance().onEvent(Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("test");
            }
        }), new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                String o1 = (String) o;
                Toast.makeText(FragmentNestActivity.this, o1, Toast.LENGTH_SHORT).show();
            }
        });
        RxBus3.getInstance().postStick(Constants.STICK_TEST, "stick_test");
    }

    private void initView() {
        mContainerFl = findViewById(R.id.container_fl);
    }

    /**
     * 添加fragment
     *
     * @param fragment
     */
    public void addFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        currentFragment = getCurrentFragment();
        if (getCurrentFragment() != null) {
            ft.hide(currentFragment);
        }
        if (fragment.isAdded()) {
            ft.hide(currentFragment);
            ft.show(fragment);
        } else {
            ft.add(R.id.container_fl, fragment);
            ft.addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
        //会引发Can not perform this action after onSaveInstanceState
        //https://medium.com/@elye.project/handling-illegalstateexception-can-not-perform-this-action-after-onsaveinstancestate-d4ee8b630066
        // 使用ft.commitAllowingStateLoss();代替
        //ft.commit();
        mFragmentStack.push(fragment);
    }

    private static final String TAG = "FragmentNestActivity";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")");
    }

    /**
     * 获取当前fragment
     *
     * @return
     */
    private Fragment getCurrentFragment() {
        currentFragment = getSupportFragmentManager().findFragmentById(R.id.container_fl);
        return currentFragment;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            mFragmentStack.pop();
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus3.getInstance().unregister(Constants.SHOW_WEBVIEW);
    }
}
