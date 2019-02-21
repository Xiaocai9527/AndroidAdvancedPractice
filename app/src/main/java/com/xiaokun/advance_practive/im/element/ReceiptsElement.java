package com.xiaokun.advance_practive.im.element;

import org.jivesoftware.smack.packet.ExtensionElement;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/21
 *      描述  ： <mobilePeidou><receipts msgid='dRM46-21' type='chat'/></mobilePeidou>
 *      版本  ：1.0
 * </pre>
 */
public class ReceiptsElement extends BasePeidouElement {

    public static final String ELEMENT_NAME = "receipts";

    private String msgId;

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgId() {
        return msgId;
    }

    @Override
    public CharSequence toXML() {
        sb.append("<" + elementName + " xmlns=" + "\"" + namespace + "\"" + ">");
        sb.append("<receipts " + " type=" + escape + getMsgType() + escape + " msgid=" + escape + msgId + escape + "/></" + elementName);
        return sb.toString();
    }

    @Override
    public String getMsgType() {
        return "chat";
    }
}
