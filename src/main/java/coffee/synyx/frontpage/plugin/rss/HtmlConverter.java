package coffee.synyx.frontpage.plugin.rss;

import java.util.List;

class HtmlConverter {

    private HtmlConverter() {
        // ok
    }

    static String toHtml(List<BlogEntry> entries) {
        String html = "";
        for (BlogEntry entry : entries) {
            html += "<div style=\"margin-bottom: 25px;\">";
            html += "<header>";
            html += "<h3 style=\"margin-bottom: 0;\">";
            html += "<a style=\"color: black; text-decoration: none; font-size: large;\" href=\"" + entry.getLink() + "\">" + entry.getTitle() + "</a>";
            html += "</h3>";
            html += "<address style=\"font-size: smaller;\">" + entry.getAuthor() + " - " + entry.getPublishDate() + "</address>";
            html += "</header>";
            html += "<div style=\"margin-top: 10px;\">";
            html += "<a style=\"text-decoration: none; color: inherit; font-size: small;\" href=\"" + entry.getLink() + "\"><span>" + entry.getDescription() + "</span></a>";
            html += "</div>";
            html += "</div>";
        }
        return html;
    }
}
