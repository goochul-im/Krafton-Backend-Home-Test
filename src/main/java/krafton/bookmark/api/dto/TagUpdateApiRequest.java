package krafton.bookmark.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record TagUpdateApiRequest(
        @Schema(description = "수정할 태그 이름", example = "새로운 태그 이름")
        @NotBlank(message = "태그의 이름은 비어있을 수 없습니다.")
        String updateName
) {
}
