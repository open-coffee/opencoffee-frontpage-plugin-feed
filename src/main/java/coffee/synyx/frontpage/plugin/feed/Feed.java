package coffee.synyx.frontpage.plugin.feed;

import java.util.List;

public class Feed {

    private String imageUrl;
    private List<BlogEntry> entries;

    public Feed(String imageUrl, List<BlogEntry> entries) {
        this.imageUrl = imageUrl;
        this.entries = entries;
    }

    String getImageUrl() {
        return imageUrl;
    }

    List<BlogEntry> getEntries() {
        return entries;
    }
}
