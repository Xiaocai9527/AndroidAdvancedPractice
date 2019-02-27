package com.xiaokun.advance_practive.im.element;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/20
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class VideoElement extends BasePeidouElement {

    //<mobileChat msgType="video" url="http://domain/chat/11.amr"
    //			property="缩略图">
    //		</ mobileChat >

    private String url = "";
    //缩略图
    private String property = "";

    public void setUrl(String url) {
        this.url = url;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getUrl() {
        return url;
    }

    public String getProperty() {
        return property;
    }

    @Override
    public CharSequence toXML() {
        sb = new StringBuilder();
        sb.append(getBeforeCommonXml());
        sb.append(" url=" + escape + url + escape + "property=" + escape + property + escape + lastXml);
        return sb.toString();
    }

    @Override
    public String getMsgType() {
        return "video";
    }

}
