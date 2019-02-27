package com.xiaokun.advance_practive.im.element;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.provider.EmbeddedExtensionProvider;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smackx.json.packet.AbstractJsonPacketExtension;
import org.jivesoftware.smackx.json.provider.AbstractJsonExtensionProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/20
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class TextElement extends BasePeidouElement {

    //<mobilePeidou  xmlns="domain">
//      <mobileChat msgtype="text" content="呃呃呃"></mobileChat>
// </mobilePeidou>

    private String content = "";

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public CharSequence toXML() {
        sb = new StringBuilder();
        sb.append(getBeforeCommonXml());
        sb.append(" content=" + escape + content + escape + lastXml);
        return sb.toString();
    }

    @Override
    public String getMsgType() {
        return "text";
    }

////    public static class Provider extends EmbeddedExtensionProvider<TextElement> {
////
////        @Override
////        protected TextElement createReturnExtension(String elementName, String namespace, Map<String, String> map, List<? extends ExtensionElement> list) {
////            TextElement textElement = new TextElement();
////            textElement.setContent(map.get("content"));
////            textElement.setNamespace(namespace);
////            return textElement;
////        }
////    }
//
////    public static class Provider extends AbstractJsonExtensionProvider {
////
////        @Override
////        public AbstractJsonPacketExtension from(String s) {
////            return null;
////        }
////    }
//
//    public static class Provider extends ExtensionElementProvider<TextElement> {
//        @Override
//        public TextElement parse(XmlPullParser xmlPullParser, int i) throws XmlPullParserException, IOException, SmackException {
//            TextElement textElement = new TextElement();
//            textElement.setContent("测试一波");
//            return textElement;
//        }
//
//    }

}
