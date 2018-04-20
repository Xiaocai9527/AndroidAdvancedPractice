package com.xiaokun.httpexceptiondemo.network;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/20
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class TestEntity
{

    /**
     * code : 1
     * error : null
     * msg : 登录成功
     * detail : {"autoId":24,"userId":"e8711757-eb98-4cb0-ac0c-4665de466e94","mobile":"13297945028","userName":null,"userPwd":null,"cardId":null,"sex":null,"birthday":null,"address":null,"isValid":"1","isDelete":null,"createTime":1524210705000,"modifyTime":1524210705000}
     * retain : null
     */

    private int code;
    private Object error;
    private String msg;
    private DetailBean detail;
    private Object retain;

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public Object getError()
    {
        return error;
    }

    public void setError(Object error)
    {
        this.error = error;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public DetailBean getDetail()
    {
        return detail;
    }

    public void setDetail(DetailBean detail)
    {
        this.detail = detail;
    }

    public Object getRetain()
    {
        return retain;
    }

    public void setRetain(Object retain)
    {
        this.retain = retain;
    }

    public static class DetailBean
    {
        /**
         * autoId : 24
         * userId : e8711757-eb98-4cb0-ac0c-4665de466e94
         * mobile : 13297945028
         * userName : null
         * userPwd : null
         * cardId : null
         * sex : null
         * birthday : null
         * address : null
         * isValid : 1
         * isDelete : null
         * createTime : 1524210705000
         * modifyTime : 1524210705000
         */

        private int autoId;
        private String userId;
        private String mobile;
        private Object userName;
        private Object userPwd;
        private Object cardId;
        private Object sex;
        private Object birthday;
        private Object address;
        private String isValid;
        private Object isDelete;
        private long createTime;
        private long modifyTime;

        public int getAutoId()
        {
            return autoId;
        }

        public void setAutoId(int autoId)
        {
            this.autoId = autoId;
        }

        public String getUserId()
        {
            return userId;
        }

        public void setUserId(String userId)
        {
            this.userId = userId;
        }

        public String getMobile()
        {
            return mobile;
        }

        public void setMobile(String mobile)
        {
            this.mobile = mobile;
        }

        public Object getUserName()
        {
            return userName;
        }

        public void setUserName(Object userName)
        {
            this.userName = userName;
        }

        public Object getUserPwd()
        {
            return userPwd;
        }

        public void setUserPwd(Object userPwd)
        {
            this.userPwd = userPwd;
        }

        public Object getCardId()
        {
            return cardId;
        }

        public void setCardId(Object cardId)
        {
            this.cardId = cardId;
        }

        public Object getSex()
        {
            return sex;
        }

        public void setSex(Object sex)
        {
            this.sex = sex;
        }

        public Object getBirthday()
        {
            return birthday;
        }

        public void setBirthday(Object birthday)
        {
            this.birthday = birthday;
        }

        public Object getAddress()
        {
            return address;
        }

        public void setAddress(Object address)
        {
            this.address = address;
        }

        public String getIsValid()
        {
            return isValid;
        }

        public void setIsValid(String isValid)
        {
            this.isValid = isValid;
        }

        public Object getIsDelete()
        {
            return isDelete;
        }

        public void setIsDelete(Object isDelete)
        {
            this.isDelete = isDelete;
        }

        public long getCreateTime()
        {
            return createTime;
        }

        public void setCreateTime(long createTime)
        {
            this.createTime = createTime;
        }

        public long getModifyTime()
        {
            return modifyTime;
        }

        public void setModifyTime(long modifyTime)
        {
            this.modifyTime = modifyTime;
        }
    }
}
