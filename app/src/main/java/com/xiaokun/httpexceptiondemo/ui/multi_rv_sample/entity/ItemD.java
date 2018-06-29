package com.xiaokun.httpexceptiondemo.ui.multi_rv_sample.entity;

import com.xiaokun.httpexceptiondemo.ui.multi_rv_sample.utils.TypeFactory;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/28
 *      描述  ：3张图片的
 *      版本  ：1.0
 * </pre>
 */
public class ItemD implements MultiItem
{

    private String imgUrl1;
    private String imgUrl2;
    private String imgUrl3;

    public ItemD(String imgUrl1, String imgUrl2, String imgUrl3)
    {
        this.imgUrl1 = imgUrl1;
        this.imgUrl2 = imgUrl2;
        this.imgUrl3 = imgUrl3;
    }

    public String getImgUrl1()
    {
        return imgUrl1;
    }

    public void setImgUrl1(String imgUrl1)
    {
        this.imgUrl1 = imgUrl1;
    }

    public String getImgUrl2()
    {
        return imgUrl2;
    }

    public void setImgUrl2(String imgUrl2)
    {
        this.imgUrl2 = imgUrl2;
    }

    public String getImgUrl3()
    {
        return imgUrl3;
    }

    public void setImgUrl3(String imgUrl3)
    {
        this.imgUrl3 = imgUrl3;
    }

    @Override
    public int getItemType(TypeFactory typeFactory)
    {
        return typeFactory.type(this);
    }
}
