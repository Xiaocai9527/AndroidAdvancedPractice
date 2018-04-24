package com.xiaokun.httpexceptiondemo.network.wanAndroid;

import java.util.List;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/24
 *     描述   : 玩Android的登录
 *     版本   : 1.0
 * </pre>
 */
public class WanLoginEntityRes
{

    /**
     * data : {"collectIds":[1349,1675,1701,1702,2228,1496,2411,2187,2439],"email":"","icon":"","id":440,"password":"xk939291","type":0,"username":"13886149842"}
     * errorCode : 0
     * errorMsg :
     */

    private DataBean data;
    private int errorCode;
    private String errorMsg;

    public DataBean getData()
    {
        return data;
    }

    public void setData(DataBean data)
    {
        this.data = data;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public static class DataBean
    {
        /**
         * collectIds : [1349,1675,1701,1702,2228,1496,2411,2187,2439]
         * email :
         * icon :
         * id : 440
         * password : xk939291
         * type : 0
         * username : 13886149842
         */

        private String email;
        private String icon;
        private int id;
        private String password;
        private int type;
        private String username;
        private List<Integer> collectIds;

        public String getEmail()
        {
            return email;
        }

        public void setEmail(String email)
        {
            this.email = email;
        }

        public String getIcon()
        {
            return icon;
        }

        public void setIcon(String icon)
        {
            this.icon = icon;
        }

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }

        public int getType()
        {
            return type;
        }

        public void setType(int type)
        {
            this.type = type;
        }

        public String getUsername()
        {
            return username;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }

        public List<Integer> getCollectIds()
        {
            return collectIds;
        }

        public void setCollectIds(List<Integer> collectIds)
        {
            this.collectIds = collectIds;
        }
    }
}
