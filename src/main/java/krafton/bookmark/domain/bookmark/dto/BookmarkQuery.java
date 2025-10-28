package krafton.bookmark.domain.bookmark.dto;

public record BookmarkQuery(
        String title,
        String url,
        Long tagId
) {
}
