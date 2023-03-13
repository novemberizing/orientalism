package net.novemberizing.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupUtil {
    public static Document parse(String html) {
        return null;
    }
    public static Element getElementById(String html, String id) {
        return null;
    }
    public static String str(String html, String id) {
        StringBuilder builder = new StringBuilder();

        Document document = Jsoup.parse(html);
        Element element = document.getElementById(id);
        if(element != null) {
            Elements elements = element.children();
            for(Element child : elements) {
                builder.append(child.toString());
            }
        }

        return builder.toString();
    }
}
