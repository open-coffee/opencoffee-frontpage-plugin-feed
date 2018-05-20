package coffee.synyx.frontpage.plugin.feed;

import java.util.List;

class FeedDto {

    private FeedImageDto image;
    private List<FeedEntryDto> entries;

    FeedDto(FeedImageDto image, List<FeedEntryDto> entries) {
        this.image = image;
        this.entries = entries;
    }

    FeedImageDto getImage() {
        return image;
    }

    List<FeedEntryDto> getEntries() {
        return entries;
    }
}
