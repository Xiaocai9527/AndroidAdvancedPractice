package com.xiaokun.httpexceptiondemo.network.wanAndroid;

import java.util.List;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/06
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class TotalResEntity {


    /**
     * 注册返回实体
     */
    public static class Register {

        public String email;
        public String icon;
        public int id;
        public String password;
        public String token;
        public int type;
        public String username;
        public List<String> chapterTops;
        public List<Integer> collectIds;
    }

    /**
     * 登录返回实体
     */
    public static class Login {

        public String email;
        public String icon;
        public int id;
        public String password;
        public String token;
        public int type;
        public String username;
        public List<String> chapterTops;
        public List<Integer> collectIds;
    }


    /**
     * 首页请求返回实体
     */
    public static class HomeArticles {

        public int curPage;
        public int offset;
        public boolean over;
        public int pageCount;
        public int size;
        public int total;
        public List<DatasBean> datas;

        public static class DatasBean {
            public String apkLink;
            public String author;
            public int chapterId;
            public String chapterName;
            public boolean collect;
            public int courseId;
            public String desc;
            public String envelopePic;
            public boolean fresh;
            public int id;
            public String link;
            public String niceDate;
            public String origin;
            public String projectLink;
            public long publishTime;
            public int superChapterId;
            public String superChapterName;
            public String title;
            public int type;
            public int userId;
            public int visible;
            public int zan;
            public List<TagsBean> tags;

            public static class TagsBean {
                public String name;
                public String url;
            }
        }
    }

    /**
     * 首页banner返回实体
     */
    public static class HomeBanner {
        public String desc;
        public int id;
        public String imagePath;
        public int isVisible;
        public int order;
        public String title;
        public int type;
        public String url;
    }

    /**
     * 常用网站
     */
    public static class CommonWebsite {

        public String icon;
        public int id;
        public String link;
        public String name;
        public int order;
        public int visible;
    }

    /**
     * 搜索热词
     */
    public static class SearchHotWords {

        public int id;
        public String link;
        public String name;
        public int order;
        public int visible;
    }

    /**
     * 体系数据
     */
    public static class SystemData {

        public int courseId;
        public int id;
        public String name;
        public int order;
        public int parentChapterId;
        public boolean userControlSetTop;
        public int visible;
        public List<ChildrenBean> children;

        public static class ChildrenBean {
            public int courseId;
            public int id;
            public String name;
            public int order;
            public int parentChapterId;
            public boolean userControlSetTop;
            public int visible;
            public List<?> children;
        }
    }

    /**
     * 收藏
     */
    public static class Collect {

        public int curPage;
        public int offset;
        public boolean over;
        public int pageCount;
        public int size;
        public int total;
        public List<DatasBean> datas;

        public static class DatasBean {
            public String author;
            public int chapterId;
            public String chapterName;
            public int courseId;
            public String desc;
            public String envelopePic;
            public int id;
            public String link;
            public String niceDate;
            public String origin;
            public int originId;
            public long publishTime;
            public String title;
            public int userId;
            public int visible;
            public int zan;
        }
    }


}
