package coffee.synyx.frontpage.plugin.rss;

import coffee.synyx.frontpage.plugin.api.FrontpagePluginInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static coffee.synyx.frontpage.plugin.rss.HtmlConverter.toHtml;
import static java.lang.invoke.MethodHandles.lookup;

@Component
public class RSSPlugin implements FrontpagePluginInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());

    private static final String BLOG_URL = "https://www.synyx.de/blog/index.xml";
    private static final int BLOG_ENTRY_COUNT = 10;
    private static final int DESCRIPTION_LENGTH = 200;

    private final BlogParser blogParser;

    @Autowired
    public RSSPlugin(BlogParser blogParser) {
        this.blogParser = blogParser;
    }

    @Override
    public String title() {
        return "Synyx Blog";
    }

    @Override
    public String content() {
        String content = "";
        try {
            content = toHtml(blogParser.parse(BLOG_URL, BLOG_ENTRY_COUNT, DESCRIPTION_LENGTH));
        } catch (ParserException e) {
            LOGGER.error("RSS Plugin: Could not receive rss feed from {}", BLOG_URL);
        }

        return content;
    }

    @Override
    public String id() {
        return "rss";
    }
}
