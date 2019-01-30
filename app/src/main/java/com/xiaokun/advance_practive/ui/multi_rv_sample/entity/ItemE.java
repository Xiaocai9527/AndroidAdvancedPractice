package com.xiaokun.advance_practive.ui.multi_rv_sample.entity;

import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.TypeEHolder;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/29
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class ItemE implements MultiItem {

    private String imgUrl;

    public ItemE(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int getItemType() {
        return TypeEHolder.LAYOUT;
    }
}
