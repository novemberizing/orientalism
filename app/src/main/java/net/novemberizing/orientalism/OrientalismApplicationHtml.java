package net.novemberizing.orientalism;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import net.novemberizing.core.JsoupUtil;
import net.novemberizing.core.StringUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OrientalismApplicationHtml {
    private static final String paragraphAppendText = "\n\n";
    private static final String paragraphTag = "p";
    private static final String blockquoteTag = "blockquote";

    private static final String blockquoteColorStr = "#F0F0F0";
    private static final Integer blockquoteStripeWidth = 20;
    private static final Integer blockquoteGapWidth = 40;

    public static String from(Element element) {
        StringBuilder builder = new StringBuilder();
        Elements elements = element.children();
        for(int i = 0; i < elements.size(); i++) {
            Element child = elements.get(i);
            if(StringUtil.equal(JsoupUtil.str(child.tag()), paragraphTag)) {
                builder.append(child.text());
                if(i + 1 != elements.size()) {
                    builder.append(paragraphAppendText);
                }
            } else {
                builder.append(child.text());
            }
        }
        return builder.toString();
    }

    public static SpannableStringBuilder from(String html) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        Document document = Jsoup.parse(html);
        Element body = document.body();
        Elements elements = body.children();

        for(int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            if(StringUtil.equal(JsoupUtil.str(element.tag()), paragraphTag)) {
                builder.append(element.text());
                if(i + 1 != elements.size()) {
                    builder.append(paragraphAppendText);
                }
            } else if(StringUtil.equal(JsoupUtil.str(element.tag()), blockquoteTag)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    String text = OrientalismApplicationHtml.from(element);
                    builder.append(text, new QuoteSpan(Color.parseColor(blockquoteColorStr), blockquoteStripeWidth, blockquoteGapWidth), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setSpan(new StyleSpan(Typeface.ITALIC), builder.length() - text.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setSpan(new RelativeSizeSpan(0.8f), builder.length() - text.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    builder.append(element.text());
                }
                if(i + 1 != elements.size()) {
                    builder.append(paragraphAppendText);
                }
            } else {
                builder.append(element.text());
            }
        }
        return builder;
    }
}
