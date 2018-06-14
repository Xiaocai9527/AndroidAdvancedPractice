package com.xiaokun.httpexceptiondemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;


/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/12
 *      描述  ：滑动测试view
 *      版本  ：1.0
 * </pre>
 */
public class ScrollTestView extends View
{
    private Context mContext;
    private Scroller mScroller;

    public ScrollTestView(Context context)
    {
        this(context, null);
    }

    public ScrollTestView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        mScroller = new Scroller(mContext);
    }

    private void smoothScrollTo(int destX, int destY)
    {
        //@return The left edge of the displayed part of your view, in pixels.
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        //1000ms内滑向destX，效果就是慢慢滑动
        mScroller.startScroll(scrollX, 0, delta, 0, 1000);
        invalidate();
    }

    @Override
    public void computeScroll()
    {
        //view类此方法是控方法
        super.computeScroll();
        //Call this when you want to know the new location.  If it returns true,
        //the animation is not yet finished.
        if (mScroller.computeScrollOffset())
        {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //子线程调用
            postInvalidate();
        }
    }
}
