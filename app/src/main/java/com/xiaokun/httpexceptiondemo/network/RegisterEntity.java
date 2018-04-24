package com.xiaokun.httpexceptiondemo.network;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/24
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class RegisterEntity
{

    /**
     * Data : {"register":"注册成功"}
     * Code : 1
     * Message : ok
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
         * register : 注册成功
         */

        private String register;

        public String getRegister()
        {
            return register;
        }

        public void setRegister(String register)
        {
            this.register = register;
        }
    }
}
