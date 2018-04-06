package coffee.synyx.frontpage.plugin.rss;

import coffee.synyx.frontpage.plugin.api.ConfigurationDescription;
import coffee.synyx.frontpage.plugin.api.ConfigurationField;
import coffee.synyx.frontpage.plugin.api.ConfigurationInstance;
import coffee.synyx.frontpage.plugin.api.FrontpagePlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static coffee.synyx.frontpage.plugin.rss.HtmlConverter.toHtml;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Arrays.asList;

@Component
public class RSSPlugin implements FrontpagePlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());

    private static final String RSS_FIELD_TITLE = "rss.field.title";
    private static final String RSS_FIELD_URL = "rss.field.url";
    private static final String RSS_FIELD_ENTRY_COUNT = "rss.field.entry.count";
    private static final String RSS_FIELD_ENTRY_LENGTH = "rss.field.entry.length";

    private static final Set<ConfigurationField> CONFIGURATION_FIELDS = Collections.unmodifiableSet(new HashSet<>(
        asList(
            createField("Title", "text", RSS_FIELD_TITLE),
            createField("URL", "text", RSS_FIELD_URL),
            createField("Anzahl Artikel", "text", RSS_FIELD_ENTRY_COUNT),
            createField("Teaser Text Länge", "text", RSS_FIELD_ENTRY_LENGTH)
        )
    ));

    private final BlogParser blogParser;

    @Autowired
    public RSSPlugin(BlogParser blogParser) {
        this.blogParser = blogParser;
    }

    private static ConfigurationField createField(final String label, final String type, final String id) {
        return new ConfigurationField() {
            @Override
            public String getLabel() {
                return label;
            }

            @Override
            public String getType() {
                return type;
            }

            @Override
            public String getId() {
                return id;
            }
        };
    }

    @Override
    public String title(ConfigurationInstance configurationInstance) {
        return configurationInstance.get(RSS_FIELD_TITLE);
    }

    @Override
    public String content(ConfigurationInstance configurationInstance) {

        final String feedUrl = configurationInstance.get(RSS_FIELD_URL);
        final int entryCount = Integer.parseInt(configurationInstance.get(RSS_FIELD_ENTRY_COUNT));
        final int entryLength = Integer.parseInt(configurationInstance.get(RSS_FIELD_ENTRY_LENGTH));

        String content = "";
        try {
            content = toHtml(blogParser.parse(feedUrl, entryCount, entryLength));
        } catch (ParserException e) {
            LOGGER.error("RSS Plugin: Could not receive rss feed from {}", feedUrl);
        }

        return content;
    }

    @Override
    public String id() {
        return "rss";
    }

    @Override
    public Optional<ConfigurationDescription> getConfigurationDescription() {
        return Optional.of(() -> CONFIGURATION_FIELDS);
    }
}
