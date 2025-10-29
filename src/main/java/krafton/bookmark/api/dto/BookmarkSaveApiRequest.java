package krafton.bookmark.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record BookmarkSaveApiRequest(
        @Schema(description = "북마크 제목", example = "구글")
        String title,
        @Schema(description = "북마크 URL", example = "https://www.google.com")
        String url,
        @Schema(description = "북마크 메모", example = "자주 사용하는 검색 엔진")
        String memo
) {
}
