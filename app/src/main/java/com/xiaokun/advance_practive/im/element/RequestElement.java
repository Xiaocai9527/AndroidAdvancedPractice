package com.xiaokun.advance_practive.im.element;

import org.jivesoftware.smack.packet.ExtensionElement;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/20
 *      描述  ：<request xmlns="urn:xmpp:receipts"></request>
 *      版本  ：1.0
 * </pre>
 */
public class RequestElement implements ExtensionElement {

    private String elementName = "request";
    private String namespace = "\"urn:xmpp:receipts\"";

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getElementName() {
        return elementName;
    }

    @Override
    public CharSequence toXML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<" + elementName + " xmlns=");
        sb.append(namespace + "></" + elementName + ">");
        return sb.toString();
    }
}
