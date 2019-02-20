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
}
