package com.xiaokun.advance_practive.im;

import com.xiaokun.advance_practive.im.database.bean.PdMessage;

/**
 * Created by 肖坤 on 2019/2/19.
 *
 * @author 肖坤
 * @date 2019/2/19
 */

public interface PdMessageListener {

    void onMessageReceived(PdMessage pdMessage);

    /**
     * 回执消息,表示发送的消息成功送达
     *
     * @param msgId
     */
    void onReceiptsMessageReceived(String msgId);

    /**
     * 发送失败消息回调,此回调只在会话窗口有效
     *
     * @param pdMessage
     */
    void onFailedMessageReceived(PdMessage pdMessage);
}
