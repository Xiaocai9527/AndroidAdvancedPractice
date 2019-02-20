package com.xiaokun.advance_practive.im.element;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/20
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class ImgElement extends BasePeidouElement {

    //<mobileChat msgType="img" url="http://domain/chat/11.jpg"
    //			property="http://domain/chat/11.jpg(缩略图地址)">
    //		</ mobileChat >

    private String url = "";
    //缩略图地址
    private String property = "";

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public CharSequence toXML() {
        sb.append(getBeforeCommonXml());
        sb.append(" url=" + escape + url + escape + "property=" + escape + property + escape + lastXml);
        return sb.toString();
    }

    @Override
    public String getMsgType() {
        return "img";
    }

}
