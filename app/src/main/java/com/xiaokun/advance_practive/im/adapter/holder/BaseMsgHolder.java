package com.xiaokun.advance_practive.im.adapter.holder;

import android.view.View;

import com.xiaokun.advance_practive.im.entity.Message;
import com.xiaokun.advance_practive.im.util.IMEUtils;
import com.xiaokun.baselib.muti_rv.BaseMultiHodler;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/27
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class BaseMsgHolder extends BaseMultiHodler<Message> {

    public BaseMsgHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMEUtils.hideSoftInput(v);
            }
        });
    }

    @Override
    public void bind(Message multiItem) {

    }
}
