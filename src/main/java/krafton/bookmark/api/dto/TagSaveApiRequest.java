package krafton.bookmark.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record TagSaveApiRequest(
        @Schema(description = "태그 이름", example = "개발")
        @NotBlank(message = "태그의 이름은 비어있을 수 없습니다.")
        String tagName
) {
}
