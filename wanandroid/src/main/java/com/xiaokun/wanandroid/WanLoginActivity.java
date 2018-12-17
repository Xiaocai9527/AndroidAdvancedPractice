package com.xiaokun.wanandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer;
import com.xiaokun.wanandroid.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/17
 *      描述  ：wanAndroid登录页面
 *      版本  ：1.0
 * </pre>
 */
public class WanLoginActivity extends AppCompatActivity implements View.OnClickListener {
    public CustomViewPager mviewpager;

    public static void start(Context context) {
        Intent starter = new Intent(context, WanLoginActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mviewpager = findViewById(R.id.viewpager);
        CardFlipPageTransformer cardFlipPageTransformer = new CardFlipPageTransformer();
        cardFlipPageTransformer.setScalable(false);
        cardFlipPageTransformer.setFlipOrientation(CardFlipPageTransformer.VERTICAL);

        List<Fragment> fragments = getFragmentsByReflect();

        MyAdapter adapter = new MyAdapter(getSupportFragmentManager(), fragments);
        Utils.changViewpagerTime(mviewpager);
        mviewpager.setAdapter(adapter);
        mviewpager.setPageTransformer(true, cardFlipPageTransformer);
        mviewpager.setPagingEnabled(false);
    }

    /**
     * 默认的传统方法
     *
     * @return
     */
    private List<Fragment> getFragmentsDefault() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(LoginFragment.newInstance());
        fragments.add(RegisterFragment.newInstance());
        return fragments;
    }

    /**
     * 通过反射获取fragment,更加解耦
     *
     * @return
     */
    private List<Fragment> getFragmentsByReflect() {
        List<Fragment> fragments = new ArrayList<>();
        try {
            for (String fragmentName : PageConfig.fragmentNames) {
                Class<?> clazz = Class.forName(fragmentName);
                Fragment fragment = (Fragment) clazz.newInstance();
                fragments.add(fragment);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return fragments;
    }

    public void openFg(int position) {
        mviewpager.setCurrentItem(position);
    }

    private class MyAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragments;

        public MyAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments == null ? null : mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }
}
