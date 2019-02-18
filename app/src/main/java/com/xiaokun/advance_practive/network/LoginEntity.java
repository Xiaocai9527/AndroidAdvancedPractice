package com.xiaokun.advance_practive.network;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/24
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class LoginEntity
{


    /**
     * Data : {"login":"登录成功"}
     * Code : 1
     * PdMessage : ok
     */

    private DataBean Data;
    private int Code;
    private String Message;

    public DataBean getData()
    {
        return Data;
    }

    public void setData(DataBean Data)
    {
        this.Data = Data;
    }

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

    public static class DataBean
    {
        /**
         * login : 登录成功
         */

        private String login;

        public String getLogin()
        {
            return login;
        }

        public void setLogin(String login)
        {
            this.login = login;
        }
    }
}
