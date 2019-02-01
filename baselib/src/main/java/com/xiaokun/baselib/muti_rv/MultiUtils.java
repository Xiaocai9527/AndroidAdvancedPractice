package com.xiaokun.baselib.muti_rv;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/01/31
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class MultiUtils {


    public static <T> List<MultiItem> transforToMulItem(List<T> list) {
        List<MultiItem> multiItems = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            if (list.get(0) instanceof MultiItem) {
                for (T t : list) {
                    multiItems.add((MultiItem) t);
                }
            }
        }
        return multiItems;
    }


}
