package rocks.coffeenet.frontpage.plugin.feed;

import coffee.synyx.frontpage.plugin.api.ConfigurationField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.MapBindingResult;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedUrlValidatorTest {

    private final FeedParser feedParser = mock(FeedParser.class);
    private final FeedUrlValidator sut = new FeedUrlValidator(feedParser);


    @Test
    public void ensureSupportsReturnsTrueForURLField() {

        final ConfigurationField configField = createConfigurationFieldWithId(FeedPlugin.FEED_FIELD_URL);

        boolean actual = sut.supports(configField);
        assertThat(actual).isTrue();
    }

    @Test
    public void ensureSupportsReturnsFalseForNonURLField() {

        final ConfigurationField configField = createConfigurationFieldWithId("my-config-field-id");

        boolean actual = sut.supports(configField);
        assertThat(actual).isFalse();
    }

    @Test
    public void ensureValidateRejectsInvalidUrl() {

        final String url = "my-url";
        final ConfigurationField configField = createConfigurationFieldWithId("my-config-field-id");
        final MapBindingResult errors = new MapBindingResult(emptyMap(), "plugin-id");

        when(feedParser.parse(anyString(), anyInt(), anyInt())).thenThrow(new ParserException("whoops", new RuntimeException()));

        sut.validate(url, configField, errors);

        assertThat(errors.hasFieldErrors("my-config-field-id")).isTrue();
        assertThat(errors.getFieldError("my-config-field-id").getCode()).isEqualTo("plugin.feed.validation.error.url");
        verify(feedParser).parse(eq(url), eq(1), eq(42));
    }

    @Test
    public void ensureValidateDoesNotRejectValidUrl() {

        final String url = "my-url";
        final ConfigurationField configField = createConfigurationFieldWithId("my-config-field-id");
        final MapBindingResult errors = new MapBindingResult(emptyMap(), "plugin-id");

        sut.validate(url, configField, errors);

        assertThat(errors.hasFieldErrors("my-config-field-id")).isFalse();
    }

    private static ConfigurationField createConfigurationFieldWithId(String id) {
        return new ConfigurationField.Builder().id(id).build();
    }
}
