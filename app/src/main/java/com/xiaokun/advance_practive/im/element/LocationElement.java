package com.xiaokun.advance_practive.im.element;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/20
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class LocationElement extends BasePeidouElement {

    //<mobileChat msgType="location" content="经度,纬度" property="位置详细描述"  other=’baidu’>
    //		</ mobileChat >

    //经度纬度
    private String content = "";
    //位置详细描述
    private String property = "";
    //百度还是高德
    private String other = "";

    public void setContent(String content) {
        this.content = content;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getContent() {
        return content;
    }

    public String getProperty() {
        return property;
    }

    public String getOther() {
        return other;
    }

    @Override
    public CharSequence toXML() {
        sb.append(getBeforeCommonXml());
        sb.append(" url=" + escape + content + escape + "property=" + escape + property + escape + lastXml);
        return sb.toString();
    }

    @Override
    public String getMsgType() {
        return "location";
    }

}
