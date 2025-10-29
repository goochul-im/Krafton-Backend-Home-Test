package krafton.bookmark.api.dto;

public record BookmarkUpdateApiRequest(
        String title,
        String url,
        String memo,
        Long tagId
) {
}
