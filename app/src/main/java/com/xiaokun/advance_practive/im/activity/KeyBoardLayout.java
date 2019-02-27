package com.xiaokun.advance_practive.im.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xiaokun.advance_practive.im.util.IMEUtils;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/27
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class KeyBoardLayout extends LinearLayout {

    public KeyBoardLayout(Context context) {
        super(context);
    }

    public KeyBoardLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isTouchView(filterViewByIds(), ev)) {
                return super.dispatchTouchEvent(ev);
            }
            if (hideSoftByEditViewIds() == null || hideSoftByEditViewIds().length == 0) {
                return super.dispatchTouchEvent(ev);
            }
            if (getContext() instanceof Activity) {
                View v = ((Activity) getContext()).getCurrentFocus();
                if (isFocusEditText(v, hideSoftByEditViewIds())) {
                    if (isTouchView(hideSoftByEditViewIds(), ev)) {
                        return super.dispatchTouchEvent(ev);
                    }
                    //隐藏键盘
                    IMEUtils.hideSoftInput(this);
                    clearViewFocus(v, hideSoftByEditViewIds());
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //是否触摸在指定view上面,对某个控件过滤
    public boolean isTouchView(View[] views, MotionEvent ev) {
        if (views == null || views.length == 0) {

            return false;
        }
        int[] location = new int[2];
        for (View view : views) {
            view.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            if (ev.getRawX() > x && ev.getRawX() < (x + view.getWidth())
                    && ev.getRawY() > y && ev.getRawY() < (y + view.getHeight())) {
                return true;
            }
        }
        return false;
    }

    //是否触摸在指定view上面,对某个控件过滤
    public boolean isTouchView(int[] ids, MotionEvent ev) {
        int[] location = new int[2];
        for (int id : ids) {
            View view = findViewById(id);
            if (view == null) {
                continue;
            }
            view.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            if (ev.getX() > x && ev.getX() < (x + view.getWidth())
                    && ev.getY() > y && ev.getY() < (y + view.getHeight())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否焦点在editText上
     *
     * @param v   焦点所在View
     * @param ids 输入框
     * @return true代表焦点在edit上
     */
    public boolean isFocusEditText(View v, int... ids) {
        if (v instanceof EditText) {
            EditText tmp_et = (EditText) v;
            for (int id : ids) {
                if (tmp_et.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    private View[] mFilterViews;
    private int[] mEditViewIds;

    public void setFilterViews(View[] filterViews) {
        mFilterViews = filterViews;
    }

    public void setEditViewIds(int[] editViewIds) {
        mEditViewIds = editViewIds;
    }

    /**
     * 传入要过滤的View
     * 过滤之后点击将不会有隐藏软键盘的操作
     *
     * @return id 数组
     */
    public View[] filterViewByIds() {
        return mFilterViews;
    }

    /**
     * 传入EditText的Id
     * 没有传入的EditText不做处理
     *
     * @return id 数组
     */
    public int[] hideSoftByEditViewIds() {
        return mEditViewIds;
    }

    /**
     * 清除editText的焦点
     *
     * @param v   焦点所在View
     * @param ids 输入框
     */
    public void clearViewFocus(View v, int... ids) {
        if (null != v && null != ids && ids.length > 0) {
            for (int id : ids) {
                if (v.getId() == id) {
                    v.clearFocus();
                    break;
                }
            }
        }
    }

}
