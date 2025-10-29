package krafton.bookmark.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record BookmarkQuery(
        @Schema(description = "검색할 북마크 제목 (부분 일치)", example = "구글")
        String title,
        @Schema(description = "검색할 북마크 URL (부분 일치)", example = "google.com")
        String url,
        @Schema(description = "검색할 태그 ID", example = "1")
        Long tagId
) {
}
