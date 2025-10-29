package krafton.bookmark.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BookmarkSaveApiRequest(
        @Schema(description = "북마크 제목", example = "구글", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "title은 비어있을 수 없습니다")
        String title,
        @Schema(description = "북마크 URL", example = "https://www.google.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "url은 비어있을 수 없습니다")
        String url,
        @Schema(description = "북마크 메모", example = "자주 사용하는 검색 엔진", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Pattern(regexp = ".*\\S+.*", message = "memo는 공백일 수 없습니다.")
        String memo
) {
}
