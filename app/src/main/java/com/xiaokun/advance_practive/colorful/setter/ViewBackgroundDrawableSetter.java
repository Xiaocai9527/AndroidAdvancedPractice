package com.xiaokun.advance_practive.colorful.setter;

import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;


/**
 * Created by 肖坤 on 2017/9/12.
 * 引自https://github.com/hehonghui/Colorful 何辉红大神！
 * 公司：依迅北斗
 * 邮箱：838494268@qq.com
 */

public class ViewBackgroundDrawableSetter extends ViewSetter
{
    public ViewBackgroundDrawableSetter(View targetView, int resId)
    {
        super(targetView, resId);
    }


    public ViewBackgroundDrawableSetter(int viewId, int resId)
    {
        super(viewId, resId);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setValue(Theme newTheme, int themeId)
    {
        if (mView == null)
        {
            return;
        }
        TypedArray a = newTheme.obtainStyledAttributes(themeId,
                new int[]{mAttrResId});
        int attributeResourceId = a.getResourceId(0, 0);
        Drawable drawable = mView.getResources().getDrawable(
                attributeResourceId);
        a.recycle();
        mView.setBackgroundDrawable(drawable);
    }
}
