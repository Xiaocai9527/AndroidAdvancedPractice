package com.xiaokun.advance_practive;

import com.xiaokun.baselib.util.RefInvoke;
import com.xiaokun.baselib.util.ReflectionHelpers;

import org.junit.Test;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/01/24
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class ViewModelUtils {


    @Test
    public void generateVmCode() {
        Object object = RefInvoke.createObject("AndroidStudioProjects.app.PDYongMaAndroid.library.yongma.src.main.java.com.peidou.yongma.ui.repayment.viewmodel.RequestRefundActivityViewModel");

        System.out.println(object.toString());
    }

}
