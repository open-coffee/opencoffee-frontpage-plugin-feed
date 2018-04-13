package coffee.synyx.frontpage.plugin.rss;


import coffee.synyx.frontpage.plugin.api.ConfigurationInstance;
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

    private RSSPlugin sut;

    @Mock
    private BlogParser blogParser;

    private ConfigurationInstance configurationInstance = key -> {
        switch (key) {
            case "rss.field.title":
                return "title";
            case "rss.field.url":
                return "url";
            case "rss.field.entry.count":
                return "10";
            case "rss.field.entry.length":
                return "100";
            default:
                return "";
        }
    };

    @Before
    public void setUp() {
        sut = new RSSPlugin(blogParser);
    }

    @Test
    public void returnsCorrectHtml() {

        BlogEntry blogEntry = new BlogEntry("title", "description", "link", "author", "date");
        when(blogParser.parse("url", 10, 100)).thenReturn(singletonList(blogEntry));

        assertThat(sut.title(configurationInstance)).isEqualTo("title");
        assertThat(sut.content(configurationInstance)).contains("author - date", "title", "description", "link");
        assertThat(sut.id()).isEqualTo("rss");
    }

    @Test
    public void throwsParserException() {

        when(blogParser.parse("title", 10, 100)).thenThrow(new ParserException("message", new Throwable()));
        assertThat(sut.content(configurationInstance)).isEqualTo("");
    }
}
