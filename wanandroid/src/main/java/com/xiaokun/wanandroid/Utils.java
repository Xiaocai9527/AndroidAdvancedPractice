package com.xiaokun.wanandroid;

import android.support.v4.view.ViewPager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/17
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class Utils {


    /**
     * 改变viewpager自动播放的滑动时间为500ms 默认是250ms
     */
    public static void changViewpagerTime(ViewPager viewPager) {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            Interpolator sInterpolator = new AccelerateDecelerateInterpolator();
            FixedScroller scroller = new FixedScroller(viewPager.getContext(), sInterpolator);
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
