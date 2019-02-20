package com.xiaokun.advance_practive.im;

import com.xiaokun.advance_practive.im.element.AudioElement;
import com.xiaokun.advance_practive.im.element.BasePeidouElement;
import com.xiaokun.advance_practive.im.element.ImgElement;
import com.xiaokun.advance_practive.im.element.TextElement;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/20
 *      描述  ：<mobilePeidou  xmlns="domain">
 *                  <mobileChat msgtype="text" content="呃呃呃"></mobileChat>
 *              </mobilePeidou>
 *      版本  ：1.0
 * </pre>
 */
public class Provider extends ExtensionElementProvider<BasePeidouElement> {
    @Override
    public BasePeidouElement parse(XmlPullParser parser, int depth) throws XmlPullParserException, IOException, SmackException {
        BasePeidouElement element = null;
        boolean flag = true;
        while (flag) {
            int event = parser.next();
            switch (event) {
                case XmlPullParser.START_TAG:
                    String name = parser.getName();
                    switch (name) {
                        case "mobileChat":
                            element = parserElement(parser);
                            break;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getDepth() == depth) {
                        flag = false;
                    }
                    break;
            }
        }

        return element;
    }

    private BasePeidouElement parserElement(XmlPullParser parser) {
        BasePeidouElement element = null;
        String msgtype = parser.getAttributeValue("", "msgtype");
        switch (msgtype) {
            case "text":
                element = new TextElement();
                String content = parser.getAttributeValue("", "content");
                ((TextElement) element).setContent(content);
                break;
            case "img":
                element = new ImgElement();
                String url = parser.getAttributeValue("", "url");
                String property = parser.getAttributeValue("", "property");
                ((ImgElement) element).setUrl(url);
                ((ImgElement) element).setProperty(property);
                break;
            case "audio":
                //语音
                element = new AudioElement();
                String url1 = parser.getAttributeValue("", "url");
                String property1 = parser.getAttributeValue("", "property");
                ((AudioElement) element).setUrl(url1);
                ((AudioElement) element).setProperty(property1);
                break;
            case "location":

                break;

            // TODO: 2019/2/20 其它的后面支持

            default:

                break;
        }
        return element;
    }
}
