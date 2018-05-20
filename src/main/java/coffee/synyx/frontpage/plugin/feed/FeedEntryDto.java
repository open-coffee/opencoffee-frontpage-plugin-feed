package coffee.synyx.frontpage.plugin.feed;

/**
 * Representation of a blog entry.
 *
 * @author Tobias Schneider - schneider@synyx.de
 */
final class FeedEntryDto {

    private final String title;
    private final String author;
    private final String description;
    private final String link;
    private final String gregorianPublishedDate;
    private final String userSeenPublishedDate;

    FeedEntryDto(String title, String description, String link, String author, String gregorianPublishedDate, String userSeenPublishedDate) {

        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
        this.gregorianPublishedDate = gregorianPublishedDate;
        this.userSeenPublishedDate = userSeenPublishedDate;
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


    String getGregorianPublishedDate() {

        return gregorianPublishedDate;
    }

    String getUserSeenPublishedDate() {

        return userSeenPublishedDate;
    }
}
