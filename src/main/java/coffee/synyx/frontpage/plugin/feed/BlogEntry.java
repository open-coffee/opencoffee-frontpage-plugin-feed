package coffee.synyx.frontpage.plugin.feed;

/**
 * Representation of a blog entry.
 *
 * @author Tobias Schneider - schneider@synyx.de
 */
final class BlogEntry {

    private final String title;
    private final String author;
    private final String description;
    private final String link;
    private final String publishDate;

    BlogEntry(String title, String description, String link, String author, String publishDate) {

        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
        this.publishDate = publishDate;
    }

    String getDescription() {

        return description;
    }


    String getLink() {

        return link;
    }


    String getAuthor() {

        return author;
    }


    String getTitle() {

        return title;
    }


    String getPublishDate() {

        return publishDate;
    }
}
