package rocks.coffeenet.frontpage.plugin.feed;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
class FeedFactory {

    SyndFeed build(URL feedUrl) throws FeedException, IOException {
        SyndFeedInput input = new SyndFeedInput();
        return input.build(new XmlReader(feedUrl));
    }
}
