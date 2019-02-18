package com.xiaokun.advance_practive.network.entity;

import java.util.List;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/03
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class XmNeswResEntity
{

    /**
     * Data : [{"title":"小米向港交所申请上市：估值700亿美元","date":"2018-05-03 10:51:05","source":"观察者网"},{"title":"小米向港交所申请上市：估值700亿美元","date":"2018-05-03 10:51:05","source":"观察者网"},{"title":"小米向港交所申请上市：估值700亿美元","date":"2018-05-03 10:51:05","source":"观察者网"},{"title":"小米向港交所申请上市：估值700亿美元","date":"2018-05-03 10:51:05","source":"观察者网"},{"title":"小米向港交所申请上市：估值700亿美元","date":"2018-05-03 10:51:05","source":"观察者网"},{"title":"小米向港交所申请上市：估值700亿美元","date":"2018-05-03 10:51:05","source":"观察者网"},{"title":"小米向港交所申请上市：估值700亿美元","date":"2018-05-03 10:51:05","source":"观察者网"},{"title":"小米向港交所申请上市：估值700亿美元","date":"2018-05-03 10:51:05","source":"观察者网"},{"title":"小米向港交所申请上市：估值700亿美元","date":"2018-05-03 10:51:05","source":"观察者网"},{"title":"小米向港交所申请上市：估值700亿美元","date":"2018-05-03 10:51:05","source":"观察者网"}]
     * Code : 1
     * PdMessage : ok
     */

    private int Code;
    private String Message;
    private List<DataBean> Data;

    public int getCode()
    {
        return Code;
    }

    public void setCode(int Code)
    {
        this.Code = Code;
    }

    public String getMessage()
    {
        return Message;
    }

    public void setMessage(String Message)
    {
        this.Message = Message;
    }

    public List<DataBean> getData()
    {
        return Data;
    }

    public void setData(List<DataBean> Data)
    {
        this.Data = Data;
    }

    public static class DataBean implements UniversalResEntity
    {
        /**
         * title : 小米向港交所申请上市：估值700亿美元
         * date : 2018-05-03 10:51:05
         * source : 观察者网
         */

        private String title;
        private String date;
        private String source;

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getDate()
        {
            return date;
        }

        public void setDate(String date)
        {
            this.date = date;
        }

        public String getSource()
        {
            return source;
        }

        public void setSource(String source)
        {
            this.source = source;
        }

        @Override
        public String getText1()
        {
            return title;
        }

        @Override
        public String getText2()
        {
            return date;
        }

        @Override
        public String getText3()
        {
            return source;
        }
    }
}
