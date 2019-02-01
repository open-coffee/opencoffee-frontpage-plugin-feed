package rocks.coffeenet.frontpage.plugin.feed;

import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.feed.synd.SyndImage;
import com.rometools.rome.feed.synd.SyndImageImpl;
import com.rometools.rome.io.FeedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedParserTest {

    private FeedParser sut;

    @Mock
    private FeedFactory feedFactory;

    @Before
    public void setUp() {

        sut = new FeedParser(feedFactory);
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
        syndEntry.setPublishedDate(getDate());

        SyndImage syndImage = new SyndImageImpl();
        syndImage.setUrl("some-url");
        syndImage.setLink("some-link");

        SyndFeed result = new SyndFeedImpl();
        result.setEntries(singletonList(syndEntry));
        result.setImage(syndImage);

        when(feedFactory.build(any(URL.class))).thenReturn(result);

        FeedDto feedDto = sut.parse("http://blog/feed/", 10, 150);

        final List<FeedEntryDto> blogEntries = feedDto.getEntries();
        assertThat(blogEntries, hasSize(1));
        assertThat(blogEntries.get(0).getDescription(), is("description"));
        assertThat(blogEntries.get(0).getLink(), is("link"));
        assertThat(blogEntries.get(0).getUserSeenPublishedDate(), is("3. December 2014"));
        assertThat(blogEntries.get(0).getGregorianPublishedDate(), is("2014-12-03 10:15"));
        assertThat(blogEntries.get(0).getAuthor(), is("author"));
        assertThat(blogEntries.get(0).getTitle(), is("title"));

        final FeedImageDto image = feedDto.getImage();
        assertThat(image.getUrl(), is("some-url"));
        assertThat(image.getLink(), is("some-link"));
    }

    @Test
    public void parseBlogAtom() throws FeedException, IOException {

        SyndContentImpl content = new SyndContentImpl();
        content.setValue("content");

        SyndEntryImpl syndEntry = new SyndEntryImpl();
        syndEntry.setContents(singletonList(content));
        syndEntry.setPublishedDate(getDate());

        final SyndImageImpl image = new SyndImageImpl();
        image.setUrl("http://this-is-a-test-url.coffee");

        SyndFeed result = new SyndFeedImpl();
        result.setImage(image);
        result.setEntries(singletonList(syndEntry));

        when(feedFactory.build(any(URL.class))).thenReturn(result);

        FeedDto feedDto = sut.parse("http://blog/feed/", 10, 150);

        assertThat(feedDto.getImage().getUrl(), is("http://this-is-a-test-url.coffee"));

        List<FeedEntryDto> blogEntries = feedDto.getEntries();

        assertThat(blogEntries, hasSize(1));
        assertThat(blogEntries.get(0).getDescription(), is("content"));
        assertThat(blogEntries.get(0).getGregorianPublishedDate(), is("2014-12-03 10:15"));
        assertThat(blogEntries.get(0).getUserSeenPublishedDate(), is("3. December 2014"));
    }

    @Test
    public void parseBlogButPublishedDateIsNull() throws FeedException, IOException {

        SyndFeed result = new SyndFeedImpl();
        result.setEntries(singletonList(new SyndEntryImpl()));

        when(feedFactory.build(any(URL.class))).thenReturn(result);

        FeedDto feedDto = sut.parse("http://blog/feed/", 10, 150);

        List<FeedEntryDto> blogEntries = feedDto.getEntries();

        assertThat(blogEntries, hasSize(1));
        assertThat(blogEntries.get(0).getGregorianPublishedDate(), is(nullValue()));
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

    private Date getDate() {
        LocalDateTime datetime = LocalDateTime.of(2014, 12, 3, 10, 15);
        return new Date(datetime.toInstant(ZoneOffset.of(ZoneId.systemDefault().getRules().getOffset(datetime).getId())).getEpochSecond() * 1000);
    }
}
