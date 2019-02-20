package com.xiaokun.advance_practive.im.database.bean.msgBody;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/18
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class PdLocationMsgBody extends PdMsgBody {
    //经度纬度
    public String content;
    //位置详细描述
    public String locationDetail;
    //定位类型,高德/百度
    public String mapType;
}
