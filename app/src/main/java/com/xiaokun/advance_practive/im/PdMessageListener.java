package com.xiaokun.advance_practive.im;

import com.xiaokun.advance_practive.im.database.bean.PdMessage;

import java.util.List;

/**
 * Created by 肖坤 on 2019/2/19.
 *
 * @author 肖坤
 * @date 2019/2/19
 */

public interface PdMessageListener {

    void onMessageReceived(List<PdMessage> var1);

    void onMessageRead(List<PdMessage> var1);

    void onMessageDelivered(List<PdMessage> var1);

    void onMessageRecalled(List<PdMessage> var1);

    void onMessageChanged(PdMessage pdMessage, Object change);

}
