package com.xiaokun.advance_practive.ui.multi_rv_sample.entity;

import com.xiaokun.advance_practive.ui.multi_rv_sample.holder.TypeCHolder;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/28
 *      描述  ：带图片的
 *      版本  ：1.0
 * </pre>
 */
public class ItemC implements MultiItem
{

    private String detail;
    private String author;
    private String imgUrl;

    public ItemC(String detail, String author, String imgUrl)
    {
        this.detail = detail;
        this.author = author;
        this.imgUrl = imgUrl;
    }

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    @Override
    public int getItemType() {
        return TypeCHolder.LAYOUT;
    }
}
