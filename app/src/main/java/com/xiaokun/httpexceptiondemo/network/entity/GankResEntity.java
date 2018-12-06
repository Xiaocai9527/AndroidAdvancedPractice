package com.xiaokun.httpexceptiondemo.network.entity;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/03
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class GankResEntity implements Serializable {

    /**
     * Data : [{"desc":"MusicLibrary-一个丰富的音频播放SDK","publishedAt":"2018-03-12T08:44:50.326Z","who":"lizixian"},{"desc":"MusicLibrary-一个丰富的音频播放SDK","publishedAt":"2018-03-12T08:44:50.326Z","who":"lizixian"},{"desc":"MusicLibrary-一个丰富的音频播放SDK","publishedAt":"2018-03-12T08:44:50.326Z","who":"lizixian"},{"desc":"MusicLibrary-一个丰富的音频播放SDK","publishedAt":"2018-03-12T08:44:50.326Z","who":"lizixian"},{"desc":"MusicLibrary-一个丰富的音频播放SDK","publishedAt":"2018-03-12T08:44:50.326Z","who":"lizixian"},{"desc":"MusicLibrary-一个丰富的音频播放SDK","publishedAt":"2018-03-12T08:44:50.326Z","who":"lizixian"},{"desc":"MusicLibrary-一个丰富的音频播放SDK","publishedAt":"2018-03-12T08:44:50.326Z","who":"lizixian"},{"desc":"MusicLibrary-一个丰富的音频播放SDK","publishedAt":"2018-03-12T08:44:50.326Z","who":"lizixian"},{"desc":"MusicLibrary-一个丰富的音频播放SDK","publishedAt":"2018-03-12T08:44:50.326Z","who":"lizixian"},{"desc":"MusicLibrary-一个丰富的音频播放SDK","publishedAt":"2018-03-12T08:44:50.326Z","who":"lizixian"}]
     * Code : 1
     * Message : ok
     */

    private int Code;
    private String Message;
    private List<DataBean> Data;

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean implements UniversalResEntity, Serializable {
        /**
         * desc : MusicLibrary-一个丰富的音频播放SDK
         * publishedAt : 2018-03-12T08:44:50.326Z
         * who : lizixian
         */

        private String desc;
        private String publishedAt;
        private String who;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        @Override
        public String getText1() {
            return desc;
        }

        @Override
        public String getText2() {
            return publishedAt;
        }

        @Override
        public String getText3() {
            return who;
        }
    }
}
