package rocks.coffeenet.frontpage.plugin.feed;

import coffee.synyx.frontpage.plugin.api.ConfigurationField;
import coffee.synyx.frontpage.plugin.api.validation.ConfigurationFieldValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class FeedUrlValidator implements ConfigurationFieldValidator {

    private final FeedParser feedParser;

    @Autowired
    public FeedUrlValidator(FeedParser feedParser) {
        this.feedParser = feedParser;
    }

    @Override
    public boolean supports(ConfigurationField field) {
        // TODO is the id enough? there could be collisions with other plugins since it is a string
        return FeedPlugin.FEED_FIELD_URL.equals(field.getId());
    }

    @Override
    public void validate(Object target, ConfigurationField field, Errors errors) {
        try {
            feedParser.parse(String.valueOf(target), 1, 42);
        } catch(ParserException e) {
            errors.rejectValue(field.getId(), "plugin.feed.validation.error.url");
        }
    }
}
