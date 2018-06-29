package com.xiaokun.httpexceptiondemo.ui.multi_rv_sample.entity;

import com.xiaokun.httpexceptiondemo.ui.multi_rv_sample.utils.TypeFactory;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/27
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class ItemB implements MultiItem
{
    private String detail;
    private String author;
    private String date;

    public ItemB(String detail, String author, String date)
    {
        this.detail = detail;
        this.author = author;
        this.date = date;
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

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    @Override
    public int getItemType(TypeFactory typeFactory)
    {
        return typeFactory.type(this);
    }
}
