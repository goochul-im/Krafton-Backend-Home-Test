package krafton.bookmark.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record BookmarkResponse(
        @Schema(description = "북마크 ID", example = "1")
        Long id,
        @Schema(description = "북마크 제목", example = "구글")
        String title,
        @Schema(description = "북마크 URL", example = "https://www.google.com")
        String url,
        @Schema(description = "북마크 메모", example = "자주 사용하는 검색 엔진")
        String memo,
        @Schema(description = "생성일시", example = "2023-01-01T10:00:00")
        LocalDateTime createdAt,
        @Schema(description = "수정일시", example = "2023-01-01T10:00:00")
        LocalDateTime updatedAt,
        @Schema(description = "태그 정보")
        TagResponse tag
) {
}
