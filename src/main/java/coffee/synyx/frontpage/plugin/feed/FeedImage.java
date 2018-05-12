package coffee.synyx.frontpage.plugin.feed;

class FeedImage {

    private final String url;
    private final String link;
    private final String description;
    private final String width;
    private final String height;
    private final String title;

    FeedImage(String url, String link, String description, String width, String height, String title) {
        this.url = url;
        this.link = link;
        this.description = description;
        this.width = width;
        this.height = height;
        this.title = title;
    }

    String getUrl() {
        return url;
    }

    String getLink() {
        return link;
    }

    String getDescription() {
        return description;
    }

    String getWidth() {
        return width;
    }

    String getHeight() {
        return height;
    }

    String getTitle() {
        return title;
    }
}
