package krafton.bookmark.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TagSaveApiRequest(
        @Schema(description = "태그 이름", example = "개발")
        String tagName
) {
}
