package coffee.synyx.frontpage.plugin.feed;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.List;

import static java.lang.Math.min;


/**
 * Interface for different feed parser implementations
 *
 * @author Tobias Schneider - schneider@synyx.de
 */
public interface Parser<T> {

    /**
     * @param url    of blog e.g.
     * @param limit  of entries
     * @param length of description
     * @return List of elements from type T
     */
    Feed parse(String url, int limit, int length);


    /**
     * Reduces a text.
     *
     * @param text      to reduce
     * @param maxLength max length of text
     * @return cropped text
     */
    default String reduceText(String text, int maxLength) {

        String html = Jsoup.parse(text).body().html();

        if (html.length() <= maxLength) {
            return html;
        }

        Element bodyCropped = Jsoup.parse(text.substring(0, maxLength)).body();
        int textToHtmlDiff = bodyCropped.html().length() - bodyCropped.text().length();

        int minLengthToCrop = min(maxLength + textToHtmlDiff, html.length());
        String substring = html.substring(0, minLengthToCrop);

        int lastStartA = substring.lastIndexOf("<a");
        int lastCloseA = substring.lastIndexOf("</a>");

        int nextAfterLastCloseA = html.indexOf("</a>", lastStartA);

        if (lastStartA > lastCloseA) {
            substring = html.substring(0, nextAfterLastCloseA + "</a>".length());
        }

        return substring + "...";
    }
}
