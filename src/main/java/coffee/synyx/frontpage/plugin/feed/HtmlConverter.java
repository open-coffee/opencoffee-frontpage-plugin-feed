package coffee.synyx.frontpage.plugin.feed;

import java.util.Objects;

class HtmlConverter {

    private HtmlConverter() {
        // ok
    }

    static String toHtml(FeedDto feedDto) {
        String html = "";

        final FeedImageDto image = feedDto.getImage();
        if (!Objects.equals(image.getUrl(), "")) {
            if (!Objects.equals(image.getLink(), "")) {
                html += "<a href=\"" + image.getLink() + "\" title=\"" + image.getTitle() + "\" rel=\"noopener\">";
            }
            html += " <img height=\"" + image.getHeight() + "\" width=\"" + image.getWidth() + "\" src=\"" + image.getUrl() + "\" alt=\"" + image.getDescription() + "\"/>";
            if (!Objects.equals(image.getLink(), "")) {
                html += "</a>";
            }
        }

        for (FeedEntryDto entry : feedDto.getEntries()) {
            html += "<div style=\"margin-bottom: 25px;\">";
            html += "  <header>";
            html += "    <h3 style=\"margin-bottom: 0;\">";
            html += "      <a style=\"color: black; text-decoration: none; font-size: large;\" target=\"_blank\" rel=\"noopener\" href=\"" + entry.getLink() + "\">" + entry.getTitle() + "</a>";
            html += "    </h3>";
            html += "    <address style=\"font-size: smaller;\">";

            if (!Objects.equals(entry.getAuthor(), "")) {
                html += entry.getAuthor() + " - ";
            }

            html += "<time pubdate datetime=\"" + entry.getGregorianPublishedDate() + "\">" + entry.getUserSeenPublishedDate() + "</address>";
            html += "  </header>";
            html += "  <div style=\"margin-top: 10px;\">";
            html += "    <a style=\"text-decoration: none; color: inherit; font-size: small;\" target=\"_blank\" rel=\"noopener\" href=\"" + entry.getLink() + "\"><span>" + entry.getDescription() + "</span></a>";
            html += "  </div>";
            html += "</div>";
        }
        return html;
    }
}
