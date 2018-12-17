package com.xiaokun.advance_practive.network.entity;

import java.util.List;

/**
 * Created by 肖坤 on 2018/6/3.
 *
 * @author 肖坤
 * @date 2018/6/3
 */

public class ListResEntity
{

    /**
     * error : false
     * results : [{"_id":"5b0d5e31421aa97f00f67c78","createdAt":"2018-06-01T11:15:00.494Z","desc":"适用于Android的简单图像裁剪库。","images":["http://img.gank.io/b8f56f47-4462-47b1-9579-bccc2fdf0870","http://img.gank.io/56df4e32-ef0e-407f-a01c-6e57a38ec063","http://img.gank.io/ccc60d71-e415-4a8a-a5d4-8b71d939cbee","http://img.gank.io/d033401d-87b8-4a83-9e4a-248ffaffbb28"],"publishedAt":"2018-06-01T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/igreenwood/SimpleCropView","used":true,"who":"lijnshanmx"},{"_id":"5b0f68d8421aa924d7b24cb8","createdAt":"2018-05-31T11:15:36.786Z","desc":"Android平台中对页面、原生路由功能的中间件.","images":["http://img.gank.io/3ada724f-39fc-44dc-8445-a19c4717f1b7"],"publishedAt":"2018-06-01T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/wenzhonghu/MyRouter","used":true,"who":"lijinshanmx"}]
     */

    private boolean error;
    private List<ResultsBean> results;

    public boolean isError()
    {
        return error;
    }

    public void setError(boolean error)
    {
        this.error = error;
    }

    public List<ResultsBean> getResults()
    {
        return results;
    }

    public void setResults(List<ResultsBean> results)
    {
        this.results = results;
    }

    public static class ResultsBean
    {
        /**
         * _id : 5b0d5e31421aa97f00f67c78
         * createdAt : 2018-06-01T11:15:00.494Z
         * desc : 适用于Android的简单图像裁剪库。
         * images : ["http://img.gank.io/b8f56f47-4462-47b1-9579-bccc2fdf0870","http://img.gank.io/56df4e32-ef0e-407f-a01c-6e57a38ec063","http://img.gank.io/ccc60d71-e415-4a8a-a5d4-8b71d939cbee","http://img.gank.io/d033401d-87b8-4a83-9e4a-248ffaffbb28"]
         * publishedAt : 2018-06-01T00:00:00.0Z
         * source : chrome
         * type : Android
         * url : https://github.com/igreenwood/SimpleCropView
         * used : true
         * who : lijnshanmx
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String get_id()
        {
            return _id;
        }

        public void set_id(String _id)
        {
            this._id = _id;
        }

        public String getCreatedAt()
        {
            return createdAt;
        }

        public void setCreatedAt(String createdAt)
        {
            this.createdAt = createdAt;
        }

        public String getDesc()
        {
            return desc;
        }

        public void setDesc(String desc)
        {
            this.desc = desc;
        }

        public String getPublishedAt()
        {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt)
        {
            this.publishedAt = publishedAt;
        }

        public String getSource()
        {
            return source;
        }

        public void setSource(String source)
        {
            this.source = source;
        }

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }

        public String getUrl()
        {
            return url;
        }

        public void setUrl(String url)
        {
            this.url = url;
        }

        public boolean isUsed()
        {
            return used;
        }

        public void setUsed(boolean used)
        {
            this.used = used;
        }

        public String getWho()
        {
            return who;
        }

        public void setWho(String who)
        {
            this.who = who;
        }

        public List<String> getImages()
        {
            return images;
        }

        public void setImages(List<String> images)
        {
            this.images = images;
        }
    }
}
