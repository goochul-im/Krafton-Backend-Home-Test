package krafton.bookmark.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record BookmarkUpdateApiRequest(
        @Schema(description = "북마크 제목", example = "구글", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String title,
        @Schema(description = "북마크 URL", example = "https://www.google.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String url,
        @Schema(description = "북마크 메모", example = "자주 사용하는 검색 엔진", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String memo,
        @Schema(description = "태그 ID", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Long tagId
) {
}
