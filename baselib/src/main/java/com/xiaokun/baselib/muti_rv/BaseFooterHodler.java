package com.xiaokun.baselib.muti_rv;

import android.view.View;

import com.xiaokun.baselib.R;

import static com.xiaokun.baselib.muti_rv.MultiAdapter.LOADING;
import static com.xiaokun.baselib.muti_rv.MultiAdapter.LOAD_COMPLETE;
import static com.xiaokun.baselib.muti_rv.MultiAdapter.LOAD_FAILED;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/01/31
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public abstract class BaseFooterHodler extends BaseMultiHodler<MultiItem> {
    public static int TYPE_FOOTER = R.layout.footer_layout;

    public BaseFooterHodler(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(MultiItem multiItem) {
        //do nothing
    }

    public void bindFooterHolder(int currentState) {
        switch (currentState) {
            case LOADING:
                showLoad();
                break;
            case LOAD_COMPLETE:
                showComplete();
                break;
            case LOAD_FAILED:
                showFailed();
                break;
            default:

                break;
        }
    }

    public abstract void showLoad();

    public abstract void showComplete();

    public abstract void showFailed();

    public abstract int getLayoutRes();

}
