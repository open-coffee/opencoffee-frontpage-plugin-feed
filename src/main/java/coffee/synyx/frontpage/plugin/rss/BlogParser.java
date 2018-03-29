package coffee.synyx.frontpage.plugin.rss;

import com.rometools.rome.feed.module.DCModule;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;


/**
 * Blog entry parse service.
 *
 * @author Tobias Schneider - schneider@synyx.de
 */
@Component
public class BlogParser implements Parser<BlogEntry> {


    private final FeedFactory feedFactory;

    @Autowired
    public BlogParser(FeedFactory feedFactory) {

        this.feedFactory = feedFactory;
    }

    @Override
    public List<BlogEntry> parse(String blogUrl, int limit, int length) {

        try {
            URL url = new URL(blogUrl);
            SyndFeed feed = feedFactory.build(url);

            return feed.getEntries().stream().limit(limit).map(toBlogEntry(length)).collect(toList());
        } catch (FeedException | IOException e) {
            throw new ParserException("Failed to parse blog with rss link " + blogUrl, e);
        }
    }


    private Function<SyndEntry, BlogEntry> toBlogEntry(int length) {

        return
            entry -> {
                String description = reduceText(entry.getDescription().getValue(), length);
                String date = getPublishDate(entry);

                return new BlogEntry(entry.getTitle(), description, entry.getLink(), entry.getAuthor(), date);
            };
    }


    private String getPublishDate(SyndEntry syndEntry) {

        return syndEntry.getModules()
            .stream()
            .filter(module -> module instanceof DCModule)
            .map(module -> (DCModule) module)
            .map(DCModule::getDate)
            .map(f -> new SimpleDateFormat("d. MMMM yyyy").format(f))
            .findFirst()
            .orElse(null);
    }
}
