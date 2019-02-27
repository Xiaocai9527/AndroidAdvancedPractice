package com.xiaokun.advance_practive.im.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.im.adapter.holder.BaseMsgHolder;
import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdTextMsgBody;
import com.xiaokun.advance_practive.im.entity.Message;
import com.xiaokun.advance_practive.im.util.IMEUtils;
import com.xiaokun.baselib.muti_rv.BaseMultiHodler;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/25
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class TextHolder extends BaseMsgHolder {

    public TextHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Message message) {
        setText(R.id.tv_text_content, ((PdTextMsgBody) message.pdMsgBody).content);
        Glide.with(mContext).load(message.avatarUrl).apply(RequestOptions.bitmapTransform(new CircleCrop())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher))
                .into((ImageView) getView(R.id.iv_avatar));
        getView(R.id.tv_text_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, ((TextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        setVisible(R.id.pb_status, message.msgStatus == PdMessage.PDMessageStatus.DELIVERING);
        if (message.msgStatus == PdMessage.PDMessageStatus.FAIL) {
            setVisible(R.id.iv_send_failed, true);
        }
    }
}
