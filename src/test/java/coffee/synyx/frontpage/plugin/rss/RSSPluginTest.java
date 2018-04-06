package coffee.synyx.frontpage.plugin.rss;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RSSPluginTest {

    private static final String BLOG_URL = "https://www.synyx.de/blog/index.xml";
    private static final int BLOG_ENTRY_COUNT = 10;
    private static final int DESCRIPTION_LENGTH = 200;

    private RSSPlugin sut;

    @Mock
    private BlogParser blogParser;


    @Before
    public void setUp() {
        sut = new RSSPlugin(blogParser);
    }

    @Test
    public void returnsCorrectHtml() {

        BlogEntry blogEntry = new BlogEntry("title", "description", "link", "author", "date");
        when(blogParser.parse(BLOG_URL, BLOG_ENTRY_COUNT, DESCRIPTION_LENGTH)).thenReturn(singletonList(blogEntry));

        assertThat(sut.title()).isEqualTo("Synyx Blog");
        assertThat(sut.content()).contains("author - date", "title", "description", "link");
        assertThat(sut.id()).isEqualTo("rss");
    }

    //
    @Test
    public void throwsParserException() {

        when(blogParser.parse(BLOG_URL, BLOG_ENTRY_COUNT, DESCRIPTION_LENGTH)).thenThrow(new ParserException("message", new Throwable()));
        assertThat(sut.content()).isEqualTo("");
    }
}
