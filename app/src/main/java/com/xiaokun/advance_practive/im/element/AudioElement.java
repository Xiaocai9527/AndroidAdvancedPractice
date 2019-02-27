package com.xiaokun.advance_practive.im.element;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/20
 *      描述  ：语音
 *      版本  ：1.0
 * </pre>
 */
public class AudioElement extends BasePeidouElement {

    //<mobileChat msgType="audio" url="http://domain/chat/11.amr"
    //			property="120">
    //		</ mobileChat >

    private String url = "";
    //语音时长
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
        return "audio";
    }

}
