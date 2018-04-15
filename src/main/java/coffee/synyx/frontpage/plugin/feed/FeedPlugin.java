package coffee.synyx.frontpage.plugin.feed;

import coffee.synyx.frontpage.plugin.api.ConfigurationDescription;
import coffee.synyx.frontpage.plugin.api.ConfigurationField;
import coffee.synyx.frontpage.plugin.api.ConfigurationFieldType;
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

import static coffee.synyx.frontpage.plugin.feed.HtmlConverter.toHtml;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Arrays.asList;

@Component
public class FeedPlugin implements FrontpagePlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());

    private static final String FEED_FIELD_TITLE = "feed.field.title";
    private static final String FEED_FIELD_URL = "feed.field.url";
    private static final String FEED_FIELD_ENTRY_COUNT = "feed.field.entry.count";
    private static final String FEED_FIELD_ENTRY_LENGTH = "feed.field.entry.length";

    private static final Set<ConfigurationField> CONFIGURATION_FIELDS = Collections.unmodifiableSet(new HashSet<>(
        asList(
            createField("Title", ConfigurationFieldType.TEXT, FEED_FIELD_TITLE),
            createField("URL", ConfigurationFieldType.URL, FEED_FIELD_URL),
            createField("Anzahl Artikel", ConfigurationFieldType.NUMBER, FEED_FIELD_ENTRY_COUNT),
            createField("Teaser Text Länge", ConfigurationFieldType.NUMBER, FEED_FIELD_ENTRY_LENGTH)
        )
    ));

    private final BlogParser blogParser;

    @Autowired
    public FeedPlugin(BlogParser blogParser) {
        this.blogParser = blogParser;
    }

    private static ConfigurationField createField(final String label, final ConfigurationFieldType type, final String id) {
        return new ConfigurationField() {
            @Override
            public String getLabel() {
                return label;
            }

            @Override
            public ConfigurationFieldType getType() {
                return type;
            }

            @Override
            public boolean isRequired() {
                return true;
            }

            @Override
            public String getId() {
                return id;
            }
        };
    }

    @Override
    public String title(ConfigurationInstance configurationInstance) {
        return configurationInstance.get(FEED_FIELD_TITLE);
    }

    @Override
    public String content(ConfigurationInstance configurationInstance) {

        final String feedUrl = configurationInstance.get(FEED_FIELD_URL);
        final int entryCount = Integer.parseInt(configurationInstance.get(FEED_FIELD_ENTRY_COUNT));
        final int entryLength = Integer.parseInt(configurationInstance.get(FEED_FIELD_ENTRY_LENGTH));

        String content = "";
        try {
            content = toHtml(blogParser.parse(feedUrl, entryCount, entryLength));
        } catch (ParserException e) {
            LOGGER.error("Feed Plugin: Could not receive feed feed from {}", feedUrl);
        }

        return content;
    }

    @Override
    public String id() {
        return "feed";
    }

    @Override
    public Optional<ConfigurationDescription> getConfigurationDescription() {
        return Optional.of(() -> CONFIGURATION_FIELDS);
    }
}
