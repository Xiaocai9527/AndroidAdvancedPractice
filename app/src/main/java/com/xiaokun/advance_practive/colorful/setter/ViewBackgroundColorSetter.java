package com.xiaokun.advance_practive.colorful.setter;

import android.content.res.Resources.Theme;
import android.util.Log;
import android.view.View;

/**
 * Created by 肖坤 on 2017/9/12.
 * 引自https://github.com/hehonghui/Colorful 何辉红大神！
 * 公司：依迅北斗
 * 邮箱：838494268@qq.com
 */

public class ViewBackgroundColorSetter extends ViewSetter
{
    public ViewBackgroundColorSetter(View target, int resId)
    {
        super(target, resId);
    }

    public ViewBackgroundColorSetter(int viewId, int resId)
    {
        super(viewId, resId);
    }

    @Override
    public void setValue(Theme newTheme, int themeId)
    {
        if (mView != null)
        {
            Log.e("color123","setValue(ViewBackgroundColorSetter.java:29)");
            mView.setBackgroundColor(getColor(newTheme));
        }
    }
}
