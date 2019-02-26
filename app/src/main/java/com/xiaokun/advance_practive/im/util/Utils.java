package com.xiaokun.advance_practive.im.util;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaokun.advance_practive.im.entity.Message;
import com.xiaokun.baselib.muti_rv.MultiItem;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/22
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class Utils {

    public static View createView(@LayoutRes int layoutId, ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
    }

    public static <T> List<MultiItem> transferList(List<T> list) {
        if (list == null) {
            return null;
        }
        List<MultiItem> items = new ArrayList<>();

        for (T t : list) {
            MultiItem item = (MultiItem) t;
            items.add(item);
        }
        return items;
    }

    public static List<Message> transferMultiItem(List<MultiItem> multiItems) {
        if (multiItems == null) {
            return null;
        }
        List<Message> messages = new ArrayList<>();

        for (MultiItem multiItem : multiItems) {
            messages.add(((Message) multiItem));
        }
        return messages;
    }

}
