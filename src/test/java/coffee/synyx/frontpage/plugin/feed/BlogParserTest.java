package coffee.synyx.frontpage.plugin.feed;

import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BlogParserTest {

    private BlogParser sut;

    @Mock
    private FeedFactory feedFactory;

    @Before
    public void setUp() {

        sut = new BlogParser(feedFactory);
    }


    @Test
    public void parseBlogRss() throws FeedException, IOException {

        SyndContentImpl description = new SyndContentImpl();
        description.setValue("description");

        SyndEntryImpl syndEntry = new SyndEntryImpl();
        syndEntry.setDescription(description);
        syndEntry.setLink("link");
        syndEntry.setAuthor("author");
        syndEntry.setTitle("title");
        syndEntry.setPublishedDate(Date.from(Instant.parse("2014-12-03T10:15:30.00Z")));

        SyndFeed result = new SyndFeedImpl();
        result.setEntries(singletonList(syndEntry));

        when(feedFactory.build(any(URL.class))).thenReturn(result);

        List<BlogEntry> blogEntries = sut.parse("http://blog/feed/", 10, 150);

        assertThat(blogEntries, hasSize(1));
        assertThat(blogEntries.get(0).getDescription(), is("description"));
        assertThat(blogEntries.get(0).getLink(), is("link"));
        assertThat(blogEntries.get(0).getPublishDate(), is("3. December 2014"));
        assertThat(blogEntries.get(0).getAuthor(), is("author"));
        assertThat(blogEntries.get(0).getTitle(), is("title"));
    }

    @Test
    public void parseBlogAtom() throws FeedException, IOException {

        SyndContentImpl content = new SyndContentImpl();
        content.setValue("content");

        SyndEntryImpl syndEntry = new SyndEntryImpl();
        syndEntry.setContents(singletonList(content));
        syndEntry.setPublishedDate(Date.from(Instant.parse("2014-12-03T10:15:30.00Z")));

        SyndFeed result = new SyndFeedImpl();
        result.setEntries(singletonList(syndEntry));

        when(feedFactory.build(any(URL.class))).thenReturn(result);

        List<BlogEntry> blogEntries = sut.parse("http://blog/feed/", 10, 150);

        assertThat(blogEntries, hasSize(1));
        assertThat(blogEntries.get(0).getDescription(), is("content"));
        assertThat(blogEntries.get(0).getPublishDate(), is("3. December 2014"));
    }


    @Test(expected = ParserException.class)
    public void parseBlogInvalidFeed() throws FeedException, IOException {

        when(feedFactory.build(any(URL.class))).thenThrow(new FeedException("foo"));

        sut.parse("http://intblog/feed/", 10, 1);
    }


    @Test(expected = ParserException.class)
    public void parseBlogMalformedUrl() {

        sut.parse("/feed/", 10, 1);
    }

    @Test
    public void reduceTextButTextIsLessThanMaxLength() {

        final String thisIsAShortText = "thisIsAShortText";
        final String reducedText = sut.reduceText(thisIsAShortText, 16);
        assertThat(reducedText, is(thisIsAShortText));
    }

    @Test
    public void reduceTextAndTextIsGreaterThanMaxLength() {

        final String longText = "This is a very long text with more than 10 characters";
        final String reducedText = sut.reduceText(longText, 10);
        assertThat(reducedText, is("This is a ..."));
    }

    @Test
    public void reduceTextAndTextIsGreaterThanMaxLengthWithLink() {

        final String longText = "This is <a href=\"test\">a Link</a> and a very long text with more than 10 characters";
        final String reducedText = sut.reduceText(longText, 11);
        assertThat(reducedText, is("This is \n<a href=\"test\">a Link</a>..."));
    }
}
