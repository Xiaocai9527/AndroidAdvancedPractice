package com.xiaokun.advance_practive.colorful.setter;

import android.content.res.Resources.Theme;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by xiaokun on 2017/9/12.
 * 引自https://github.com/hehonghui/Colorful 何辉红大神！
 */

public class TextColorSetter extends ViewSetter
{
    public TextColorSetter(TextView textView, int resId)
    {
        super(textView, resId);
    }

    public TextColorSetter(int viewId, int resId)
    {
        super(viewId, resId);
    }

    @Override
    public void setValue(Theme newTheme, int themeId)
    {
        if (mView == null)
        {
            return;
        }
        Log.e("color123", "setValue(TextColorSetter.java:29)");
        ((TextView) mView).setTextColor(getColor(newTheme));
    }
}
