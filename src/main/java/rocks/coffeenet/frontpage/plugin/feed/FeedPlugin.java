package rocks.coffeenet.frontpage.plugin.feed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rocks.coffeenet.frontpage.plugin.api.ConfigurationDescription;
import rocks.coffeenet.frontpage.plugin.api.ConfigurationField;
import rocks.coffeenet.frontpage.plugin.api.ConfigurationFieldType;
import rocks.coffeenet.frontpage.plugin.api.ConfigurationInstance;
import rocks.coffeenet.frontpage.plugin.api.FrontpagePlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Arrays.asList;
import static rocks.coffeenet.frontpage.plugin.api.ConfigurationFieldType.NUMBER;
import static rocks.coffeenet.frontpage.plugin.api.ConfigurationFieldType.TEXT;
import static rocks.coffeenet.frontpage.plugin.api.ConfigurationFieldType.URL;

@Component
public class FeedPlugin implements FrontpagePlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());

    private static final String FEED_FIELD_TITLE = "feed.field.title";
    static final String FEED_FIELD_URL = "feed.field.url";
    private static final String FEED_FIELD_ENTRY_COUNT = "feed.field.entry.count";
    private static final String FEED_FIELD_ENTRY_LENGTH = "feed.field.entry.length";

    private static final Set<ConfigurationField> CONFIGURATION_FIELDS = Collections.unmodifiableSet(asSet(
        createField("Title", TEXT, FEED_FIELD_TITLE, false),
        createField("URL", URL, FEED_FIELD_URL, true),
        createField("Anzahl Artikel", NUMBER, FEED_FIELD_ENTRY_COUNT, true),
        createField("Teaser Text Länge", NUMBER, FEED_FIELD_ENTRY_LENGTH, true)
    ));

    private final FeedParser feedParser;

    @Autowired
    public FeedPlugin(FeedParser feedParser) {
        this.feedParser = feedParser;
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
            content = HtmlConverter.toHtml(feedParser.parse(feedUrl, entryCount, entryLength));
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

    private static ConfigurationField createField(final String label, final ConfigurationFieldType type, final String id, final boolean required) {
        return new ConfigurationField.Builder()
            .label(label)
            .type(type)
            .id(id)
            .required(required)
            .build();
    }

    private static <T> Set<T> asSet(T... items) {
        return new HashSet<>(asList(items));
    }
}
