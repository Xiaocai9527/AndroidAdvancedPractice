package com.xiaokun.httpexceptiondemo.network;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/19
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public class ResEntity1
{


    /**
     * Data : {"res":"返回成功 "}
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
         * res : 返回成功
         */

        private String res;

        public String getRes()
        {
            return res;
        }

        public void setRes(String res)
        {
            this.res = res;
        }
    }

    @Override
    public String toString()
    {
        return "ResEntity1{" +
                "Data=" + Data +
                ", Code=" + Code +
                ", Message='" + Message + '\'' +
                '}';
    }
}
