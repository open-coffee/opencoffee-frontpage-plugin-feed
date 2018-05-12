package coffee.synyx.frontpage.plugin.feed;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.util.stream.Collectors.toList;


/**
 * Blog entry parse service.
 *
 * @author Tobias Schneider - schneider@synyx.de
 */
@Component
public class BlogParser implements Parser {


    private final FeedFactory feedFactory;

    @Autowired
    public BlogParser(FeedFactory feedFactory) {

        this.feedFactory = feedFactory;
    }

    @Override
    public Feed parse(String blogUrl, int limit, int length) {

        try {
            URL url = new URL(blogUrl);
            SyndFeed feed = feedFactory.build(url);

            String imageUrl = feed.getImage() != null ? feed.getImage().getUrl() : "";
            List<BlogEntry> blogEntries = feed.getEntries().stream().limit(limit).map(toBlogEntry(length)).collect(toList());

            return new Feed(imageUrl, blogEntries);

        } catch (FeedException | IOException e) {
            throw new ParserException("Failed to parse blog with feed link " + blogUrl, e);
        }
    }

    private Function<SyndEntry, BlogEntry> toBlogEntry(int length) {

        return
            entry -> {
                String article = "";

                if (entry.getDescription() != null) {
                    article = entry.getDescription().getValue();
                } else if (!entry.getContents().isEmpty()) {
                    article = entry.getContents().get(0).getValue();
                }

                String reducedArticle = reduceText(article, length);
                String gregorianPublishedDate = getFormattedPublishedDate(entry, "yyyy-MM-dd HH:mm");
                String userSeenPublishedDate = getFormattedPublishedDate(entry, "d. MMMM yyyy");

                return new BlogEntry(entry.getTitle(), reducedArticle, entry.getLink(), entry.getAuthor(), gregorianPublishedDate, userSeenPublishedDate);
            };
    }


    private String getFormattedPublishedDate(SyndEntry syndEntry, String pattern) {

        Date publishedDate = syndEntry.getPublishedDate();

        String formattedPublishedDate = null;

        if (publishedDate != null) {
            LocalDateTime convertedDate = ofInstant(publishedDate.toInstant(), systemDefault());
            formattedPublishedDate = DateTimeFormatter.ofPattern(pattern).format(convertedDate);
        }

        return formattedPublishedDate;
    }
}
