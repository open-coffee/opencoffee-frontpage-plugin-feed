package coffee.synyx.frontpage.plugin.feed;


import coffee.synyx.frontpage.plugin.api.ConfigurationDescription;
import coffee.synyx.frontpage.plugin.api.ConfigurationInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedPluginTest {

    private FeedPlugin sut;

    @Mock
    private BlogParser blogParser;

    private ConfigurationInstance configurationInstance = key -> {
        switch (key) {
            case "feed.field.title":
                return "title";
            case "feed.field.url":
                return "url";
            case "feed.field.entry.count":
                return "10";
            case "feed.field.entry.length":
                return "100";
            default:
                return "";
        }
    };

    @Before
    public void setUp() {
        sut = new FeedPlugin(blogParser);
    }

    @Test
    public void returnsCorrectHtml() {

        BlogEntry blogEntry = new BlogEntry("title", "description", "link", "author", "date", "formattedDate");
        when(blogParser.parse("url", 10, 100)).thenReturn(singletonList(blogEntry));

        assertThat(sut.title(configurationInstance)).isEqualTo("title");
        assertThat(sut.content(configurationInstance)).contains("author", "date", "formattedDate", "title", "description", "link");
        assertThat(sut.id()).isEqualTo("feed");
    }

    @Test
    public void returnsCorrectHtmlWithoutAuthor() {

        BlogEntry blogEntry = new BlogEntry("title", "description", "link", "", "date", "formattedDate");
        when(blogParser.parse("url", 10, 100)).thenReturn(singletonList(blogEntry));

        assertThat(sut.content(configurationInstance)).doesNotContain("author");
    }

    @Test
    public void throwsParserException() {

        when(blogParser.parse("url", 10, 100)).thenThrow(new ParserException("message", new Throwable()));
        assertThat(sut.content(configurationInstance)).isEqualTo("");
    }

    @Test
    public void numberOfConfigurationFieldsAreCorrect() {

        final Optional<ConfigurationDescription> optionalConfigurationDescription = sut.getConfigurationDescription();
        assertThat(optionalConfigurationDescription.get().getConfigurations()).hasSize(4);
    }
}
