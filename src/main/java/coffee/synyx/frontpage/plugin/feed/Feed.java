package coffee.synyx.frontpage.plugin.feed;

import java.util.List;

class Feed {

    private FeedImage image;
    private List<BlogEntry> entries;

    Feed(FeedImage image, List<BlogEntry> entries) {
        this.image = image;
        this.entries = entries;
    }

    FeedImage getImage() {
        return image;
    }

    List<BlogEntry> getEntries() {
        return entries;
    }
}
