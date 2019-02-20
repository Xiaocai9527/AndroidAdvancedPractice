package com.xiaokun.advance_practive.im.element;

import org.jivesoftware.smack.packet.ExtensionElement;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/20
 *      描述  ：<mobilePeidou  xmlns="domain">
 *                  <mobileChat msgtype="text" content="呃呃呃"></mobileChat>
 *             </mobilePeidou>
 *      版本  ：1.0
 * </pre>
 */
public abstract class BasePeidouElement implements ExtensionElement {

    private String elementName = "mobilePeidou";
    private String chatElementName = "mobileChat";
    private String namespace = "";

    protected String escape = "\"";

    protected String lastXml = "></mobileChat></mobilePeidou>";

    /**
     * 域名
     *
     * @param namespace
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getNamespace() {
        return elementName;
    }

    @Override
    public String getElementName() {
        return namespace;
    }


    protected StringBuilder sb = new StringBuilder();

    @Override
    public abstract CharSequence toXML();

    protected String getBeforeCommonXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<" + elementName + " xmlns=" + "\"" + namespace + "\"" + ">");
        sb.append("<" + chatElementName + " msgtype=" + "\"" + getMsgType() + "\"");
        return sb.toString();
    }

    public abstract String getMsgType();

}
