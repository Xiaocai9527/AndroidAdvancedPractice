package com.xiaokun.advance_practive.ui.big_mvp.detail;

import android.webkit.WebView;

import com.xiaokun.advance_practive.ui.big_mvp.BasePresenter;
import com.xiaokun.advance_practive.ui.big_mvp.BaseView;

/**
 * Created by 肖坤 on 2018/6/3.
 *
 * @author 肖坤
 * @date 2018/6/3
 */

public interface DetailContract
{
    interface View extends BaseView<Presenter>
    {
        WebView getWebview();

        void onPageStart();

        void onPageFinished();
    }

    interface Presenter extends BasePresenter
    {
        void initWebview();

        void loadWebview(String url);
    }
}
