package krafton.bookmark.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record TagResponse(
        @Schema(description = "태그 ID", example = "1")
        Long id,
        @Schema(description = "태그 이름", example = "개발")
        String name,
        @Schema(description = "생성일시", example = "2023-01-01T10:00:00")
        LocalDateTime createdAt,
        @Schema(description = "수정일시", example = "2023-01-01T10:00:00")
        LocalDateTime updatedAt
) {
}
