package krafton.bookmark.api.dto;

public record BookmarkSaveApiRequest(
        String title,
        String url,
        String memo
) {
}
