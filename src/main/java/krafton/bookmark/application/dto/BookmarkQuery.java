package krafton.bookmark.application.dto;

public record BookmarkQuery(
        String title,
        String url,
        Long tagId
) {
}
