package coffee.synyx.frontpage.plugin.feed;

import java.util.List;

public class Feed {

    private String thumbnail;
    private List<BlogEntry> entries;

    public Feed(String thumbnail, List<BlogEntry> entries) {
        this.thumbnail = thumbnail;
        this.entries = entries;
    }

    String getThumbnail() {
        return thumbnail;
    }

    List<BlogEntry> getEntries() {
        return entries;
    }
}
